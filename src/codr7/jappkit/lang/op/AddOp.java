package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.*;
import codr7.jappkit.type.LongType;
import codr7.jappkit.type.NumType;

public class AddOp extends Op {
    public AddOp(VM vm) { super(vm); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        Val src = stack.pop(), dst = stack.peek();
        NumType<?> t = (NumType)dst.type;
        t.add(src, dst);
        return pc+1;
    }
}