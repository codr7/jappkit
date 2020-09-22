package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.types.LongType;

public abstract class BranchOp extends Op {
    public long cond = 0;
    public int offs = 0;
    public int targetPc = -1;

    public BranchOp(Target target) { super(target); }

    public BranchOp cond(long it) {
        cond = it;
        return this;
    }

    public BranchOp offs(int it) {
        offs = it;
        return this;
    }

    public void targetPc(Target it) { targetPc = it.nops(); }
}