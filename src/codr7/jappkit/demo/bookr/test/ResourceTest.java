package codr7.jappkit.demo.bookr.test;

import codr7.jappkit.db.Tx;
import codr7.jappkit.demo.bookr.DB;
import codr7.jappkit.demo.bookr.Quantity;
import codr7.jappkit.demo.bookr.Resource;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.*;

public class ResourceTest {
    @Test
    public void quantity() {
        DB db = new DB(Path.of("testdb"));
        db.drop();
        db.open(Instant.now());
        Tx tx = new Tx();

        Resource r = new Resource(db);
        r.name = "foo";
        r.quantity = 10L;
        r.store(tx);
        assertEquals(Quantity.get(db, r, Instant.MIN, Instant.MAX, tx), 10L);

        r.quantity = 5L;
        r.store(tx);
        tx.commit();
        assertEquals(Quantity.get(db, r, Instant.MIN, Instant.MAX, tx), 5L);
        db.close();
    }
}