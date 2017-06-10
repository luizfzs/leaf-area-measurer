package helper;

import java.io.File;
import java.util.Arrays;

/**
 * Created by luiz on 09/06/17.
 */
public class DirectoryHelper {

    public static boolean checkLeafDirectory(File directory){
        return Arrays.stream(directory.listFiles()).filter(x -> x.isDirectory()).toArray().length == 0;
    }

    public static File[] getDirectories(File directory){
        return Arrays.stream(directory.listFiles()).filter(x -> x.isDirectory()).toArray(File[]::new);
    }

}
