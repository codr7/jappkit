package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;
import codr7.jappkit.types.LongType;

public class DecOp extends Op {
    public int stackOffs = 0;
    public long delta = 1;

    public DecOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        Val it = stack.peek(stackOffs);
        it.data = it.as(LongType.it) - delta;
        return pc+1;
    }
}