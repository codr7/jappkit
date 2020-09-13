package codr7.jappkit.db;

import codr7.jappkit.db.columns.StringColumn;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.*;

public class TableTest {
    @Test
    public void store() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table_store");
        StringColumn col = new StringColumn("string");
        tbl.addColumn(col);
        scm.drop();
        scm.open(Instant.now());
        assertEquals(42, 42);
        scm.close();
    }
}