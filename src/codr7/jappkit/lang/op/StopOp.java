package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class StopOp extends Op {
    public StopOp(VM vm) { super(vm); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) { return -1; }
}