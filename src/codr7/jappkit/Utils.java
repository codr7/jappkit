package codr7.jappkit;

import java.io.File;

public final class Utils {
    static void mkdir(String path) {
        new File(path).mkdirs();
    }

    static boolean rmdir(String path) {
        return rmdir(new File(path));
    }

    static boolean rmdir(File _) {
        File[] fs = _.listFiles();

        if (fs != null) {
            for (File f: _) { rmdir(_); }
        }

        return _.delete();
    }

    private Utils() { }
}
