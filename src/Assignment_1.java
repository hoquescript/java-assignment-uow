import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Assignment_1 extends Assignment {
	public Assignment_1(CompilationUnit cu) {
		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration node) {
				System.out.printf("Line: %d Method Declaration: %s%n", cu.getLineNumber(node.getStartPosition()),
						node.getName().getIdentifier());
				System.out.println("Variables declared in this method declaration:");
				node.accept(new ASTVisitor() {
					// Tracks variable declaration count inside the method
					int index = 0;

					@Override
					public boolean visit(VariableDeclarationFragment fragment) {
						// Incrementing the count after visiting VariableDeclarationFragment node
						index += 1;

						int line = cu.getLineNumber(fragment.getStartPosition());
						String variable = fragment.getName().getIdentifier();
						System.out.printf("[%d] Line: %d Variable Name: %s%n", index, line, variable);

						return super.visit(fragment);
					}

					@Override
					public void endVisit(MethodDeclaration node) {
						// If no variables were declared in the method, prints "None"
						if (index == 0) {
							System.out.println("None");
						}
						System.out.println("");
						super.endVisit(node);
					}

				});
				return true;
			}
		};

		cu.accept(tree);
	}

}
