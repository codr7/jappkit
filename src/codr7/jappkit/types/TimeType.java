package codr7.jappkit.types;

import codr7.jappkit.db.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.db.Encoding;

import java.nio.channels.SeekableByteChannel;
import java.time.Instant;

public class TimeType extends Type<Instant> {
    public TimeType() { super("Time"); }

    @Override
    public Instant init() { return Instant.ofEpochMilli(0L); }

    @Override
    public Instant set(Instant it) { return Instant.ofEpochMilli(it.toEpochMilli()); }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readTime(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeTime((Instant)it, out); }

    @Override
    public Cmp cmp(Instant x, Instant y) { return Cmp.valueOf(x.compareTo(y)); }
};