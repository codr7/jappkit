package codr7.jappkit.db;

import codr7.jappkit.db.columns.ListColumn;
import codr7.jappkit.db.columns.LongColumn;
import codr7.jappkit.db.columns.MapColumn;
import codr7.jappkit.db.columns.StringColumn;
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
        assertEquals(tbl.records(tx).count(), 1);
        tbl.records(tx).forEach((Record x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        tx.commit();
        lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(tbl.id), Long.valueOf(1));
        assertEquals(lr.get(col), "foo");
        assertEquals(tbl.records(tx).count(), 1);
        tbl.records(tx).forEach((x) -> assertEquals(x.get(tbl.id), r.get(tbl.id)));

        scm.close();
    }

    @Test
    public void list() {
        Schema scm = new Schema(Path.of("testdb"));
        Table tbl = new Table(scm, "table_list");
        ListColumn<Long> col = new ListColumn<>(tbl, "string", ArrayList.class, LongColumn.type);

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record();
        List<Long> v = col.init();
        v.add(42L);
        r.set(col, v);
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
        Table tbl = new Table(scm, "table_map");
        MapColumn<Long, String> col = new MapColumn<>(tbl, "map", HashMap.class, LongColumn.type, StringColumn.type);

        scm.drop();
        scm.open(Instant.now());

        Record r = new Record();
        Map<Long, String> v = col.init();
        v.put(42L, "foo");
        r.set(col, v);
        Tx tx = new Tx();
        tbl.store(r, tx);
        tx.commit();

        Record lr = tbl.load(r.get(tbl.id), tx);
        assertEquals(lr.get(col).get(42L), "foo");
        scm.close();
    }
}