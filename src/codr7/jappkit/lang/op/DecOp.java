package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;
import codr7.jappkit.type.LongType;

public class DecOp extends Op {
    public int stackOffs = 0;
    public long delta = 1;

    public DecOp(Target target) { super(target); }

    public DecOp stackOffs(int it) {
        stackOffs = it;
        return this;
    }

    public DecOp delta(long it) {
        delta = it;
        return this;
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        Val it = stack.peek(stackOffs);
        it.data = it.as(LongType.it) - delta;
        return pc+1;
    }
}