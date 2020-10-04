package codr7.jappkit.calc;

import codr7.jappkit.E;
import codr7.jappkit.Stack;
import codr7.jappkit.Val;
import codr7.jappkit.calc.forms.ExprForm;
import codr7.jappkit.calc.forms.IdForm;
import codr7.jappkit.calc.forms.NumForm;
import codr7.jappkit.lang.CallStack;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.op.StopOp;
import codr7.jappkit.type.LongType;
import codr7.jappkit.type.NumType;

import java.io.*;
import java.util.ArrayList;

public class Calc extends VM {
    public Calc set(String key, Val val) {
        env.set(key, val);
        return this;
    }

    public <ValT> Calc set(String key, NumType<ValT> type, ValT data) { return set(key, Val.make(type, data)); }

    public Val eval(String it) {
        return eval(new PushbackInputStream(new ByteArrayInputStream(it.getBytes())));
    }

    public Form read(PushbackInputStream in) {
        Form l = readForm(in), op, r;
        if (l == null) { return null; }
        var out = new ArrayList<Form>();

        while (true) {
            op = readForm(in);
            if (op == null) { break; }
            r = readForm(in);
            if (r == null) { throw new E("Invalid expression: %s", r.toString()); }
            l = new ExprForm(((IdForm)op).id, l, r);
            out.add(l);
        }

        return l;
    }

    public void skipSpace(PushbackInputStream in) {
        try {
            var c = in.read();
            while (Character.isSpaceChar(c)) { c = in.read(); }
            if (c != -1) { in.unread(c); }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }
    }

    public Form readForm(PushbackInputStream in) {
        try {
            skipSpace(in);
            var c = in.read();
            if (c == -1) { return null; }

            if (Character.isDigit(c)) {
                in.unread(c);
                return new NumForm(readNum(in));
            } else {
                in.unread(c);
                return new IdForm(readId(in));
            }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }

        return null;
    }

    public Val eval(PushbackInputStream it) {
        var f = read(it);
        f.compile(this, env);
        new StopOp(this);
        var stack = new Stack();
        eval(new CallStack(), stack, 0);
        return stack.pop();
    }

    public String readId(PushbackInputStream in) {
        var buf = new StringBuilder();

        try {
            while (true) {
                var c = in.read();
                if (c == -1) { break; }

                if (Character.isSpaceChar(c)) {
                    in.unread(c);
                    break;
                }

                buf.append((char)c);
            }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }

        return buf.toString();
    }

    public int charToInt(int c, int base) {
        if (Character.isDigit(c)) {
            return c - '0';
        }

        if (base == 16 && c >= 'a' && c <= 'f') {
            return 10 + c - 'a';
        }

        return -1;
    }

    public Val readNum(PushbackInputStream in) {
        int base = 10;
        long out = 0;

        try {
            var c = in.read();
            if (c == -1) { return null; }
            if (c == '0') {
                c = in.read();

                switch (c) {
                    case 'b':
                        base = 2;
                        break;
                    case 'x':
                        base = 16;
                        break;
                    default:
                        in.unread('0');
                        if (c != -1) { in.unread(c); }
                }
            } else { in.unread(c); }

            while (true) {
                c = in.read();
                if (c == -1) { break; }
                var v = charToInt(c, base);

                if (v == -1) {
                    in.unread(c);
                    break;
                }

                out *= base;
                out += v;
            }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }

        return Val.make(LongType.it, out);
    }

    private Env env = new Env();
}
