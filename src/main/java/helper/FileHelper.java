package helper;

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
}
