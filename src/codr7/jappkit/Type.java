package codr7.jappkit;

import codr7.jappkit.db.Cmp;

import java.nio.channels.SeekableByteChannel;

public abstract class Type<ValueT> {
    public final String name;

    public Type(String name) { this.name = name; }
    public abstract ValueT init();
    public ValueT clone(ValueT it) { return it; }
    public ValueT get(ValueT it) { return it; }
    public ValueT set(ValueT it) { return it; }
    public abstract Object load(SeekableByteChannel in);
    public abstract void store(Object it, SeekableByteChannel out);
    public abstract Cmp cmp(ValueT x, ValueT y);
}