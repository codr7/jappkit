package codr7.jappkit.lang.op;

import codr7.jappkit.lang.Op;
import codr7.jappkit.lang.Target;

public abstract class BranchOp extends Op {
    public long cond = 0;
    public int stackOffs = 0;
    public int targetPc = -1;

    public BranchOp(Target target) { super(target); }

    public BranchOp cond(long it) {
        cond = it;
        return this;
    }

    public BranchOp stackOffs(int it) {
        stackOffs = it;
        return this;
    }

    public void targetPc(int it) { targetPc = it; }

    public void targetPc(Target it) { targetPc(it.nops()); }
}