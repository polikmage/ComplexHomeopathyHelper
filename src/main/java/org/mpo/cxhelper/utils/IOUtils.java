package org.mpo.cxhelper.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {
    public static String readTextFile(File file) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file.toURI())));
        return content;
    }
}
