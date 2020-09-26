package codr7.jappkit.dom.html;

import org.testng.annotations.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.testng.Assert.*;

public class DocTest {
    @Test
    public void empty() {
        Doc d = new Doc("foo");
        d.body.a("http://bar.baz");
        OutputStream buf = new ByteArrayOutputStream();
        d.write(buf);
        assertEquals(buf.toString(), "<html>\n<head>\n<title>\nfoo</title>\n</head>\n<body>\n<a href=\"http://bar.baz\"/>\n</body>\n</html>\n");
    }

}