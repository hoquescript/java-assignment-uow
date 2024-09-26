import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

public class Assignment_2 {
	public Assignment_2(CompilationUnit cu) {
		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				processInvocation("Method Call", node.getExpression() != null ? node.getExpression().toString() : "",
						node.getName().getIdentifier(), node.arguments(), cu.getLineNumber(node.getStartPosition()),
						cu);
				return super.visit(node);
			}

			@Override
			public boolean visit(ClassInstanceCreation node) {
				processInvocation("Constructor Call", "new " + node.getType().toString(), "", node.arguments(),
						cu.getLineNumber(node.getStartPosition()), cu);
				return super.visit(node);
			}

			@Override
			public boolean visit(SuperConstructorInvocation node) {
				processInvocation("Super Constructor Call", "super", "", node.arguments(),
						cu.getLineNumber(node.getStartPosition()), cu);
				return super.visit(node);
			}

			/**
			 * A generic method to process invocations (method calls, constructors, super,
			 * etc.)
			 *
			 * @param invocationType Type of invocation (e.g., "Method Call", "Constructor
			 *                       Call")
			 * @param identifier     The expression or class name (e.g., method name or
			 *                       'new' for constructor)
			 * @param methodName     The method name (empty for constructors)
			 * @param arguments      List of arguments in the invocation
			 * @param lineNumber     Line number of the invocation
			 * @param cu             The CompilationUnit to get line information
			 */
			private void processInvocation(String invocationType, String identifier, String methodName,
					List<?> arguments, int lineNumber, CompilationUnit cu) {
				// Initialize lists to hold the parameters and their types
				ArrayList<String> params = new ArrayList<>();
				ArrayList<String> types = new ArrayList<>();

				// Iterate over the arguments and resolve their types
				for (Object arg : arguments) {
					Expression expression = (Expression) arg;
					ITypeBinding typeBinding = expression.resolveTypeBinding();
					if (typeBinding != null) {
						params.add(expression.toString());
						types.add(typeBinding.getName());
					}
				}

				// Get the number of arguments
				int noOfArgs = arguments.size();

				// Print the details of the invocation
				System.out.printf("Line Number: %d %s: %s%s(%s)%n", lineNumber, invocationType, identifier,
						methodName.isEmpty() ? "" : "." + methodName, String.join(",", params));
				System.out.printf("%s Signature: %s:%d:%s%n", invocationType,
						methodName.isEmpty() ? identifier : methodName, noOfArgs, String.join(",", types));
				System.out.println();
			}
		};

		cu.accept(tree);
	}
}
