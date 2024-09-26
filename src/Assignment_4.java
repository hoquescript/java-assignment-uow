import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

public class Assignment_4 {
	public Assignment_4(CompilationUnit cu) {
		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration methodDeclaration) {
				System.out.println("###");
				System.out.printf("Line: %d Method Declaration: %s%n",
						cu.getLineNumber(methodDeclaration.getStartPosition()), methodDeclaration.getName());

				Map<String, String[]> map = new HashMap<>();
				methodDeclaration.accept(new ASTVisitor() {

					@Override
					public boolean visit(MethodInvocation methodInvocation) {
						// Iterate through each argument of the method invocation
						for (Object arg : methodInvocation.arguments()) {
							Expression expression = (Expression) arg;
							if (expression instanceof SimpleName) {
								// If the argument is a simple name (like a variable), add it to the map
								insertData(map, arg.toString(), methodInvocation.getName().toString());
							} else if (expression instanceof MethodInvocation) {
								// If the argument is another method invocation, handle it accordingly
								MethodInvocation methodInvocationArg = (MethodInvocation) expression;
								Expression baseExpression = methodInvocationArg.getExpression();
								insertData(map, baseExpression.toString(), methodInvocation.getName().toString());
							} else {
								// Skip if the argument is neither SimpleName nor MethodInvocation
								continue;
							}
						}

						int line = cu.getLineNumber(methodInvocation.getStartPosition());
						String identifier = methodInvocation.getExpression().toString();
						String methodName = methodInvocation.getName().toString();
						String methodCollection = Arrays.toString(map.get(identifier));

						System.out.printf("Line: %d Method Call: %s.%s()%n", line, identifier, methodName);
						System.out.printf("Methods that use %s in their arguments: %s%n", identifier, methodCollection);

						return super.visit(methodInvocation);
					}

				});
				return super.visit(methodDeclaration);
			}

		};

		cu.accept(tree);
	}

	private static void insertData(Map<String, String[]> map, String key, String simpleName) {
		if (map.containsKey(key)) {
			// If the key exists, get the existing array
			String[] existingArray = map.get(key);
			// Create a new array with an extra slot
			String[] newArray = Arrays.copyOf(existingArray, existingArray.length + 1);
			// Add the new value to the last position
			newArray[newArray.length - 1] = simpleName;
			// Update the map with the new array
			map.put(key, newArray);
		} else {
			// If the key does not exist, create a new array with the value
			map.put(key, new String[] { simpleName });
		}
	}
}
