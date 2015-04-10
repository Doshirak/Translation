package reader;

import errorhandler.*;
import errorhandler.Error;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
    private Position position;
    private ErrorHandler errorHandler;
    private InputStreamReader reader;

    public Reader (InputStreamReader reader, ErrorHandler errorHandler, Position position) {
        this.reader = reader;
        this.errorHandler = errorHandler;
        this.position = position;
    }

    public int read () {
        try {
            int c = reader.read();
            if ((char)c == '\n') {
                position.newLine();
            } else {
                position.next();
            }
            return c;
        } catch (IOException e) {
            errorHandler.addError(new Error("", new Position(position)));
            return -1;
        }
    }
}
