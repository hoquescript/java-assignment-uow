import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class Assignment_3 {
	public Assignment_3() throws IOException {
		String path = "/Users/wahid/ContentReader.java";
		String content = Files.readString(Paths.get(path));
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());

		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor tree = new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration declare) {
				String modifier = getModifiersString(declare.getModifiers());
				System.out.println("Line: " + cu.getLineNumber(declare.getStartPosition()) + " Method Declaration: "
						+ modifier + " " + declare.getReturnType2() + " " + declare.getName());
				declare.accept(new ASTVisitor() {
					@Override
					public boolean visit(MethodInvocation node) {
						IMethodBinding methodBinding = node.resolveMethodBinding();
						System.out.println(node.resolveMethodBinding());
						
						System.out.println(node.typeArguments());
						Expression expression = node.getExpression();
						if (expression instanceof SimpleName) {
							SimpleName name = (SimpleName) expression;
							int lineNumber = cu.getLineNumber(node.getStartPosition());
							System.out.println("Line: " + lineNumber + " " + name.getIdentifier());
							System.out.println(cu.findDeclaringNode(content));
//							node.getParent().getParent().accept(new ASTVisitor() {
//								public boolean visit(MethodInvocation node) {
//									System.out.println("X" + lineNumber + " " + node.getName());
//									return true;
//								};
//							});
						}
						return super.visit(node);
					}
				});
				return true;
			}

			private String getModifiersString(int modifiers) {
				StringBuilder modifierString = new StringBuilder();
				if (Modifier.isPublic(modifiers))
					modifierString.append("public ");
				if (Modifier.isProtected(modifiers))
					modifierString.append("protected ");
				if (Modifier.isPrivate(modifiers))
					modifierString.append("private ");
				if (Modifier.isAbstract(modifiers))
					modifierString.append("abstract ");
				if (Modifier.isStatic(modifiers))
					modifierString.append("static ");
				if (Modifier.isFinal(modifiers))
					modifierString.append("final ");
				return modifierString.toString().trim();
			}
		};

		cu.accept(tree);
	}

}
