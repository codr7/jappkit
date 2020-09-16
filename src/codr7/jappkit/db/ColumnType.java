package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;

public interface ColumnType<ValueT> {
    ValueT init();
    Object load(SeekableByteChannel in);
    void store(Object it, SeekableByteChannel out);
    Cmp cmp(ValueT x, ValueT y);
}