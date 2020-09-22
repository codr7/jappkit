package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class StopOp extends Op {
    public StopOp(Target target) {
        super(target);
        target.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) { return -1; }
}