import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Buffer {
    private Reader reader;
    private int current;
    public Buffer (InputStreamReader reader) {
        this.reader = new Reader(reader);
        next();
    }
    public int read () {
        return current;
    }
    public void next () {
        current = reader.read();
    }
}
