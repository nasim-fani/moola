package Compiler;
import gen.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import java.io.IOException;
import java.util.Hashtable;

public class Compiler {
    public static void main(String[] args) throws IOException {
        CharStream stream = CharStreams.fromFileName("./sample/test2.mla");
        MoolaLexer lexer = new MoolaLexer(stream);
        TokenStream tokens = new CommonTokenStream(lexer);
        MoolaParser parser = new MoolaParser(tokens);
        parser.setBuildParseTree(true);
        ParseTree tree = parser.program();
        ParseTreeWalker walker = new ParseTreeWalker();
        Hashtable global = new Hashtable();
        MoolaListener listener = new ProgramPrinter(global);

        walker.walk(listener,tree);
    }
}
