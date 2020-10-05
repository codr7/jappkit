package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Index;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Schema;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.column.*;
import codr7.jappkit.db.type.RefType;

import java.nio.file.Path;
import java.util.TreeSet;

public class DB extends Schema {
    public final Table account =  new Table(this, "account");
    public final StringCol accountName = new StringCol(account, "name");
    public final Index accountNameIndex = new Index(this, "account_name", accountName);

    public final Table product =  new Table(this, "product");
    public final StringCol productName = new StringCol(product, "name");
    public final Index productNameIndex = new Index(this, "product_name", productName);

    public final Table chargeRule = new Table(this, "charge_rule");
    public final RefCol<Product> chargeRuleProduct = new RefCol<>(chargeRule, "product", product, Product.make(this));
    public final TimeCol chargeRuleStart = new TimeCol(chargeRule, "start");
    public final TimeCol chargeRuleEnd = new TimeCol(chargeRule, "end");
    public final RefCol<Account> chargeRuleFrom = new RefCol<>(chargeRule, "from", account, Account.make(this));
    public final RefCol<Account> chargeRuleTo = new RefCol<>(chargeRule, "to", account, Account.make(this));
    public final StringCol chargeRuleBody = new StringCol(chargeRule, "body");
    public final Index chargeRuleIndex = new Index(this, "charge_rule", chargeRuleProduct, chargeRuleEnd, chargeRuleStart);

    public final Table charge = new Table(this, "charge");
    public final RefCol<Product> chargeProduct = new RefCol<>(charge, "product", product, Product.make(this));
    public final RefCol<Account> chargeFrom = new RefCol<>(charge, "from", account, Account.make(this));
    public final RefCol<Account> chargeTo = new RefCol<>(charge, "to", account, Account.make(this));
    public final FixCol chargeAmount = new FixCol(charge, "amount");

    public final Table resource =  new Table(this, "resource");
    public final StringCol resourceName = new StringCol(resource, "name");
    public final LongCol resourceQuantity = new LongCol(resource, "quantity");
    public final RefCol<Product> resourceProduct = new RefCol<Product>(resource, "product", product, Product.make(this));
    public final Index resourceNameIndex = new Index(this, "resource_name", resourceName);

    public final Table quantity =  new Table(this, "quantity");
    public final RefCol<Resource> quantityResource = new RefCol<Resource>(quantity, "resource", resource, Resource.make(this));
    public final TimeCol quantityStart = new TimeCol(quantity, "start");
    public final TimeCol quantityEnd = new TimeCol(quantity, "end");
    public final LongCol quantityTotal = new LongCol(quantity, "total");
    public final LongCol quantityUsed = new LongCol(quantity, "used");
    public final Index quantityIndex = new Index(this, "quantity", quantityResource, quantityEnd, quantityStart);

    public final Table item =  new Table(this, "item");
    public final RefCol<Resource> itemResource = new RefCol<Resource>(item, "resource", resource, Resource.make(this));
    public final RefCol<Product> itemProduct = new RefCol<Product>(item, "product", product, Product.make(this));
    public final TimeCol itemStart = new TimeCol(item, "start");
    public final TimeCol itemEnd = new TimeCol(item, "end");
    public final LongCol itemQuantity = new LongCol(item, "quantity");
    public final LongCol itemPrice = new LongCol(item, "price");
    public final SetCol<Ref<Charge>> itemCharges = new SetCol(item, "charges", TreeSet.class, new RefType<Charge>(charge, Charge.make(this)));

    public DB(Path root) {
        super(root);
        account.addIndex(accountNameIndex);
        product.addIndex(productNameIndex);
        chargeRule.addIndex(chargeRuleIndex);
        resource.addIndex(resourceNameIndex);
        quantity.addIndex(quantityIndex);
    }
}