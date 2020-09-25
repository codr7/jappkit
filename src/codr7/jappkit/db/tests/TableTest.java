package codr7.jappkit.db.tests;

import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Schema;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.Tx;
import codr7.jappkit.db.columns.*;
import codr7.jappkit.types.LongType;
import codr7.jappkit.types.StringType;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.testng.Assert.*;

public class TableTest {
    @Test
    public void store() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        BooleanCol booleanCol = new BooleanCol(tbl, "boolean");
        StringCol stringCol = new StringCol(tbl, "string");
        TimeCol timeCol = new TimeCol(tbl, "time");

        scm.drop();
        scm.open(Instant.now());

        Rec r = new Rec().init(tbl).set(booleanCol, true).set(stringCol, "foo").set(timeCol, Instant.now());
        Tx tx = new Tx();
        tbl.store(r, tx);
        assertEquals(r.get(tbl.id), Long.valueOf(1));

        Rec lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertTrue(lr.get(booleanCol));
        assertEquals(lr.get(stringCol), "foo");
        assertEquals(lr.get(timeCol), r.get(timeCol));
        assertEquals(tbl.recs(tx).count(), 1);
        tbl.recs(tx).forEach((Rec x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        tx.commit();
        lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertTrue(lr.get(booleanCol));
        assertEquals(lr.get(stringCol), "foo");
        assertEquals(lr.get(timeCol), r.get(timeCol));
        assertEquals(tbl.recs(tx).count(), 1);
        tbl.recs(tx).forEach((x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        scm.close();
    }

    @Test
    public void delete() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        LongCol col = new LongCol(tbl, "column");

        scm.drop();
        scm.open(Instant.now());
        Tx tx = new Tx();

        Rec r1 = new Rec().init(tbl).set(col, 21L);
        tbl.store(r1, tx);
        Rec r2 = new Rec().init(tbl).set(col, 42L);
        tbl.store(r2, tx);
        tbl.delete(r1.get(tbl.id), tx);
        tbl.recs(tx).forEach((Rec x) -> assertEquals(x.get(col), r2.get(col)));

        tx.commit();
        scm.close();
        scm.open(Instant.now());

        tbl.recs(tx).forEach((x) -> assertEquals(x.get(col), r2.get(col)));

        scm.close();
    }

    @Test
    public void list() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        ListCol<Long> col = new ListCol<>(tbl, "column", ArrayList.class, LongType.it);

        scm.drop();
        scm.open(Instant.now());

        Rec r = new Rec().init(tbl);
        r.get(col).add(42L);
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Rec lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).size(), 1);
        assertEquals(lr.get(col).iterator().next().longValue(), 42);
        scm.close();
    }

    @Test
    public void set() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        SetCol<Long> col = new SetCol<>(tbl, "column", HashSet.class, LongType.it);

        scm.drop();
        scm.open(Instant.now());

        Rec r = new Rec().init(tbl);
        r.get(col).add(42L);
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Rec lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).size(), 1);
        assertEquals(lr.get(col).iterator().next().longValue(), 42);
        scm.close();
    }

    @Test
    public void map() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");

        MapCol<Long, String> col =
                new MapCol<>(tbl, "column", HashMap.class, LongType.it, StringType.it);

        scm.drop();
        scm.open(Instant.now());

        Rec r = new Rec().init(tbl);
        r.get(col).put(42L, "foo");
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Rec lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).size(), 1);
        assertEquals(lr.get(col).get(42L), "foo");
        scm.close();
    }
}