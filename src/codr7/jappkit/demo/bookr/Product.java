package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Rec;

public class Product extends Mod {
    public static Make<Product> make(DB db) { return (rec) -> { return new Product(db, rec); }; }

    public String name;

    public Product(DB db, Rec rec) { super(db.product, rec); }

    public Product(DB db) { super(db.product); }
}
