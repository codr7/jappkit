package codr7.jappkit.lang;

import codr7.jappkit.Stack;

public abstract class Op {
    public final int pc;

    public Op(VM vm) {
        this.pc = vm.nops();
        vm.emit(this);
    }

    public int eval(VM vm, CallStack calls, Stack stack) { return pc+1; }
}
