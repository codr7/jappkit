package codr7.jappkit.demo.bookr;

import codr7.jappkit.Time;
import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;

import java.time.Instant;

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

    public void setResource(Resource resource, Tx tx) {
        this.resource = db.itemResource.ref(resource);
        this.product = db.itemProduct.ref(resource.product);
    }

    @Override
    public void store(Tx tx) {
        var pir = table.load(id, tx);
        boolean updateQuantity = true;

        if (pir != null) {
            var pi = new Item(db, pir);
            updateQuantity = pi.resource.id != resource.id || !pi.start.equals(start) || !pi.end.equals(end) || pi.quantity != quantity;
            if (updateQuantity) { Quantity.update(db, pi.resource(tx), pi.start, pi.end, 0, -pi.quantity, tx); }
        }

        if (updateQuantity && quantity != 0L) { Quantity.update(db, resource(tx), start, end, 0, quantity, tx); }
        super.store(tx);
    }
}