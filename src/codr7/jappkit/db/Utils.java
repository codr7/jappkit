package codr7.jappkit.db;

import java.io.File;
import java.nio.file.Path;

public final class Utils {
    static void mkdir(Path path) {
        new File(path.toString()).mkdirs();
    }

    static boolean rmdir(Path path) {
        return rmdir(new File(path.toString()));
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
