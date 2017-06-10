package helper;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by luiz on 10/06/17.
 */
public class TestFileHelper {

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

}
