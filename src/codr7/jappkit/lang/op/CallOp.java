package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class CallOp extends Op {
    public int targetPc = -1;

    public CallOp(VM vm) { super(vm); }

    public CallOp targetPc(int it) {
        targetPc = it;
        return this;
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        calls.push(pc+1);
        return targetPc;
    }
}