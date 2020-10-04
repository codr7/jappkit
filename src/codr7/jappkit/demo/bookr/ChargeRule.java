package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Tx;

import java.time.Instant;

public class ChargeRule extends Mod {
    public static Make<ChargeRule> make(DB db) { return (rec) -> new ChargeRule(db, rec); }

    public DB db;
    public Ref<Product> product;
    public Instant start;
    public Instant end;
    public Ref<Account> from;
    public Ref<Account> to;
    public String body;

    public ChargeRule(DB db, Rec rec) {
        super(db.chargeRule, rec);
        this.db = db;
    }

    public ChargeRule(DB db) {
        super(db.chargeRule);
        this.db = db;
        start = Instant.MIN;
        end = Instant.MAX;
    }

    public Product product(Tx tx) { return product.deref(tx); }

    public ChargeRule setProduct(Product it) {
        product = db.chargeRuleProduct.ref(it);
        return this;
    }

    public Account from(Tx tx) { return from.deref(tx); }

    public ChargeRule setFrom(Account it) {
        from = db.chargeRuleFrom.ref(it);
        return this;
    }

    public Account to(Tx tx) { return to.deref(tx); }

    public ChargeRule setTo(Account it) {
        to = db.chargeRuleFrom.ref(it);
        return this;
    }
}
