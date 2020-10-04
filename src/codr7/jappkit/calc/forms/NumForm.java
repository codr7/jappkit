package codr7.jappkit.calc.forms;

import codr7.jappkit.Val;
import codr7.jappkit.calc.Form;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.op.PushOp;

public class NumForm extends Form {
    public final Val val;

    public NumForm(Val val) { this.val = val; }

    @Override
    public void compile(VM vm, Env env) { new PushOp(vm).val(val); }

    public String toString() { return "Num(" + this.val.data + ")"; }
}