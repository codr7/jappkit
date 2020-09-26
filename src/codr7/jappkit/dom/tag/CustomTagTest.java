package codr7.jappkit.dom.tag;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.testng.Assert.*;

public class CustomTagTest {
    @Test
    public void empty() {
        OutputStream buf = new ByteArrayOutputStream();
        new CustomTag("foo").set("bar", 42).set("baz").write(new PrintStream(buf));
        assertEquals(buf.toString(), "<foo bar=\"42\" baz/>\n");
    }

}