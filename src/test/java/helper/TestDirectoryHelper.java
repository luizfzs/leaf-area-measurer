package helper;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

/**
 * Created by luiz on 10/06/17.
 */
public class TestDirectoryHelper {

    @Rule
    public TemporaryFolder root = new TemporaryFolder();

    private File getRootDirectory() throws IOException {
        return root.newFolder("data");
    }

    public File createLeafDirectory() throws IOException {
        File data = getRootDirectory();
        return data;
    }

    public File createNotLeafDirectory() throws IOException{
        File data = getRootDirectory();
        root.newFolder(data.getName(), "data-sub1");
        return data;
    }

    @Test
    public void shouldReturnFalseForLeafDirectoryEmpty() throws IOException {
        File dir = createNotLeafDirectory();
        Assert.assertFalse(DirectoryHelper.checkLeafDirectory(dir));
    }

    @Test
    public void shouldReturnTrueForLeafDirectoryEmpty() throws IOException {
        File dir = createLeafDirectory();
        Assert.assertTrue(DirectoryHelper.checkLeafDirectory(dir));
    }

    @Test
    public void shouldReturnEmptyListForGetDirectories() throws IOException {
        File dir = createLeafDirectory();
        Assert.assertTrue(DirectoryHelper.getDirectories(dir).length == 0);
    }

    @Test
    public void shouldReturnNonEmptyListForGetDirectories() throws IOException {
        File dir = createNotLeafDirectory();
        Assert.assertTrue(DirectoryHelper.getDirectories(dir).length > 0);
    }
}
