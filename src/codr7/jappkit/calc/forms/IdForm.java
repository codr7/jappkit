package codr7.jappkit.calc.forms;

import codr7.jappkit.E;
import codr7.jappkit.calc.Form;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.op.PushOp;

public class IdForm extends Form {
    public final String id;

    public IdForm(String id) { this.id = id; }

    @Override
    public void compile(VM vm, Env env) {
        var v = env.get(id);
        if (v == null) { throw new E("Unknown identifier: %s", id); }
        new PushOp(vm).val(v);
    }

    public String toString() { return "Id(" + this.id + ")"; }
}
