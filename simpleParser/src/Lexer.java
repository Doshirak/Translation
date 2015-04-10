import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lexer {
    private Buffer buffer;

    public Lexer (InputStreamReader reader) {

        buffer = new Buffer(reader);
    }

    public boolean isDigit () {
        return Character.isDigit(buffer.read());
    }

    public void skip() {
        buffer.next();
    }
    public int read() {
        return buffer.read();
    }
}
