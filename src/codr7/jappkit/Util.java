package codr7.jappkit;

import java.io.File;
import java.nio.file.Path;

public final class Util {
    public static void mkdir(Path path) {
        new File(path.toString()).mkdirs();
    }

    public static boolean rmdir(Path path) {
        return rmdir(new File(path.toString()));
    }

    public static boolean rmdir(File it) {
        File[] fs = it.listFiles();

        if (fs != null) {
            for (File f: fs) { rmdir(f); }
        }

        return it.delete();
    }

    private Util() { }
}
