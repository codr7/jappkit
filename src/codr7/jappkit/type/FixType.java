package codr7.jappkit.type;

import codr7.jappkit.E;
import codr7.jappkit.Fix;
import codr7.jappkit.Val;

public class FixType extends LongType {
    public static FixType it = new FixType("Fix");
    public FixType(String name) { super(name); }

    @Override
    public void add(Val src, Val dst) {
        if (src.type == FixType.it) {
            super.mul(src, dst);
            dst.data = (Long)dst.data / Fix.SCALE;
        } else if (src.type == LongType.it) {
            dst.data = (Long) dst.data + Fix.make((Long) src.data);
        } else {
            throw new E("Add not supported: %s", src.type.name);
        }
    }

    @Override
    public void mul(Val src, Val dst) {
        if (src.type == FixType.it) {
            super.mul(src, dst);
            dst.data = (Long)dst.data / Fix.SCALE;
        } else if (src.type == LongType.it) {
            dst.data = (Long) dst.data * (Long) src.data;
        } else {
            throw new E("Mul not supported: %s", src.type.name);
        }
    }
}