import com.sun.istack.internal.logging.Logger;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Test extends TestCase {

    public InputStreamReader getReader(String string) {
        InputStream stream = new ByteArrayInputStream(string.getBytes());
        return new InputStreamReader(stream);
    }

    public int parse(String expression) throws Parser.ParserException {
        InputStream stream = new ByteArrayInputStream(expression.getBytes());
        InputStreamReader reader = new InputStreamReader(stream);
        Parser parser = new Parser(reader);
        return parser.parse();
    }

    @Override
    public void setUp() {
    }

    @Override
    public void tearDown() {

    }

    public void testInt() {
        try {
            assertEquals(parse("2"), 2);
            assertEquals(parse("10000"), 10000);
        } catch (Parser.ParserException e) {
            fail();
        }
        boolean exception = false;
        try {
            parse("abc");
        } catch (Parser.ParserException e) {
            exception = true;
        }
        assertTrue(exception);
    }

    public void testSum() {
        try {
            assertEquals(parse("2+2"), 4);
        } catch (Parser.ParserException e) {
            fail();
        }
    }

    public void testSub() {
        try {
            assertEquals(parse("4-2"), 2);
            assertEquals(parse("2-4"), -2);
        } catch (Parser.ParserException e) {
            fail();
        }
    }

    public void testMul() {
        try {
            assertEquals(parse("1*2"), 2);
            assertEquals(parse("-1*2"), -2);
        } catch (Parser.ParserException e) {
            fail();
        }
    }

    public void testExp() {
        try {
            assertEquals(parse("2^2"), 4);
            assertEquals(parse("2^2^2"), 16);
            assertEquals(parse("2^-1"), 1);
        } catch (Parser.ParserException e) {
            fail();
        }
    }

    public void testNegative() {
        try {
            assertEquals(parse("-1"), -1);
        } catch (Parser.ParserException e) {
            fail();
        }
    }

    public void testExpression() {
        try {
            assertEquals(parse("4-(1+1)"), 2);
            assertEquals(parse("2*(1+1)"), 4);
            assertEquals(parse("2^(1+1)"), 4);
        } catch (Parser.ParserException e) {
            fail();
        }
    }
}
