package codr7.jappkit.db;

import codr7.jappkit.db.column.RefCol;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.time.Instant;

import static org.testng.Assert.assertEquals;

public class ModTest {
    private static class DB extends Schema{
        public final Table account;
        public final Table charge;
        public final RefCol<Account> from;
        public final RefCol<Account> to;

        public DB(Path root) {
            super(root);

            account = new Table(this, "account");
            charge = new Table(this, "charge");
            from = new RefCol<>(charge, "from", account, Account.make(this));
            to = new RefCol<>(charge, "to", account, Account.make(this));
        }
    }

    private static class Account extends Mod {
        public static final Make<Account> make(DB db) { return (rec) -> new Account(db, rec); }

        public Account(DB db, Rec rec) { super(db.account, rec); }
        public Account(DB db) { super(db.account); }
    }

    private static class Charge extends Mod {
        public static final Make<Charge> make(DB db) { return (rec) -> new Charge(db, rec); }

        public Charge(DB db, Rec rec) { super(db.charge, rec); }

        public Charge(DB db, Account from, Account to) {
            super(db.charge);
            this.from = db.from.ref(from);
            this.to = db.to.ref(to);
        }

        public Account from(Tx tx) { return from.deref(tx); }
        public Account to(Tx tx) { return to.deref(tx); }

        private Ref<Account> from;
        private Ref<Account> to;
    }

    @Test
    public void store() {
        DB db = new DB(Path.of("testdb"));
        db.drop();
        db.open(Instant.now());
        Tx tx = new Tx();

        Account a1 = new Account(db);
        a1.store(tx);
        Account a2 = new Account(db);
        a2.store(tx);
        Charge c = new Charge(db, a1, a2);
        c.store(tx);
        tx.commit();

        assertEquals(c.from(tx).id, a1.id);
        assertEquals(c.to(tx).id, a2.id);
        db.close();
    }
}