package parser;

import context.Context;
import grammar.Grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestParser {
    public static void main(String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar5"));
        Context context = new Context(new File("loopProg"));
        Parser parser = new Parser(context, grammar);
        parser.parse();
        parser.writeOperations();
    }
}
