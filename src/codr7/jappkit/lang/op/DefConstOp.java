package codr7.jappkit.lang.op;

import codr7.jappkit.Val;
import codr7.jappkit.lang.*;

public class DefConstOp extends Op {
    public String key = null;
    public Val val = null;

    public DefConstOp(Target target) { super(target); }

    public DefConstOp key(String it) {
        key = it;
        return this;
    }

    public DefConstOp val(Val it) {
        val = it;
        return this;
    }

    public void compile(VM vm, Env env, Target out) { env.set(key, val); }
}