package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;
import codr7.jappkit.type.LongType;

public class BranchLtOp extends BranchOp {
    public BranchLtOp(VM vm) { super(vm); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        return (stack.peek(stackOffs, LongType.it) < cond) ? targetPc : pc+1;
    }
}
