package codr7.jappkit.calc;

import codr7.jappkit.E;
import codr7.jappkit.Fix;
import codr7.jappkit.Val;
import codr7.jappkit.calc.forms.ExprForm;
import codr7.jappkit.calc.forms.IdForm;
import codr7.jappkit.calc.forms.NumForm;
import codr7.jappkit.type.FixType;
import codr7.jappkit.type.LongType;

import java.io.*;
import java.util.ArrayList;

public class Reader {
    public static int charToInt(int c, int base) {
        if (Character.isDigit(c)) {
            return c - '0';
        }

        if (base == 16 && c >= 'a' && c <= 'f') {
            return 10 + c - 'a';
        }

        return -1;
    }

    public Reader(PushbackInputStream in) { this.in = in; }

    public Reader(InputStream in) { this(new PushbackInputStream(in)); }

    public Reader(String in) { this(new ByteArrayInputStream(in.getBytes())); }

    public Form read() {
        Form l = readForm(), op, r;
        if (l == null) { return null; }
        var out = new ArrayList<Form>();

        while (true) {
            op = readForm();
            if (op == null) { break; }
            r = readForm();
            if (r == null) { throw new E("Invalid expression: %s", r.toString()); }
            l = new ExprForm(((IdForm)op).id, l, r);
            out.add(l);
        }

        return l;
    }

    public void skipSpace() {
        try {
            var c = in.read();
            while (Character.isSpaceChar(c)) { c = in.read(); }
            if (c != -1) { in.unread(c); }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }
    }

    public Form readForm() {
        try {
            skipSpace();
            var c = in.read();
            if (c == -1) { return null; }

            if (Character.isDigit(c)) {
                in.unread(c);
                return new NumForm(readNum());
            } else {
                in.unread(c);
                return new IdForm(readId());
            }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }

        return null;
    }

    public String readId() {
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

    public Val readNum() {
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
                if (c == '.') { return readFix(out, base); }
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

    public Val readFix(long val, int base) {
        long out = 0;
        int scale = 1;

        try {
            while (true) {
                var c = in.read();
                if (c == -1) { break; }
                var v = charToInt(c, base);

                if (v == -1) {
                    in.unread(c);
                    break;
                }

                out *= base;
                out += v;
                scale *= base;
            }
        }
        catch (EOFException e) {}
        catch (IOException e) { throw new E(e); }

        return Val.make(FixType.it, Fix.make(val) + out * Fix.SCALE / scale);
    }

    private final PushbackInputStream in;
}
