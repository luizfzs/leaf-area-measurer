package controller;

import helper.FileHelper;
import model.Parameters;
import model.filefilter.StatsFileFilter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FilenameFilter;
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
    public static final String STAT_VALUE2 = "12.35";

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

    @Test
    public void shouldCreateStatFileForLeafDirectory() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);
        Files.write(createdFile.toPath(), STAT_VALUE.getBytes(), WRITE);
        File createdFile2 = File.createTempFile("abd",".txt", dir);
        Files.write(createdFile2.toPath(), STAT_VALUE2.getBytes(), WRITE);

        Method createAggregateStatFile = statsController.getClass().getDeclaredMethod("createAggregateStatFile", File.class);
        createAggregateStatFile.setAccessible(true);
        File aggregateStatFile = (File) createAggregateStatFile.invoke(statsController, dir);

        Method methodUnderTest = statsController.getClass()
                .getDeclaredMethod("processStatFiles", File[].class, File.class);
        methodUnderTest.setAccessible(true);
        methodUnderTest.invoke(statsController,
                dir.listFiles(new StatsFileFilter()),
                aggregateStatFile);

        StringBuilder aggregateStatFileContent = new StringBuilder();
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE);
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile2.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE2);

        Method readStatFromFile = statsController.getClass()
                .getDeclaredMethod("readStatFromFile", File.class);
        readStatFromFile.setAccessible(true);
        String readAggregateStatsFileContent = (String) readStatFromFile.invoke(statsController, aggregateStatFile);

        Assert.assertTrue("Content of aggregated file is wrong",
                readAggregateStatsFileContent.equals(aggregateStatFileContent.toString()));
    }

    @Test
    public void shouldProcessDirectory() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);
        Files.write(createdFile.toPath(), STAT_VALUE.getBytes(), WRITE);
        File createdFile2 = File.createTempFile("abd",".txt", dir);
        Files.write(createdFile2.toPath(), STAT_VALUE2.getBytes(), WRITE);

        Method methodUnderTest = statsController.getClass()
                .getDeclaredMethod("processDirectory", File.class);
        methodUnderTest.setAccessible(true);
        methodUnderTest.invoke(statsController, dir);

        StringBuilder aggregateStatFileContent = new StringBuilder();
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE);
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile2.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE2);

        File aggregateStatFile = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals(dir.getName() + Parameters.STATS_FILE_EXTENSION_WITH_DOT);
            }
        })[0];

        Method readStatFromFile = statsController.getClass()
                .getDeclaredMethod("readStatFromFile", File.class);
        readStatFromFile.setAccessible(true);
        String readAggregateStatsFileContent = (String) readStatFromFile.invoke(statsController, aggregateStatFile);

        Assert.assertTrue("Content of aggregated file is wrong",
                readAggregateStatsFileContent.equals(aggregateStatFileContent.toString()));
    }

    @Test
    public void shouldAggregateStatsOfLeafDirectory() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createLeafDirectory();
        File createdFile = File.createTempFile("abc",".txt", dir);
        Files.write(createdFile.toPath(), STAT_VALUE.getBytes(), WRITE);
        File createdFile2 = File.createTempFile("abd",".txt", dir);
        Files.write(createdFile2.toPath(), STAT_VALUE2.getBytes(), WRITE);

        Method methodUnderTest = statsController.getClass()
                .getDeclaredMethod("aggregateStats", File.class);
        methodUnderTest.setAccessible(true);
        methodUnderTest.invoke(statsController, dir);

        StringBuilder aggregateStatFileContent = new StringBuilder();
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE);
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile2.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE2);

        File aggregateStatFile = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals(dir.getName() + Parameters.STATS_FILE_EXTENSION_WITH_DOT);
            }
        })[0];

        Method readStatFromFile = statsController.getClass()
                .getDeclaredMethod("readStatFromFile", File.class);
        readStatFromFile.setAccessible(true);
        String readAggregateStatsFileContent = (String) readStatFromFile.invoke(statsController, aggregateStatFile);

        Assert.assertTrue("Content of aggregated file is wrong",
                readAggregateStatsFileContent.equals(aggregateStatFileContent.toString()));
    }

    @Test
    public void shouldAggregateStatsOfNonLeafDirectory() throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        File dir = createNotLeafDirectory();
        File subDir = dir.listFiles()[0];
        File createdFile = File.createTempFile("abc",".txt", subDir);
        Files.write(createdFile.toPath(), STAT_VALUE.getBytes(), WRITE);
        File createdFile2 = File.createTempFile("abd",".txt", subDir);
        Files.write(createdFile2.toPath(), STAT_VALUE2.getBytes(), WRITE);

        Method createAggregateStatFile = statsController.getClass().getDeclaredMethod("createAggregateStatFile", File.class);
        createAggregateStatFile.setAccessible(true);
        createAggregateStatFile.invoke(statsController, dir);

        Method methodUnderTest = statsController.getClass()
                .getDeclaredMethod("aggregateStats", File.class);
        methodUnderTest.setAccessible(true);
        methodUnderTest.invoke(statsController, dir);

        StringBuilder aggregateStatFileContent = new StringBuilder();
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE);
        aggregateStatFileContent.append(FileHelper.getNameWithoutExtension(createdFile2.getName()));
        aggregateStatFileContent.append(",").append(STAT_VALUE2);

        File aggregateStatFile = subDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.equals(subDir.getName() + Parameters.STATS_FILE_EXTENSION_WITH_DOT);
            }
        })[0];

        Method readStatFromFile = statsController.getClass()
                .getDeclaredMethod("readStatFromFile", File.class);
        readStatFromFile.setAccessible(true);
        String readAggregateStatsFileContent = (String) readStatFromFile.invoke(statsController, aggregateStatFile);

        Assert.assertTrue("Content of aggregated file is wrong",
                readAggregateStatsFileContent.equals(aggregateStatFileContent.toString()));
    }

}
