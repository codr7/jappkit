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
        StringColumn col = new StringColumn(tbl, "string");

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record();
        r.set(col, "foo");
        Tx tx = new Tx();
        tbl.store(r, tx);
        assertEquals(r.get(tbl.id), Long.valueOf(1));

        Record lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertEquals(lr.get(col), "foo");

        tx.commit();
        lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertEquals(lr.get(col), "foo");

        scm.close();
    }
}