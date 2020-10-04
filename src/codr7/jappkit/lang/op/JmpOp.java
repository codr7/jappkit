package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.VM;

public class JmpOp extends Op {
    public int targetPc = -1;

    public JmpOp(VM vm) { super(vm); }

    public JmpOp targetPc(int it) {
        targetPc = it;
        return this;
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) { return targetPc; }
}
