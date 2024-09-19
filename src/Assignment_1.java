import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Assignment_1 {
	public Assignment_1() throws IOException {
		String path = "/Users/wahid/ContentReader.java";
		String content = Files.readString(Paths.get(path));
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());

		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor tree = new ASTVisitor() {
			boolean hasVariables = false;

			@Override
			public boolean visit(MethodDeclaration node) {
				System.out.println("Line: " + cu.getLineNumber(node.getStartPosition()) + " Method Declaration: "
						+ node.getName().getIdentifier());
				System.out.println("Variables declared in this method declaration:");

				node.accept(new ASTVisitor() {
					int index = 1;

					@Override
					public boolean visit(VariableDeclarationFragment fragment) {
						hasVariables = true;
						System.out.println("[" + index + "]" + "Line: " + cu.getLineNumber(fragment.getStartPosition())
								+ " Variable Name: " + fragment.getName().getIdentifier());
						return super.visit(fragment);
					}

					@Override
					public void endVisit(VariableDeclarationFragment node) {
						index += 1;
						super.endVisit(node);
					}

				});
				return true;
			}

			@Override
			public void endVisit(MethodDeclaration node) {
				if (!hasVariables) {
					System.out.println("None");
				}
				System.out.println("");
				super.endVisit(node);
			}

		};

		cu.accept(tree);
	}

}
