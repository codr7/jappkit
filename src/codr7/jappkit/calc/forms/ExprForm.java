package codr7.jappkit.calc.forms;

import codr7.jappkit.E;
import codr7.jappkit.calc.Form;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.op.AddOp;
import codr7.jappkit.lang.op.MulOp;

public class ExprForm extends Form {
    public enum Op {
        Add(2), Mul(1);
        final int prio;
        Op(int prio) { this.prio = prio; }
    };

    public ExprForm(String opName, Form left, Form right) {
        Op op;

        switch(opName) {
            case "+":
                op = Op.Add;
                break;
            case "*":
                op = Op.Mul;
                break;
            default:
                throw new E("Invalid op: %s", opName);
        }

        this.op = op;
        this.left = left;
        this.right = right;

        if (ExprForm.class.isAssignableFrom(left.getClass())) {
            var le = (ExprForm)left;

            if (op.prio < le.op.prio) {
                this.op = le.op;
                le.op = op;
                this.right = le.left;
                le.left = le.right;
                le.right = right;
            }
        }
    }

    @Override
    public void compile(VM vm, Env env) {
        left.compile(vm, env);
        right.compile(vm, env);

        switch (op) {
            case Add:
                new AddOp(vm);
                break;
            case Mul:
                new MulOp(vm);
                break;
            default:
                throw new E("Unhandled operator: " + op);
        }
    }

    public String toString() { return op + "(" + left.toString() + " " + right.toString() + ")"; }

    private Op op;
    private Form left, right;
}

