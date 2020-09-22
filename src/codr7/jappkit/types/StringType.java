package codr7.jappkit.types;

import codr7.jappkit.Type;
import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Encoding;

import java.nio.channels.SeekableByteChannel;

public class StringType extends Type<String> {
    public static StringType it = new StringType("String");

    public StringType(String name) { super(name); }

    @Override
    public String init() { return ""; }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readString(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeString((String)it, out); }

    @Override
    public Cmp cmp(String x, String y) { return Cmp.valueOf(x.compareTo(y)); }
}