package codr7.jappkit.lang;

import codr7.jappkit.Stack;

public abstract class Op {
    public final int pc;

    public Op(Target target) {
        this.pc = target.nops();
        target.emit(this);
    }

    public abstract int eval(VM vm, CallStack calls, Stack stack);
}
