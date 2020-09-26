package codr7.jappkit.db.column;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.type.MapType;

import java.util.Map;

public class MapCol<KeyT, ValueT> extends Col<Map<KeyT, ValueT>> {
    public MapCol(Table table, String name, Class MapClass, Type<KeyT> keyType, Type<ValueT> valueType) {
        super(table, new MapType<>(MapClass, keyType, valueType), name);
    }
}