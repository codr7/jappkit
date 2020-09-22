package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;
import codr7.jappkit.types.LongType;

public class BranchLessOp extends BranchOp {
    public BranchLessOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        return (stack.peek(offs, LongType.it) < cond) ? targetPc : pc+1;
    }
}
