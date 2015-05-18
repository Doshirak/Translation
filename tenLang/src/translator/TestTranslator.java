package translator;

import context.Context;
import grammar.Grammar;
import parser.Parser;

import java.io.File;
import java.io.FileNotFoundException;

public class TestTranslator {
    public static void main(String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar3"));
        Context context = new Context(new File("assignProg"));
        Translator translator = new Translator(context, grammar);
        translator.translate();
        translator.write();
    }
}
