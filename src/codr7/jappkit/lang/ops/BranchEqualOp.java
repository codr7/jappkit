package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.types.LongType;

public class BranchEqualOp extends BranchOp {
    public BranchEqualOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        return (stack.peek(offs, LongType.it) == cond) ? targetPc : pc+1;
    }
}