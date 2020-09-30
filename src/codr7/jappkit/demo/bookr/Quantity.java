package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;

import java.time.Instant;

public class Quantity extends Mod {
    public static Make<Quantity> make(DB db) { return (rec) -> new Quantity(db, rec); }

    public static long get(DB db, Resource resource, Instant start, Instant end, Tx tx) {
        return db.quantityIndex
                .findFirst(new Object[]{db.quantityResource.ref(resource), start.plusMillis(1)}, tx)
                .map((e) -> { return new Quantity(db, db.quantity.load(e.getValue(), tx)); })
                .takeWhile((q) -> q.end.compareTo(end) < 0)
                .map((q) -> q.total-q.used)
                .min((x, y) -> x.compareTo(y))
                .orElse(0L);
    }

    public Instant start, end;
    public long total, used;

    public Quantity(DB db, Rec rec) { super(db.quantity, rec); }

    public Quantity(DB db, Resource resource, Instant start, Instant end) {
        super(db.quantity);
        this.resource = db.quantityResource.ref(resource);
        this.start = start;
        this.end = end;
        this.total = resource.quantity;
        this.used = 0;
    }

    public Resource resource(Tx tx) { return resource.deref(tx); }

    private Ref<Resource> resource;
}