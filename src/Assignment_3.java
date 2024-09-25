import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class Assignment_3 {
	public Assignment_3() throws IOException {
		String relativePath = "test/ContentReader.java";
		Path path = Paths.get(relativePath);
		String content = Files.readString(path);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor tree = new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration declaration) {
				int line = cu.getLineNumber(declaration.getStartPosition());
				String modifier = getModifiersString(declaration.getModifiers());
				String returnType = declaration.getReturnType2() != null ? declaration.getReturnType2().toString() + " "
						: "";
				System.out.println("Line: " + line + " Method Declaration: " + modifier + " " + returnType
						+ declaration.getName());
				declaration.accept(new ASTVisitor() {
					Map<String, List<String>> variableMethods = new HashMap<>();

					@Override
					public boolean visit(MethodDeclaration node) {
						variableMethods.clear();
						return true;
					}

					@Override
					public boolean visit(VariableDeclarationFragment node) {
						String variableName = node.getName().getIdentifier();
						variableMethods.put(variableName, new ArrayList<>());
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

							List<String> methodsCalled = variableMethods.getOrDefault(variableName, new ArrayList<>());

							System.out.printf("Line: %d Method call: %s.%s()%n", lineNumber, variableName, methodName);
							System.out.printf("Methods called on %s: %s%n%n", variableName, methodsCalled);

							methodsCalled.add(methodName);
							variableMethods.put(variableName, methodsCalled);
						}
						return true;
					}
				});
				System.out.println("______________________________");
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
		};

		cu.accept(tree);
	}

}
