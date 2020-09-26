package codr7.jappkit.db;

import codr7.jappkit.E;
import codr7.jappkit.error.EOF;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.time.Instant;

public final class Encoding {
    public static final String charsetName = "UTF-8";

    public static boolean readBoolean(SeekableByteChannel in) { return readByte(in) == 1; }

    public static byte readByte(SeekableByteChannel in) {
        ByteBuffer buf = ByteBuffer.allocate(1);

        try {
            if (in.read(buf) == -1) { throw new EOF(); }
        } catch (IOException e){ throw new E(e); }

        buf.rewind();
        return buf.get();
    }

    public static long readLong(SeekableByteChannel in) {
        byte len = readByte(in);
        if (len == 0) { return 0L; }
        ByteBuffer buf = ByteBuffer.allocate(len);
        try { in.read(buf); } catch (IOException e){ throw new E(e); }
        buf.rewind();
        byte[] bs = new byte[len];
        buf.get(bs);
        return Long.valueOf(new String(bs)).longValue();
    }

    public static String readString(SeekableByteChannel in) {
        long len = readLong(in);
        ByteBuffer buf = ByteBuffer.allocate((int)len);
        try { in.read(buf); } catch (IOException e) { throw new E(e); }
        buf.rewind();
        byte[] bs = new byte[(int)len];
        buf.get(bs);
        return new String(bs);
    }

    public static Instant readTime(SeekableByteChannel in) { return Instant.ofEpochMilli(readLong(in)); }

    public static void writeBoolean(boolean it, SeekableByteChannel out) { writeByte((byte)(it ? 1 : 0), out); }

    public static void writeByte(byte it, SeekableByteChannel out) {
        ByteBuffer buf = ByteBuffer.allocate(1);
        buf.put(it);
        buf.rewind();

        try { out.write(buf); } catch (IOException e) { throw new E(e); }
    }

    public static void writeLong(long it, SeekableByteChannel out) {
        if (it == 0L) {
            writeByte((byte)0, out);
        } else {
            byte[] bs = Long.valueOf(it).toString().getBytes();
            ByteBuffer buf = ByteBuffer.allocate(bs.length + 1);
            buf.put((byte) bs.length);
            buf.put(bs);
            buf.rewind();

            try { out.write(buf); } catch (IOException e) { throw new E(e); }
        }
    }

    public static void writeString(String it, SeekableByteChannel out) {
        byte[] bs = null;
        try { bs = it.getBytes(charsetName); }
        catch (UnsupportedEncodingException e) { throw new E(e.getMessage()); }

        writeLong(bs.length, out);
        ByteBuffer buf = ByteBuffer.allocate(bs.length);
        buf.put(bs);
        buf.rewind();

        try { out.write(buf); } catch (IOException e) { throw new E(e); }
    }

    public static void writeTime(Instant it, SeekableByteChannel out) { writeLong(it.toEpochMilli(), out); }

    private Encoding() { }
}
