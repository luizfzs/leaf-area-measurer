package controller;

import exceptions.MoreThanOneTemplateException;
import exceptions.NoAreaTemplateFoundException;
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

    @Before
    public void createMeasurerController(){
        measurerController = new MeasurerController();
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

    @Test(expected = NoAreaTemplateFoundException.class)
    public void shouldThrowExceptionNoAreaTemplateFoundForGetTemplateFile() throws Throwable {
        File dir = createLeafDirectory();

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getTemplateFile", File.class);
        methodUnderTest.setAccessible(true);
        try {
            methodUnderTest.invoke(measurerController, dir);
        } catch (InvocationTargetException ite){
            throw ite.getTargetException();
        }
    }

    @Test
    public void shouldTemplateFilePngForGetTemplateFile() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getTemplateFile", File.class);
        methodUnderTest.setAccessible(true);

        File templateFile = (File) methodUnderTest.invoke(measurerController, dir);
        Assert.assertNotNull(templateFile);
    }

    @Test
    public void shouldTemplateJpgFileForGetTemplateFile() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".jpg", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getTemplateFile", File.class);
        methodUnderTest.setAccessible(true);

        File templateFile = (File) methodUnderTest.invoke(measurerController, dir);
        Assert.assertNotNull(templateFile);
    }

    @Test
    public void shouldReturnTrueForPngFormatForCheckImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnTrueForJpgFormatForCheckImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".jpg", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnFalseForTxtFormatForCheckImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".txt", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertFalse((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnFalseForOnlyAreaTemplateForCheckImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("checkImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertFalse((Boolean) methodUnderTest.invoke(measurerController, dir));
    }

    @Test
    public void shouldReturnEmptyListForOnlyAreaTemplateForGetImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue(((File[]) methodUnderTest.invoke(measurerController, dir)).length == 0);
    }

    @Test
    public void shouldReturnOneElementListForGetImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue(((File[]) methodUnderTest.invoke(measurerController, dir)).length == 1);
    }

    @Test
    public void shouldReturnTwoElementsListForGetImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".png", dir);
        File.createTempFile("image2",".jpg", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue(((File[]) methodUnderTest.invoke(measurerController, dir)).length == 2);
    }

    @Test
    public void shouldReturnTwoElementsListExcludingOneAreaTemplateJpgForGetImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".png", dir);
        File.createTempFile("image2",".jpg", dir);
        File.createTempFile("area-template1",".jpg", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue(((File[]) methodUnderTest.invoke(measurerController, dir)).length == 2);
    }

    @Test
    public void shouldReturnTwoElementsListExcludingOneAreaTemplatePngForGetImageFiles() throws Throwable {
        File dir = createLeafDirectory();
        File.createTempFile("image1",".png", dir);
        File.createTempFile("image2",".jpg", dir);
        File.createTempFile("area-template1",".png", dir);

        Method methodUnderTest = measurerController.getClass().getDeclaredMethod("getImageFiles", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue(((File[]) methodUnderTest.invoke(measurerController, dir)).length == 2);
    }

    @Test(expected = NoAreaTemplateFoundException.class)
    public void shouldThrowExceptionProcessFileWithoutAreaTemplate() throws IOException {
        File dir = createLeafDirectory();
        measurerController.processFiles(dir, null);
    }

    @Test
    public void shouldPassRootDirHasOnlyAreaTemplateFile() throws IOException {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);
        measurerController.processFiles(dir, null);
    }

    @Test
    public void shouldPassRootDirHasAreaTemplateFileAndOneImageFile() throws IOException {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);
        File.createTempFile("image1",".png", dir);
        measurerController.processFiles(dir, null);
    }

    @Test
    public void shouldPassRootDirHasAreaTemplateFileAndMoreThanOneImageFile() throws IOException {
        File dir = createLeafDirectory();
        File.createTempFile("area-template1",".png", dir);
        File.createTempFile("image1",".png", dir);
        File.createTempFile("image2",".png", dir);
        measurerController.processFiles(dir, null);
    }

    @Test
    public void shouldPassRootDirHasAnotherDirInside() throws IOException {
        File dir = createLeafDirectory();
        root.newFolder(dir.getName(), "data-sub1");
        File.createTempFile("area-template1",".png", dir.listFiles()[0]);

        measurerController.processFiles(dir, null);
    }

}
