package codr7.jappkit.type;

import codr7.jappkit.Type;
import codr7.jappkit.Val;

public abstract class NumType<ValT> extends Type<ValT> {
    public NumType(String name) { super(name); }
    public abstract void add(Val src, Val dst);
    public abstract void mul(Val src, Val dst);
}