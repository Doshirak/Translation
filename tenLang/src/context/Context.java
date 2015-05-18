package context;

import errorhandler.*;
import reader.Position;

import java.io.*;

public class Context {
    private InputStreamReader reader;
    private Position position = new Position();
    private ErrorHandler errorHandler = new ErrorHandler();

    public Context(InputStreamReader reader) {
        this.reader = reader;
    }

    public Context(String string) {
        this.reader = new InputStreamReader(new ByteArrayInputStream(string.getBytes()));
    }

    public Context(File file) {
        try {
            this.reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            errorHandler.addError("can't open file");
        }
    }

    public Context() {
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public Position getPosition() {
        return position;
    }

    public InputStreamReader getReader() {
        return reader;
    }

}
