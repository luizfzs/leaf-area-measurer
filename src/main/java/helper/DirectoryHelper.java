package helper;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by luiz on 09/06/17.
 */
public class DirectoryHelper {

    public static boolean checkLeafDirectory(File directory){
        return Arrays.stream(directory.listFiles()).filter(x -> x.isDirectory()).sorted(Comparator.comparing(File::getName)).toArray().length == 0;
    }

    public static File[] getDirectories(File directory){
        return Arrays.stream(directory.listFiles()).filter(x -> x.isDirectory()).sorted(Comparator.comparing(File::getName)).toArray(File[]::new);
    }

}
