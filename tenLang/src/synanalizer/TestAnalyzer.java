package synanalizer;

import context.Context;
import variables.Variable;
import errorhandler.ErrorHandler;
import grammar.Grammar;
import reader.Position;

import java.io.File;
import java.io.FileNotFoundException;

public class TestAnalyzer {

    public static void main(String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar4"));
        Context context = new Context(new File("loopProg"));
        SynAnalyzer analyzer = new SynAnalyzer(context, grammar);
        analyzer.read();
        analyzer.write();
    }
}
