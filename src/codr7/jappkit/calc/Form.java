package codr7.jappkit.calc;

import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;

public abstract class Form {
    public abstract void compile(VM vm, Env env);
}