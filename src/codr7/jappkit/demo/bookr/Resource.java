package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Tx;

import java.time.Instant;

public class Resource extends Mod {
    public static Make<Resource> make(DB db) { return (rec) -> { return new Resource(db, rec); }; }

    public final DB db;
    public String name;
    public long quantity;

    public Resource(DB db, Rec rec) {
        super(db.resource, rec);
        this.db = db;
    }

    public Resource(DB db) {
        super(db.resource);
        this.db = db;
    }

    @Override
    public void store(Tx tx) {
        new Quantity(db, this, Instant.MIN, Instant.MAX).store(tx);
        super.store(tx);
    }
}
