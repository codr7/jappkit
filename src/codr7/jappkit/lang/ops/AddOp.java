package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;
import codr7.jappkit.types.LongType;

public class AddOp extends Op {
    public AddOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        Val src = stack.pop(), dst = stack.peek();
        dst.data = dst.as(LongType.it) + src.as(LongType.it);
        return pc+1;
    }
}