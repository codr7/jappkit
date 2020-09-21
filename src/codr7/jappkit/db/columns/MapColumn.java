package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.MapType;

import java.util.Map;

public class MapColumn <KeyT, ValueT> extends Column<Map<KeyT, ValueT>> {
    public MapColumn(Table table, String name, Class MapClass, Type<KeyT> keyType, Type<ValueT> valueType) {
        super(table, new MapType<>(MapClass, keyType, valueType), name);
    }
}