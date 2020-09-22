package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.ListType;

import java.util.List;

public class ListCol<ItemT> extends Col<List<ItemT>> {
    public ListCol(Table table, String name, Class listClass, Type<ItemT> itemType) {
        super(table, new ListType<>(listClass, itemType), name);
    }
}
