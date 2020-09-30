package codr7.jappkit.db.test;

import codr7.jappkit.db.*;
import codr7.jappkit.db.column.LongCol;
import codr7.jappkit.db.column.StringCol;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.*;

public class IndexTest {
    @Test
    public void store() {
        var scm = new Schema(Path.of("testdb"));
        var tbl = new Table(scm, "table");
        var col1 = new LongCol(tbl, "long");
        var col2 = new StringCol(tbl, "string");
        var idx = new Index(scm, "index", col1, col2);
        tbl.addIndex(idx);

        scm.drop();
        scm.open(Instant.now());

        var r = new Rec().init(tbl).set(col1, 42L).set(col2, "foo");
        Object[]k = new Object[]{42L, "foo"};

        var tx = new Tx();
        tbl.store(r, tx);
        assertEquals(idx.find(k, tx), r.get(tbl.id));
        idx.records(tx).forEach((i) -> assertEquals(i.getValue(), r.get(tbl.id)));
        tx.commit();

        scm.close();
        scm.open(Instant.now());
        assertEquals(idx.find(k, tx), r.get(tbl.id));
        idx.records(tx).forEach((i) -> assertEquals(i.getValue(), r.get(tbl.id)));

        scm.close();
    }

    @Test
    public void remove() {
        var scm = new Schema(Path.of("testdb"));
        var tbl = new Table(scm, "table");
        var col = new LongCol(tbl, "column");
        var idx = new Index(scm, "index", col);
        tbl.addIndex(idx);

        scm.drop();
        scm.open(Instant.now());
        var tx = new Tx();

        var r = new Rec().init(tbl);
        tbl.store(r.set(col, 21L), tx);
        tx.commit();
        tbl.store(r.set(col, 42L), tx);

        assertNull(idx.find(new Object[]{21L}, tx));
        assertEquals(idx.find(new Object[]{42L}, tx), r.get(tbl.id));

        tx.commit();
        scm.close();
        scm.open(Instant.now());

        assertNull(idx.find(new Object[]{21L}, tx));
        assertEquals(idx.find(new Object[]{42L}, tx), r.get(tbl.id));
        scm.close();
    }

    public void findFirst() {
        var scm = new Schema(Path.of("testdb"));
        var tbl = new Table(scm, "table");
        var col1 = new LongCol(tbl, "long");
        var col2 = new StringCol(tbl, "string");
        var idx = new Index(scm, "index", col1, col2);
        tbl.addIndex(idx);

        scm.drop();
        scm.open(Instant.now());

        var r1 = new Rec().init(tbl).set(col1, 21L).set(col2, "foo");
        var r2 = new Rec().init(tbl).set(col1, 21L).set(col2, "bar");
        var r3 = new Rec().init(tbl).set(col1, 42L).set(col2, "baz");

        var tx = new Tx();
        tbl.store(r1, tx);
        tbl.store(r2, tx);
        tbl.store(r3, tx);

        assertEquals(idx.findFirst(new Object[]{42L, "foo"}, tx).iterator().next().getValue(), r2.get(tbl.id));
        tx.commit();
        assertEquals(idx.findFirst(new Object[]{21L, "baz"}, tx).iterator().next().getValue(), r3.get(tbl.id));

        scm.close();
    }
}