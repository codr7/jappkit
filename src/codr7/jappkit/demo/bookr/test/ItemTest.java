package codr7.jappkit.demo.bookr.test;

import codr7.jappkit.Fix;
import codr7.jappkit.Time;
import codr7.jappkit.db.Tx;
import codr7.jappkit.demo.bookr.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.*;

public class ItemTest {
    @Test
    public void quantity() {
        DB db = new DB(Path.of("testdb"));
        db.drop();
        db.open(Instant.now());
        Tx tx = new Tx();

        Resource r = new Resource(db);
        r.name = "foo";
        r.quantity = 10;
        r.store(tx);

        Item i = new Item(db);
        i.resource = db.itemResource.ref(r);
        i.setLength(3*Time.DAY);
        i.quantity = 3;
        i.store(tx);

        Assert.assertEquals(Quantity.get(db, r, Instant.MIN, i.start, tx), 10L);
        assertEquals(Quantity.get(db, r, i.start, i.end, tx), 7L);
        assertEquals(Quantity.get(db, r, i.end, Instant.MAX, tx), 10L);

        i.quantity = 9L;
        i.setLength(9*Time.DAY);
        i.store(tx);
        tx.commit();

        assertEquals(Quantity.get(db, r, Instant.MIN, i.start, tx), 10L);
        assertEquals(Quantity.get(db, r, i.start, i.end, tx), 1L);
        assertEquals(Quantity.get(db, r, i.end, Instant.MAX, tx), 10L);

        db.close();
    }

    @Test
    public void charge() {
        var db = new DB(Path.of("testdb"));
        db.drop();
        db.open(Instant.now());
        var tx = new Tx();

        var p = new Product(db);
        p.name = "foo";
        p.store(tx);

        var a1 = new Account(db);
        a1.name = "a1";
        a1.store(tx);

        var a2 = new Account(db);
        a2.name = "a2";
        a2.store(tx);

        var cr = new ChargeRule(db);
        cr.setProduct(p);
        cr.setFrom(a1);
        cr.setTo(a2);
        cr.body = "? * 0.25";
        cr.store(tx);

        var r = new Resource(db);
        r.name = "foo";
        r.quantity = 10;
        r.setProduct(p);
        r.store(tx);

        var i = new Item(db);
        i.setResource(r, tx);
        i.setLength(2 * Time.DAY);
        i.quantity = 2;
        i.price = Fix.make(1000);
        i.store(tx);

        assertEquals(i.product.id, p.id);
        assertEquals(i.charges(tx).count(), 1);
        var c = i.charges(tx).iterator().next();
        assertEquals(c.product.id, p.id);
        assertEquals(c.from.id, a1.id);
        assertEquals(c.to.id, a2.id);
        assertEquals(c.amount, Fix.make(250));

        i.price = Fix.make(500);
        i.store(tx);
        tx.commit();
        i.reload(tx);

        assertEquals(i.charges(tx).count(), 1);
        c = i.charges(tx).iterator().next();
        assertEquals(c.amount, Fix.make(125));

        db.close();
    }
}