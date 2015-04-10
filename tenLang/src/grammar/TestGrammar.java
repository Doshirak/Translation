package grammar;

import java.io.File;
import java.io.FileNotFoundException;

public class TestGrammar {
    public static void main(String args[]) throws FileNotFoundException {
        Grammar grammar = new Grammar();
        grammar.read(new File("grammar"));
    }
}
