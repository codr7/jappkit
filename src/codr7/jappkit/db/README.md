# db

### intro
This package implements a simple log-based, transactional, embedded relational database in Java.
I spent a lot of time over the years building abstractions around SQL, the design described in
this document attacks the problem from the other end and gets rid of SQL and much of the complexity
in the process.

### design and limitations
* Mainly intended for embedded, single process use cases; multi-threaded access is supported.
* Table keys and indexes must be loaded into memory before use, though it's possible to
control loading per table/index; the actual data contained in the table records is loaded 
from disk on request. 
* No query languages included, the database is designed to be accessed from its java API. 

### example: account transfers
The following example implements the bare minimum needed to simulate account transfers.
It contains two tables, one for accounts and one for charges/transfers.

```
    private static class DB extends Schema {
        public final Table account =  new Table(this, "account");
        public final LongCol accountBalance = new LongCol(account, "balance");
        public final Table charge = new Table(this, "charge");
        public final RefCol<Account> chargeFrom = new RefCol<>(charge, "from", account, Account.make(this));
        public final RefCol<Account> chargeTo = new RefCol<>(charge, "to", account, Account.make(this));
        public final LongCol chargeAmount = new LongCol(charge, "amount");

        public DB(Path root) { super(root); }
    }

    private static class Account extends Mod {
        public static final Make<Account> make(DB db) { return (rec) -> { return new Account(db, rec); }; }

        public long balance;

        public Account(DB db, Rec rec) { super(db.account, rec); }
        public Account(DB db) { super(db.account); }
    }

    private static class Charge extends Mod {
        public static final Make<Charge> make(DB db) { return (rec) -> new Charge(db, rec); }

        public Charge(DB db, Rec rec) { super(db.charge, rec); }

        public Charge(DB db, Account from, Account to, long amount) {
            super(db.charge);
            this.from = db.chargeFrom.ref(from);
            this.to = db.chargeTo.ref(to);
            this.amount = amount;
        }

        public Account from(Tx tx) { return from.deref(tx); }
        public Account to(Tx tx) { return to.deref(tx); }

        @Override
        public void store(Tx tx) {
            Account a = from(tx);
            a.balance -= amount;
            a.store(tx);

            a = to(tx);
            a.balance += amount;
            a.store(tx);

            super.store(tx);
        }

        private Ref<Account> from;
        private Ref<Account> to;
        private long amount;
    }
```

And that's it, but before we can actually use the model it needs a root directory.
`open` takes a timestamp argument which allows opening the database at a specific point in time.

```
    @Test
    public void store() {
        DB db = new DB(Path.of("testdb"));
        db.drop();
        db.open(Instant.now());
        Tx tx = new Tx();

        Account a1 = new Account(db);
        a1.balance = 1000L;
        a1.store(tx);

        Account a2 = new Account(db);
        a2.balance = 0L;
        a2.store(tx);

        Charge c = new Charge(db, a1, a2, 300L);
        c.store(tx);
        tx.commit();

        a1.reload(tx);
        assertEquals(a1.balance, 700L);

        a2.reload(tx);
        assertEquals(a2.balance, 300L);

        db.close();
    }
```