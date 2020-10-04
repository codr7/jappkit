package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class RetOp extends Op {
    public RetOp(VM vm) { super(vm); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) { return calls.pop(); }
}