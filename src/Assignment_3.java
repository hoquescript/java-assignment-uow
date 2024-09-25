import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class Assignment_3 {

	public Assignment_3(CompilationUnit cu) {
		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodDeclaration declaration) {
				System.out.println("###");
				int line = cu.getLineNumber(declaration.getStartPosition());
				String modifier = getModifiersString(declaration.getModifiers());
				String returnType = declaration.getReturnType2() != null ? declaration.getReturnType2().toString() + " "
						: "";
				String method = declaration.getName().toString();
				System.out.printf("Line: %d Method Declaration: %s %s%s%n", line, modifier, returnType, method);

				declaration.accept(new ASTVisitor() {
					Map<String, List<String>> variableMethods = new HashMap<>();

					@Override
					public boolean visit(MethodDeclaration node) {
						// Clear the map for method calls associated with each new method declaration
						variableMethods.clear();
						return true;
					}

					@Override
					public boolean visit(VariableDeclarationFragment node) {
						// Store the newly declared variable in the map with an empty list of method
						// calls
						String variableName = node.getName().getIdentifier();
						variableMethods.put(variableName, new ArrayList<>());
						// If the variable is initialized with a class instance, record the constructor
						// name
						if (node.getInitializer() instanceof ClassInstanceCreation) {
							ClassInstanceCreation constructor = (ClassInstanceCreation) node.getInitializer();
							variableMethods.get(variableName).add(constructor.getType().toString());
						}
						return true;
					}

					@Override
					public boolean visit(MethodInvocation node) {
						Expression expression = node.getExpression();
						if (expression instanceof SimpleName) {
							String variableName = ((SimpleName) expression).getIdentifier();
							String methodName = node.getName().getIdentifier();
							int lineNumber = cu.getLineNumber(node.getStartPosition());

							// Fetch the current list of methods called on this variable, default to empty
							// list
							List<String> methodsCalled = variableMethods.getOrDefault(variableName, new ArrayList<>());

							System.out.printf("Line: %d Method call: %s.%s()%n", lineNumber, variableName, methodName);
							System.out.printf("Methods called on %s: %s%n%n", variableName, methodsCalled);

							// Record the current method invocation for future reference
							methodsCalled.add(methodName);
							variableMethods.put(variableName, methodsCalled);
						}
						return true;
					}
				});
				return true;
			}
		};
		cu.accept(tree);
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
		if (Modifier.isSynchronized(modifiers))
			modifierString.append("synchronized ");
		if (Modifier.isVolatile(modifiers))
			modifierString.append("volatile ");
		if (Modifier.isTransient(modifiers))
			modifierString.append("transient ");
		if (Modifier.isNative(modifiers))
			modifierString.append("native ");
		return modifierString.length() > 0 ? modifierString.toString().trim() : "";
	}
}
