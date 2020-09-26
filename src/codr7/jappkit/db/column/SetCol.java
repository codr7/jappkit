package codr7.jappkit.db.column;

import codr7.jappkit.Type;
import codr7.jappkit.db.Col;
import codr7.jappkit.db.Table;
import codr7.jappkit.type.SetType;

import java.util.Set;

public class SetCol<ItemT> extends Col<Set<ItemT>> {
    public SetCol(Table table, String name, Class setClass, Type<ItemT> itemType) {
        super(table, new SetType<>(setClass, itemType), name);
    }
}