package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.Type;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;

public class PushOp extends Op {
    public Val val = null;

    public PushOp(Target target) { super(target); }

    public PushOp val(Val it) {
        val = it;
        return this;
    }

    public <ValT> PushOp val(Type<ValT> type, ValT data) {
        return val(Val.make(type, data));
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        stack.push(val.clone());
        return pc+1;
    }
}
