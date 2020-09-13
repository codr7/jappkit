package codr7.jappkit.db;

import java.io.File;

public final class Utils {
    static void mkdir(String path) {
        new File(path).mkdirs();
    }

    static boolean rmdir(String path) {
        return rmdir(new File(path));
    }

    static boolean rmdir(File it) {
        File[] fs = it.listFiles();

        if (fs != null) {
            for (File f: fs) { rmdir(f); }
        }

        return it.delete();
    }

    private Utils() { }
}
