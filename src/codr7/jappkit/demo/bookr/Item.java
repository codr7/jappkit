package codr7.jappkit.demo.bookr;

import codr7.jappkit.Fix;
import codr7.jappkit.Time;
import codr7.jappkit.calc.Calc;
import codr7.jappkit.calc.Reader;
import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;
import codr7.jappkit.db.test.ModTest;
import codr7.jappkit.type.FixType;
import codr7.jappkit.type.LongType;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Stream;

import static org.testng.Assert.assertEquals;

public class Item extends Mod {
    public static Make<Item> make(DB db) { return (rec) -> new Item(db, rec); }

    public final DB db;
    public Ref<Resource> resource;
    public Ref<Product> product;
    public Instant start, end;
    public long quantity;
    public long price;

    public Item(DB db, Rec rec) {
        super(db.item, rec);
        this.db = db;
    }

    public Item(DB db) {
        super(db.item);
        this.db = db;
        start = Time.today();
        end = start;
        quantity = 1;
    }

    public Item setLength(long it) {
        end = start.plusSeconds(it);
        return this;
    }

    public Resource resource(Tx tx) { return resource.deref(tx); }

    public void setResource(Resource it, Tx tx) {
        this.resource = db.itemResource.ref(it);
        this.product = db.itemProduct.ref(it.product);
    }

    public Product product(Tx tx) { return product.deref(tx); }

    public void setProduct(Product it) { this.product = db.itemProduct.ref(it); }

    public Charge charge(Account from, Account to, long amount, Tx tx) {
        var c = new Charge(db, product(tx), from, to, amount);
        c.store(tx);
        charges.add(new Ref<Charge>(db.charge, c.id, Charge.make(db)));
        return c;
    }

    public Stream<Charge> charges(Tx tx) { return charges.stream().map((c) -> c.deref(tx)); }

    @Override
    public void store(Tx tx) {
        var pir = table.load(id, tx);
        var updateQuantity = true;
        var updateCharge = true;

        if (pir != null) {
            var pi = new Item(db, pir);
            updateQuantity = pi.resource.id != resource.id || !pi.start.equals(start) || !pi.end.equals(end) || pi.quantity != quantity;
            if (updateQuantity) { Quantity.update(db, pi.resource(tx), pi.start, pi.end, 0, -pi.quantity, tx); }
            updateCharge = pi.product.id != product.id || pi.price != price;

            if (updateCharge) {
                for (var c : charges) { c.deref(tx).delete(tx); }
                charges.clear();
            }
        }

        if (updateQuantity && quantity != 0) { Quantity.update(db, resource(tx), start, end, 0, quantity, tx); }

        if (product.id != -1 && price != 0 && updateCharge) {
            var today = Time.today();

            db.chargeRuleIndex
                    .findFirst(new Object[]{db.chargeRuleProduct.ref(product(tx)), today.plusSeconds(1)}, tx)
                    .takeWhile((e) -> db.chargeRuleIndex.key(e.getKey(), db.chargeRuleStart).compareTo(today) < 0)
                    .map((e) -> { return new ChargeRule(db, db.chargeRule.load(e.getValue(), tx)); })
                    .forEach((cr) -> {
                        var c = new Calc();
                        c.set("?", FixType.it, price);
                        var a = (Long)c.eval(new Reader(cr.body)).data;
                        charge(cr.from(tx), cr.to(tx), a, tx);
                    });
        }

        super.store(tx);
    }

    private Set<Ref<Charge>> charges;
}