package controller;

import org.junit.Assert;
import org.junit.Before;
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
public class TestStatsController {

    public StatsController statsController;
    public static final String STAT_VALUE = "12.34";

    @Rule
    public TemporaryFolder root = new TemporaryFolder();

    private File getRootDirectory() throws IOException {
        return root.newFolder("data");
    }

    public File createLeafDirectory() throws IOException {
        File data = getRootDirectory();
        return data;
    }

    @Before
    public void createMeasurerController(){
        statsController = new StatsController();
    }

    @Test
    public void shouldReturnEmptyStringForReadStatFromFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);

        Method methodUnderTest = statsController.getClass().getDeclaredMethod("readStatFromFile", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue((methodUnderTest.invoke(statsController, createdFile)).equals(""));
    }

    @Test
    public void shouldReturnNotEmptyStringForReadStatFromFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);
        Files.write(createdFile.toPath(), STAT_VALUE.getBytes(), WRITE);

        Method methodUnderTest = statsController.getClass().getDeclaredMethod("readStatFromFile", File.class);
        methodUnderTest.setAccessible(true);

        Assert.assertTrue((methodUnderTest.invoke(statsController, createdFile)).equals(STAT_VALUE));
    }

    @Test
    public void shouldCreateStatFile() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();

        Method methodUnderTest = statsController.getClass().getDeclaredMethod("createAggregateStatFile", File.class);
        methodUnderTest.setAccessible(true);
        File createFile = (File) methodUnderTest.invoke(statsController, dir);

        Assert.assertTrue("Creation of Stat file returned null", createFile != null);
        Assert.assertTrue("Created file does not exist on file system", createFile.exists());
    }

}
