import org.eclipse.jdt.core.dom.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Claude extends ASTVisitor {
    private CompilationUnit cu;
    private String currentMethodName;
    private Map<String, List<MethodInvocation>> methodCallsMap = new HashMap<>();

    public Claude(CompilationUnit cu) {
        this.cu = cu;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        currentMethodName = node.getName().getIdentifier();
        methodCallsMap.put(currentMethodName, new ArrayList<>());
        return true;
    }

    @Override
    public boolean visit(MethodInvocation node) {
        if (currentMethodName != null) {
            methodCallsMap.get(currentMethodName).add(node);
        }
        return true;
    }

    public void analyze() {
        for (Map.Entry<String, List<MethodInvocation>> entry : methodCallsMap.entrySet()) {
            String methodName = entry.getKey();
            List<MethodInvocation> calls = entry.getValue();
            
            System.out.println("Analyzing method: " + methodName);
            for (MethodInvocation call : calls) {
                analyzeMethodCall(methodName, call);
            }
            System.out.println();
        }
    }

    private void analyzeMethodCall(String methodName, MethodInvocation call) {
        int lineNumber = cu.getLineNumber(call.getStartPosition());
        String receiver = getReceiver(call);
        String calledMethodName = call.getName().getIdentifier();

        System.out.println("Line: " + lineNumber);
        System.out.println("Method Declaration: " + methodName);
        System.out.println("Line: " + lineNumber + " Method Call: " + 
                           (receiver.isEmpty() ? "" : receiver + ".") + calledMethodName + "()");

        List<String> priorMethods = findPriorMethodsUsingReceiver(methodName, call, receiver);
        if (!receiver.isEmpty()) {
            System.out.println("Methods that use " + receiver + " in their arguments before this call: " + priorMethods);
        }
        System.out.println();
    }

    private String getReceiver(MethodInvocation call) {
        if (call.getExpression() instanceof SimpleName) {
            return ((SimpleName) call.getExpression()).getIdentifier();
        }
        return "";
    }

    private List<String> findPriorMethodsUsingReceiver(String methodName, MethodInvocation currentCall, String receiver) {
        List<String> methods = new ArrayList<>();
        if (receiver.isEmpty()) return methods;

        for (MethodInvocation call : methodCallsMap.get(methodName)) {
            if (call.getStartPosition() >= currentCall.getStartPosition()) {
                break;
            }
            if (usesVariableAsArgument(call, receiver)) {
                methods.add(call.getName().getIdentifier());
            }
        }
        return methods;
    }

    private boolean usesVariableAsArgument(MethodInvocation call, String variableName) {
        for (Object arg : call.arguments()) {
            if (arg instanceof SimpleName && ((SimpleName) arg).getIdentifier().equals(variableName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS16);
		String relativePath = "test/UI.java";
        Path path = Paths.get(relativePath);
        String content = Files.readString(path);
        parser.setSource(content.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        Claude analyzer = new Claude(cu);
        cu.accept(analyzer);
        analyzer.analyze();
    }
}