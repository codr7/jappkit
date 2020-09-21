package codr7.jappkit.db.tests;

import codr7.jappkit.db.Record;
import codr7.jappkit.db.Schema;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.Tx;
import codr7.jappkit.db.columns.*;
import org.testng.annotations.*;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class TableTest {
    @Test
    public void store() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        StringColumn stringCol = new StringColumn(tbl, "string");
        TimeColumn timeCol = new TimeColumn(tbl, "time");

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record().init(tbl).set(stringCol, "foo").set(timeCol, Instant.now());
        Tx tx = new Tx();
        tbl.store(r, tx);
        assertEquals(r.get(tbl.id), Long.valueOf(1));

        Record lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertEquals(lr.get(stringCol), r.get(stringCol));
        assertEquals(lr.get(timeCol), r.get(timeCol));
        assertEquals(tbl.records(tx).count(), 1);
        tbl.records(tx).forEach((Record x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        tx.commit();
        lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertEquals(lr.get(stringCol), r.get(stringCol));
        assertEquals(lr.get(timeCol), r.get(timeCol));
        assertEquals(tbl.records(tx).count(), 1);
        tbl.records(tx).forEach((x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        scm.close();
    }

    @Test
    public void delete() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        LongColumn col = new LongColumn(tbl, "column");

        scm.drop();
        scm.open(Instant.now());
        Tx tx = new Tx();

        Record r1 = new Record().init(tbl).set(col, 21L);
        tbl.store(r1, tx);
        Record r2 = new Record().init(tbl).set(col, 42L);
        tbl.store(r2, tx);
        tbl.delete(r1.get(tbl.id), tx);
        tbl.records(tx).forEach((Record x) -> assertEquals(x.get(col), r2.get(col)));

        tx.commit();
        scm.close();
        scm.open(Instant.now());

        tbl.records(tx).forEach((x) -> assertEquals(x.get(col), r2.get(col)));

        scm.close();
    }

    @Test
    public void list() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");
        ListColumn<Long> col = new ListColumn<>(tbl, "column", ArrayList.class, LongColumn.type);

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record().init(tbl);
        r.get(col).add(42L);
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Record lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).iterator().next().longValue(), 42);
        scm.close();
    }

    @Test
    public void map() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table");

        MapColumn<Long, String> col =
                new MapColumn<>(tbl, "column", HashMap.class, LongColumn.type, StringColumn.type);

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record().init(tbl);
        r.get(col).put(42L, "foo");
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Record lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).get(42L), "foo");
        scm.close();
    }
}