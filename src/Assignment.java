import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Assignment {
	public static void main(String[] args) throws IOException {
		// Reading source file
		String relativePath = "test/ContentReader.java";
		Path path = Paths.get(relativePath);
		String content = Files.readString(path);

		// Parses Java source code from content into an AST
		ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		new Assignment_1(cu);

		// new Assignment_2();
		// new Assignment_3();
		// new Assignment_4();
	}

}
