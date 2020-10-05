package codr7.jappkit.calc;

import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.op.StopOp;
import codr7.jappkit.type.NumType;

public class Calc extends VM {
    public Calc set(String key, Val val) {
        env.set(key, val);
        return this;
    }

    public <ValT> Calc set(String key, NumType<ValT> type, ValT data) { return set(key, Val.make(type, data)); }

    public Val eval(Reader in) {
        clear();
        var startPc = nops();
        in.read().compile(this, env);
        new StopOp(this);
        var stack = new Stack();
        eval(new CallStack(), stack, startPc);
        return stack.pop();
    }

    private Env env = new Env();
}
