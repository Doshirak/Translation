package reader;

import errorhandler.ErrorHandler;

import java.io.InputStreamReader;

public class Buffer {
    private Position position;
    private ErrorHandler errorHandler;
    private Reader reader;
    private int prev;
    private int current;
    public Buffer (InputStreamReader reader, ErrorHandler errorHandler, Position position) {
        this.errorHandler = errorHandler;
        this.position = position;
        this.reader = new Reader(reader, errorHandler, position);
        next();
    }
    public int readPrev() {
        return  prev;
    }
    public int read () {
        return current;
    }
    public void next () {
        prev = current;
        current = reader.read();
    }
    public boolean empty() {
        return current == -1;
    }
}
