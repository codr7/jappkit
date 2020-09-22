package codr7.jappkit.types;

import codr7.jappkit.Type;
import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Encoding;

import java.nio.channels.SeekableByteChannel;

public class BooleanType extends Type<Boolean> {
    public static BooleanType it = new BooleanType("Boolean");

    public BooleanType(String name) { super(name); }

    @Override
    public Boolean init() { return false; }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readBoolean(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeBoolean(((Boolean)it).booleanValue(), out); }

    @Override
    public Cmp cmp(Boolean x, Boolean y) { return Cmp.valueOf(x.compareTo(y)); }
}
