import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

public class Assignment_4 {

	public Assignment_4() throws IOException {
		String relativePath = "test/UI.java";
		Path path = Paths.get(relativePath);
		String content = Files.readString(path);
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration methodDeclaration) {
				System.out.println("Line: " + cu.getLineNumber(methodDeclaration.getStartPosition())
						+ " Method Declaration: " + methodDeclaration.getName());
				System.out.println("------------------------------------------");
				HashMap<String, String[]> map = new HashMap<>();
				methodDeclaration.accept(new ASTVisitor() {

					@Override
					public boolean visit(MethodInvocation methodInvocation) {
						for (Object arg : methodInvocation.arguments()) {
							Expression expression = (Expression) arg;
							if (expression instanceof SimpleName) {
								insertData(map, arg.toString(), methodInvocation.getName().toString());
							} else if (expression instanceof MethodInvocation) {
								MethodInvocation methodInvocationArg = (MethodInvocation) expression;
								Expression baseExpression = methodInvocationArg.getExpression();
								SimpleName methodName = methodInvocationArg.getName();
								insertData(map, baseExpression.toString(), methodInvocation.getName().toString());
								// We should further calculate for arguments for here as well
							} else {
								continue;
							}
						}
						System.out.println(
								"Line: " + cu.getLineNumber(methodInvocation.getStartPosition()) + " Method Call: "
										+ methodInvocation.getExpression() + "." + methodInvocation.getName() + "()");
						System.out.println(
								"Methods that use " + methodInvocation.getExpression() + " in their arguments: "
										+ Arrays.toString(map.get(methodInvocation.getExpression().toString())) + "\n");

						return super.visit(methodInvocation);
					}

				});
				return super.visit(methodDeclaration);
			}

		};

		cu.accept(tree);
	}

	private static void insertData(HashMap<String, String[]> map, String key, String simpleName) {
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
