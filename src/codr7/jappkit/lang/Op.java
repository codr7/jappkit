package codr7.jappkit.lang;

public abstract class Op {
    public final int pc;

    public Op(Target target) { this.pc = target.nops(); }

    public abstract int eval(VM vm, Stack stack);
}
