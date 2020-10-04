package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.type.NumType;

public class MulOp extends Op {
    public MulOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        Val src = stack.pop(), dst = stack.peek();
        NumType<?> t = (NumType)dst.type;
        t.mul(src, dst);
        return pc+1;
    }
}