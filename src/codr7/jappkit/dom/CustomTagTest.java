package codr7.jappkit.dom;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.testng.Assert.*;

public class CustomTagTest {
    @Test
    public void empty() {
        OutputStream buf = new ByteArrayOutputStream();
        new CustomTag("foo").set("bar", 42).set("baz").write(buf);
        assertEquals(buf.toString(), "<foo bar=\"42\" baz/>");
    }

    @Test
    public void body() {
        OutputStream buf = new ByteArrayOutputStream();
        new CustomTag("foo").append("bar").write(buf, true);
        assertEquals(buf.toString(), "<foo>\n  bar</foo>\n");
    }
}