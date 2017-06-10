package helper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.WRITE;

/**
 * Created by luiz on 10/06/17.
 */
public class TestFileHelper {

    public static final String ABCABC = "abcabc";
    @Rule
    public TemporaryFolder root = new TemporaryFolder();

    private File getRootDirectory() throws IOException {
        return root.newFolder("data");
    }

    public File createLeafDirectory() throws IOException {
        File data = getRootDirectory();
        return data;
    }

    @Test
    public void shouldReturnFileName(){
        String fileName = "abc.txt";
        Assert.assertTrue(FileHelper.getNameWithoutExtension(fileName).equals("abc"));
    }

    @Test
    public void shouldReturnFileNameStrippingOnlyLastExtensions(){
        String fileName = "abc.txt.png";
        Assert.assertTrue(FileHelper.getNameWithoutExtension(fileName).equals("abc.txt"));
    }

    @Test
    public void shouldReturnExtension(){
        String fileName = "abc.txt";
        Assert.assertTrue(FileHelper.getExtension(fileName).equals(".txt"));
    }

    @Test
    public void shouldReturnOnlyLastExtension(){
        String fileName = "abc.txt.png";
        Assert.assertTrue(FileHelper.getExtension(fileName).equals(".png"));
    }

    @Test
    public void shouldReturnShouldTruncateFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);
        Files.write(createdFile.toPath(), ABCABC.getBytes(), WRITE);

        Assert.assertTrue("Content was not written to test file",
                createdFile.length() == ABCABC.length());

        FileHelper.truncateFile(createdFile);

        Assert.assertTrue("Test file was not truncated",
                createdFile.length() == 0);
    }

}
