package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class CpOp extends Op {
    public int offs = 0;

    public CpOp(Target target) {
        super(target);
        target.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) {
        stack.push(stack.peek(offs).cp());
        return pc+1;
    }
}