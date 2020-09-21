package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;

public interface ColumnType<ValueT> {
    ValueT init();
    default ValueT clone(ValueT it) { return it; }
    default ValueT get(ValueT it) { return it; }
    default ValueT set(ValueT it) { return it; }
    Object load(SeekableByteChannel in);
    void store(Object it, SeekableByteChannel out);
    Cmp cmp(ValueT x, ValueT y);
}