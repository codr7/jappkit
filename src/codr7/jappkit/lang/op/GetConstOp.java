package codr7.jappkit.lang.op;

import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;

public class GetConstOp extends Op {
    public String key = null;

    public GetConstOp(Target target) { super(target); }

    public GetConstOp key(String it) {
        key = it;
        return this;
    }

    public void compile(VM vm, Env env, Target out) { new PushOp(out).val(env.get(key)); }
}