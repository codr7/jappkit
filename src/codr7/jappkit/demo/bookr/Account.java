package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;

public class Account extends Mod {
    public static Make<Account> make(DB db) { return (rec) -> { return new Account(db, rec); }; }

    public String name;
    public long balance;

    public Account(DB db, Rec rec) { super(db.account, rec); }
    public Account(DB db) { super(db.account); }
}
