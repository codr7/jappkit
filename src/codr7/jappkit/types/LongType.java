package codr7.jappkit.types;

import codr7.jappkit.Type;
import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Encoding;

import java.nio.channels.SeekableByteChannel;

public class LongType extends Type<Long> {
    public LongType() { super("Long"); }

    @Override
    public Long init() { return 0L; }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readLong(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeLong(((Long)it).longValue(), out); }

    @Override
    public Cmp cmp(Long x, Long y) { return Cmp.valueOf(x.compareTo(y)); }
}