package codr7.jappkit.db;

import codr7.jappkit.db.columns.LongColumn;
import codr7.jappkit.db.columns.StringColumn;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.*;

public class IndexTest {
    @Test
    public void store() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "index_store");
        LongColumn col1 = new LongColumn(tbl, "long");
        StringColumn col2 = new StringColumn(tbl, "string");
        Index idx = new Index(scm, "index_store");
        tbl.addIndex(idx);
        idx.addColumn(col1).addColumn(col2);

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record().set(col1, 42L).set(col2, "foo");
        Object[]k = new Object[]{42L, "foo"};

        Tx tx = new Tx();
        tbl.store(r, tx);
        assertEquals(idx.find(k, tx), r.get(tbl.id));
        tx.commit();

        scm.close();
        scm.open(Instant.now());
        assertEquals(idx.find(k, tx), r.get(tbl.id));

        scm.close();
    }
}