package lexer;

import errorhandler.*;
import errorhandler.Error;
import lexer.Lexer;
import reader.Position;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestLexer {

    public static void main (String args[]) {
        ErrorHandler errorHandler = new ErrorHandler();
        try {
            Position position = new Position();
            if (args.length > 0) {
                String filename = args[0];
                String array[] = filename.split("\\.");
                if (array.length == 0) {
                    errorHandler.addError(new Error("need file extension", null));
                    System.exit(1);
                }
                String name = array[0].trim();
                String extension = array[1].trim();
                if (!extension.equals("ten")) {
                    errorHandler.addError(new Error("wrong file extension", null));
                    System.exit(1);
                }
                InputStream inputStream = new FileInputStream(filename);
                InputStreamReader reader = new InputStreamReader(inputStream);
                Lexer lexer = new Lexer(reader, errorHandler, position);
                lexer.read();
                lexer.write(name + ".lex");
            } else {
                Lexer lexer = new Lexer("// comment\nint a / ;", errorHandler, position);
                lexer.read();
                lexer.write();
            }
        } catch (IOException e) {
            errorHandler.addError(new Error("input error", null));
        }
    }
}
