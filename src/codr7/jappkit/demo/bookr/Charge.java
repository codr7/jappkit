package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;

public class Charge extends Mod {
    public static Make<Charge> make(DB db) { return (rec) -> new Charge(db, rec); }

    public DB db;
    public Ref<Product> product;
    public Ref<Account> from;
    public Ref<Account> to;
    public long amount;

    public Charge(DB db, Rec rec) {
        super(db.charge, rec);
        this.db = db;
    }

    public Charge(DB db, Product product, Account from, Account to, long amount) {
        super(db.charge);
        this.db = db;
        this.product = db.chargeProduct.ref(product);
        this.from = db.chargeFrom.ref(from);
        this.to = db.chargeTo.ref(to);
        this.amount = amount;
    }

    public Product product(Tx tx) { return product.deref(tx); }

    public Account from(Tx tx) { return from.deref(tx); }

    public Account to(Tx tx) { return to.deref(tx); }
}