import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reader {
    private InputStreamReader reader;
    public Reader (InputStreamReader reader) {
        this.reader = reader;
    }
    public int read () {
        try {
            return  reader.read();
        } catch (IOException e) {
            return -1;
        }
    }
}
