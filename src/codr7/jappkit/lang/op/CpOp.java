package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class CpOp extends Op {
    public int offs = 0;

    public CpOp(Target target) { super(target); }

    public CpOp offs(int it) {
        offs = it;
        return this;
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        stack.push(stack.peek(offs).cp());
        return pc+1;
    }
}