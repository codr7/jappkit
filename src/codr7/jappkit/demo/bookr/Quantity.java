package codr7.jappkit.demo.bookr;

import codr7.jappkit.E;
import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;

import java.time.Instant;
import java.util.Comparator;
import java.util.stream.Stream;

public class Quantity extends Mod {
    public static Make<Quantity> make(DB db) { return (rec) -> new Quantity(db, rec); }

    public static Stream<Quantity> stream(DB db, Resource resource, Instant start, Instant end, Tx tx) {
        return db.quantityIndex
                .findFirst(new Object[]{db.quantityResource.ref(resource), start.plusSeconds(1)}, tx)
                .takeWhile((e) -> db.quantityIndex.key(e.getKey(), db.quantityStart).compareTo(end) < 0)
                .map((e) -> { return new Quantity(db, db.quantity.load(e.getValue(), tx)); });
    }

    public static long get(DB db, Resource resource, Instant start, Instant end, Tx tx) {
        return stream(db, resource, start, end, tx)
                .map((q) -> q.total-q.used)
                .min(Comparator.naturalOrder())
                .orElse(0L);
    }

    public static void update(DB db, Resource resource, Instant start, Instant end, long total, long used, Tx tx) {
        stream(db, resource, start, end, tx).forEach((q) -> {
            if (q.start.compareTo(start) < 0) {
                new Quantity(db, resource, q.start, start, q.total, q.used).store(tx);
                q.start = start;
            }

            if (q.end.compareTo(end) > 0) {
                new Quantity(db, resource, end, q.end, q.total, q.used).store(tx);
                q.end = end;
            }

            q.total += total;
            q.used += used;

            if (q.used > q.total) { throw new E("Overbook"); }
            q.store(tx);
        });
    }

    public Instant start, end;
    public long total, used;

    public Quantity(DB db, Rec rec) { super(db.quantity, rec); }

    public Quantity(DB db, Resource resource, Instant start, Instant end, long total, long used) {
        super(db.quantity);
        this.resource = db.quantityResource.ref(resource);
        this.start = start;
        this.end = end;
        this.total = total;
        this.used = 0;
    }

    public Resource resource(Tx tx) { return resource.deref(tx); }

    private Ref<Resource> resource;
}