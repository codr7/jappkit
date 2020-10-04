package codr7.jappkit.type;

import codr7.jappkit.Fix;
import codr7.jappkit.Val;

public class FixType extends LongType {
    public static FixType it = new FixType("Fix");
    public FixType(String name) { super(name); }

    @Override
    public void mul(Val src, Val dst) {
        super.mul(src, dst);
        dst.data = (Long)dst.data / Fix.M;
    }
}