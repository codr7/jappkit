package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class SwapOp extends Op {
    public int offs1 = 1, offs2 = 0;

    public SwapOp(Target target) {
        super(target);
        target.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) {
        stack.peek(offs1).swap(stack.peek(offs2));
        return pc+1;
    }
}