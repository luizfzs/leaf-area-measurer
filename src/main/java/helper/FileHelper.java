package helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.*;

/**
 * Created by luiz on 09/06/17.
 */
public class FileHelper {

    public static String getNameWithoutExtension(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".", fileName.length()));
    }

    public static void truncateFile(File file) throws IOException {
        Files.write(
                file.toPath(),
                "".getBytes(),
                CREATE, TRUNCATE_EXISTING, WRITE);
    }
}
