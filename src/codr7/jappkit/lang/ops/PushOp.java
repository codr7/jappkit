package codr7.jappkit.lang.ops;

import codr7.jappkit.Type;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;

public class PushOp extends Op {
    public final Val val;

    public PushOp(Target target, Val val) {
        super(target);
        this.val = val;
        target.emit(this);
    }

    public <ValT> PushOp(Target target, Type<ValT> type, ValT val) { this(target, Val.make(type, val)); }

    public int eval(VM vm, Stack stack) {
        stack.push(val.clone());
        return pc + 1;
    }
}
