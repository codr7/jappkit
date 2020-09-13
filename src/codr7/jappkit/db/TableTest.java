package codr7.jappkit.db;

import org.testng.annotations.*;

import java.time.Instant;

import static org.testng.Assert.*;

public class TableTest {
    @Test
    public void store() {
        Schema scm = new Schema("testdb");
        Table tbl = new Table(scm, "table_store");
        scm.drop();
        scm.open(Instant.now(););
        assertEquals(42, 42);
        scm.close();
    }
}