import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

/**
 * Created by sergey on 02.05.15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        CharStream input = new ANTLRFileStream("input.txt");
        PrintStream out = new PrintStream(new FileOutputStream("output.asm"));

        GrammarLexer lex = new GrammarLexer(input);
        TokenStream tokens = new CommonTokenStream(lex);
        GrammarParser parser = new GrammarParser(tokens);

        ParseTree tree = parser.programm();

        FunctionVisitor fv = new FunctionVisitor();
        fv.visit(tree);

        Visitor visitor = new Visitor();
        visitor.setFunctionStorage(fv.getFuncStorage());
        //ParseTreeWalker treeWalker = new ParseTreeWalker();
        //заимплементить ParseTreeListener
        //ParseTreeListener listener = new ParseTreeListener(parser);
        //treeWalker.walk(listener, tree);

        //List<String> code = new Visitor().visit(tree).getValue();
        List<String> code = visitor.visit(tree).getValue();
        for (String line : code) {
            out.println(line);
            System.out.println(line);
        }
    }
}
