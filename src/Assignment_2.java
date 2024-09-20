import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.SimpleName;

public class Assignment_2 {
	public Assignment_2() throws IOException {
		String relativePath = "src/ContentReader.java";
        Path path = Paths.get(relativePath);
        String content = Files.readString(path);
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
				// Iterate over arguments
//                for (Object arg : node.arguments()) {
//                    Expression expression = (Expression) arg;
//                    ITypeBinding typeBinding = expression.resolveTypeBinding();
//                    System.out.println(expression + " " + typeBinding);
//                    if (typeBinding != null) {
//                        System.out.println("Argument type: " + typeBinding.getQualifiedName());
//                    } else {
//                        System.out.println("Unable to resolve argument type");
//                    }
//                };
                System.out.println(node.typeArguments());
//                node.typeArguments();
//				System.out.println("Line: " + cu.getLineNumber(node.getStartPosition()) + " Method Declaration: "
//						+ node.getExpression().toString() + "." + node.getName().getIdentifier()+"()");
//				System.out.println("Method Signature: " + node.getName() + ":" + node.arguments().size() + ":"  );
				return super.visit(node);
			}
		};

		cu.accept(tree);
	}
}
