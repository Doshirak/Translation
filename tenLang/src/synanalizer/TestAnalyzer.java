package synanalizer;

import context.Context;
import grammar.Grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestAnalyzer {

    public static void main(String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar5"));
        Context context = new Context(new File("loopProg"));
        SynAnalyzer analyzer = new SynAnalyzer(context, grammar);
        analyzer.read();
        analyzer.write();
    }
}
