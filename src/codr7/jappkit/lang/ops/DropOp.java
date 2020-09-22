package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;

public class DropOp extends Op {
    public int offs = 0, nitems = 1;

    public DropOp(Target target) { super(target); }

    public DropOp offs(int it) {
        offs = it;
        return this;
    }

    public DropOp nitems(int it) {
        nitems = it;
        return this;
    }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        stack.drop(offs, nitems);
        return pc+1;
    }
}
