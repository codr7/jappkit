package codr7.jappkit.demo.bookr;

import codr7.jappkit.db.Index;
import codr7.jappkit.db.Schema;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.column.LongCol;
import codr7.jappkit.db.column.RefCol;
import codr7.jappkit.db.column.StringCol;
import codr7.jappkit.db.column.TimeCol;

import java.nio.file.Path;

public class DB extends Schema {
    public final Table resource =  new Table(this, "resource");
    public final StringCol resourceName = new StringCol(resource, "name");
    public final LongCol resourceQuantity = new LongCol(resource, "quantity");
    public final Index resourceNameIndex = new Index(this, "resource_name", resourceName);

    public final Table quantity =  new Table(this, "quantity");
    public final RefCol<Resource> quantityResource = new RefCol<Resource>(quantity, "resource", resource, Resource.make(this));
    public final TimeCol quantityStart = new TimeCol(quantity, "start");
    public final TimeCol quantityEnd = new TimeCol(quantity, "end");
    public final LongCol quantityTotal = new LongCol(quantity, "total");
    public final LongCol quantityUsed = new LongCol(quantity, "used");
    public final Index quantityIndex = new Index(this, "quantity", quantityResource, quantityStart, quantityEnd);

    public DB(Path root) {
        super(root);
        resource.addIndex(resourceNameIndex);
        quantity.addIndex(quantityIndex);
    }
}