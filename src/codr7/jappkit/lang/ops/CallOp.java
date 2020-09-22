package codr7.jappkit.lang.ops;

import codr7.jappkit.Stack;
import codr7.jappkit.lang.*;

public class CallOp extends Op {
    public int targetPc;

    public CallOp(Target target, int targetPc) {
        super(target);
        this.targetPc = targetPc;
        target.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) {
        calls.push(pc+1);
        return targetPc;
    }
}