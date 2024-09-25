import java.util.ArrayList;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

public class Assignment_2 {
	public Assignment_2(CompilationUnit cu){
		ASTVisitor tree = new ASTVisitor() {
			@Override
			public boolean visit(MethodInvocation node) {
                // Initialize lists to hold the parameters and their types
				ArrayList<String> params = new ArrayList<>();
				ArrayList<String> types = new ArrayList<>();
				for (Object arg : node.arguments()) {
					Expression expression = (Expression) arg;
					ITypeBinding typeBinding = expression.resolveTypeBinding();
					// If the type binding is not null, store the expression and its type
					if (typeBinding != null) {
						params.add(expression.toString());
						types.add(typeBinding.getName());
					}
				}

				int line = cu.getLineNumber(node.getStartPosition());
				int noOfArgs = node.arguments().size();
				String identifier = node.getExpression().toString();
				String methodCall = node.getName().getIdentifier();

				System.out.printf("Line Number: %d Method Call: %s.%s(%s)%n", line, identifier, methodCall,
						String.join(",", params));
				System.out.printf("Method Signature: %s:%d:%s%n", methodCall, noOfArgs, String.join(",", types));
				System.out.println();

				return super.visit(node);
			}
		};

		cu.accept(tree);
	}
}
