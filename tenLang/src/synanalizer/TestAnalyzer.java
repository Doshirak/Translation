package synanalizer;

import errorhandler.ErrorHandler;
import grammar.Grammar;
import reader.Position;

import java.io.File;
import java.io.FileNotFoundException;

public class TestAnalyzer {

    public static void main(String args[]) throws FileNotFoundException {
        ErrorHandler errorHandler = new ErrorHandler();
        Position position = new Position();
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar"));
        SynAnalyzer analyzer = new SynAnalyzer("var a : int program: a := 2 ; print a", errorHandler, position, grammar);
        analyzer.read();
        analyzer.write();
    }
}
