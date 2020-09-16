package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;

public abstract class ColumnType<ValueT> {
    public abstract Cmp cmp(ValueT x, ValueT y);
    public abstract Object load(SeekableByteChannel in);
    public abstract void store(Object it, SeekableByteChannel out);
}