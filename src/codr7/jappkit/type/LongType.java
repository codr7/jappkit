package codr7.jappkit.type;

import codr7.jappkit.Cmp;
import codr7.jappkit.E;
import codr7.jappkit.Fix;
import codr7.jappkit.Val;
import codr7.jappkit.db.Encoding;

import java.nio.channels.SeekableByteChannel;

public class LongType extends NumType<Long> {
    public static LongType it = new LongType("Long");

    public LongType(String name) { super(name); }

    @Override
    public Long init() { return 0L; }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readLong(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeLong(((Long)it).longValue(), out); }

    @Override
    public void add(Val src, Val dst) {
        if (src.type == FixType.it) {
            dst.type = FixType.it;
            dst.data = Fix.make((Long) dst.data) + (Long) src.data;
        } else if (src.type == LongType.it) {
            dst.data = (Long) dst.data + (Long) src.data;
        } else {
            throw new E("Add not supported: %s", src.type.name);
        }
    }

    @Override
    public void mul(Val src, Val dst) {
        if (src.type == FixType.it) {
            dst.type = FixType.it;
            dst.data = (Long) dst.data * (Long) src.data;
        } else if (src.type == LongType.it) {
            dst.data = (Long) dst.data * (Long) src.data;
        } else {
            throw new E("Mul not supported: %s", src.type.name);
        }
    }

    @Override
    public Cmp cmp(Long x, Long y) { return Cmp.valueOf(x.compareTo(y)); }
}