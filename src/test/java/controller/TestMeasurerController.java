package controller;

import exceptions.MoreThanOneTemplateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by luiz on 09/06/17.
 */
public class TestMeasurerController {

    @Rule
    public TemporaryFolder root = new TemporaryFolder();

    public MeasurerController measurerController;

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

    @Before
    public void createMeasurerController(){
        measurerController = new MeasurerController();
    }

    @Test
    public void shouldReturnTrueForLeafDirectoryEmpty() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File dir = createLeafDirectory();
        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkLeafDirectory", File.class);
        methodUnderTest.setAccessible(true);
        Assert.assertTrue((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnFalseForLeafDirectoryEmpty() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File dir = createNotLeafDirectory();
        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkLeafDirectory", File.class);
        methodUnderTest.setAccessible(true);
        Assert.assertFalse((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnFalseForCheckTemplateFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkTemplateFile", File.class);
        methodUnderTest.setAccessible(true);
        Assert.assertFalse((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnTrueForCheckTemplateFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".txt", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkTemplateFile", File.class);
        methodUnderTest.setAccessible(true);
        Assert.assertTrue((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test(expected = MoreThanOneTemplateException.class)
    public void shouldThrowExceptionMoreThanOneTemplateFileForCheckTemplateFile() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".txt", dir);
        File.createTempFile("area-template2",".txt", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkTemplateFile", File.class);
        methodUnderTest.setAccessible(true);
        try {
            methodUnderTest.invoke(measurerController, dir);
        } catch (InvocationTargetException ite){
            throw ite.getTargetException();
        }

    }
}
