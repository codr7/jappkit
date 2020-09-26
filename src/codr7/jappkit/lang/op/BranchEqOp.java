package codr7.jappkit.lang.op;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.type.LongType;

public class BranchEqOp extends BranchOp {
    public BranchEqOp(Target target) { super(target); }

    @Override
    public int eval(VM vm, CallStack calls, Stack stack) {
        return (stack.peek(stackOffs, LongType.it) == cond) ? targetPc : pc+1;
    }
}