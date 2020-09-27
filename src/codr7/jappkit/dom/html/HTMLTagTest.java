package codr7.jappkit.dom.html;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.testng.Assert.*;

public class HTMLTagTest {
    @Test
    public void empty() {
        HTMLTag t = new HTMLTag("html");
        t.appendTag("body").a("http://foo.bar");
        OutputStream buf = new ByteArrayOutputStream();
        t.write(buf);
        assertEquals(buf.toString(), "<html><body><a href=\"http://foo.bar\"/></body></html>");
    }
}