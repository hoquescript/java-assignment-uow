import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Assignment {
	public static void main(String[] args) throws IOException {
		// Reading source file
		String directory = "test";
		String file = "LogWriter.java";
		Path path = Paths.get(directory + "/" + file);
		String content = Files.readString(path);

		// Parses Java source code from content into an AST
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		// Configuring parser options
		Map<String, String> options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
		parser.setUnitName(file);
		String[] sources = { Paths.get("src").toString() };
		parser.setEnvironment(null, sources, new String[] { "UTF-8" }, true);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		System.out.println("Question-1");
		System.out.println("---------------------------------------------------");
		new Assignment_1(cu);

		System.out.println("Question-2");
		System.out.println("---------------------------------------------------");
		new Assignment_2(cu);

		System.out.println("Question-3");
		System.out.println("---------------------------------------------------");
		new Assignment_3(cu);
		
		System.out.println("Question-4");
		System.out.println("---------------------------------------------------");
		new Assignment_4(cu);
	}

}
