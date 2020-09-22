package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class RetOp extends Op {
    public RetOp(Target target) {
        super(target);
        target.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) { return calls.pop(); }
}