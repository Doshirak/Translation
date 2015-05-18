package reader;

import context.Context;
import errorhandler.*;
import errorhandler.Error;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
    private InputStreamReader reader;
    private Position position;
    private ErrorHandler errorHandler;
    private Context context;

    public Reader(InputStreamReader reader, ErrorHandler errorHandler, Position position) {
        this.reader = reader;
        this.errorHandler = errorHandler;
        this.position = position;
    }

    public Reader(Context context) {
        this.reader = context.getReader();
        this.position = context.getPosition();
        this.errorHandler = context.getErrorHandler();
    }

    public int read() {
        try {
            int c = reader.read();
            if ((char) c == '\n') {
                position.newLine();
            } else {
                position.next();
            }
            return c;
        } catch (Exception e) {
            errorHandler.addError(new Error("read error", new Position(position)));
            return -1;
        }
    }
}
