package codr7.jappkit.lang;

import codr7.jappkit.Stack;

public abstract class Op {
    public final int pc;

    public Op(Target target) {
        this.pc = target.nops();
        target.emit(this);
    }

    public void compile(VM vm, Env env, Target out) { out.emit(this); }

    public int eval(VM vm, CallStack calls, Stack stack) { return pc+1; }
}
