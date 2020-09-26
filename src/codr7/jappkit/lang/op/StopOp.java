package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class StopOp extends Op {
    public StopOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) { return -1; }
}