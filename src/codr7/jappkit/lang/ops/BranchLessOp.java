package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;
import codr7.jappkit.types.LongType;

public class BranchLessOp extends Op {
    public long cond;
    public int offs = 0;
    public int targetPc = -1;

    public BranchLessOp(Target target, long cond) {
        super(target);
        this.cond = cond;
        target.emit(this);
    }

    public void mark(Target it) { targetPc = it.nops(); }

    public int eval(VM vm, CallStack calls, Stack stack) {
        return (stack.peek(offs, LongType.it) < cond) ? targetPc : pc+1;
    }
}
