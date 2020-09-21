package codr7.jappkit.db.tests;

import codr7.jappkit.db.*;
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
        tbl.addIndex(idx.addColumn(col1).addColumn(col2));

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record().init(tbl).set(col1, 42L).set(col2, "foo");
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

    public void findFirst() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "index_find_first");
        LongColumn col1 = new LongColumn(tbl, "long");
        StringColumn col2 = new StringColumn(tbl, "string");
        Index idx = new Index(scm, "index_store");
        tbl.addIndex(idx.addColumn(col1).addColumn(col2));

        scm.drop();
        scm.open(Instant.now());

        Record r1 = new Record().init(tbl).set(col1, 21L).set(col2, "foo");
        Record r2 = new Record().init(tbl).set(col1, 21L).set(col2, "bar");
        Record r3 = new Record().init(tbl).set(col1, 42L).set(col2, "baz");

        Tx tx = new Tx();
        tbl.store(r1, tx);
        tbl.store(r2, tx);
        tbl.store(r3, tx);

        assertEquals(idx.findFirst(new Object[]{42L, "foo"}, tx).iterator().next().getValue(), r2.get(tbl.id));
        tx.commit();
        assertEquals(idx.findFirst(new Object[]{21L, "baz"}, tx).iterator().next().getValue(), r3.get(tbl.id));

        scm.close();
    }
}