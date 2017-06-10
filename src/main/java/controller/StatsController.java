package controller;

import exceptions.CouldNotCreateStatFileException;
import helper.DirectoryHelper;
import helper.FileHelper;
import model.Parameters;
import model.filefilter.StatsFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.*;

/**
 * Created by luiz on 09/06/17.
 */
public class StatsController {

    public static void main(String[] args) throws IOException {
        new StatsController().aggregateStats(new File("data"));
    }

    public void aggregateStats(File currentDirectory) throws IOException {
        if(DirectoryHelper.checkLeafDirectory(currentDirectory)){
            processDirectory(currentDirectory);
        } else {
            File[] directories = DirectoryHelper.getDirectories(currentDirectory);
            for (File directory : directories){
                aggregateStats(directory);
            }
        }
    }

    private void processDirectory(File currentDirectory) throws IOException {
        String stat;
        File[] stats = currentDirectory.listFiles(new StatsFileFilter());
        File aggregateStat = new File(currentDirectory.getAbsolutePath()
                + "/" + currentDirectory.getName()
                + Parameters.STATS_FILE_EXTENSION_WITH_DOT);
        Files.write(
                aggregateStat.toPath(),
                "".getBytes(),
                CREATE, TRUNCATE_EXISTING, WRITE);

        if(!aggregateStat.exists() && !aggregateStat.createNewFile()){
            throw new CouldNotCreateStatFileException(currentDirectory.getAbsolutePath());
        }

        for(File statFile : stats){
            if(statFile.getAbsolutePath().equals(aggregateStat.getAbsolutePath())){
                continue;
            }
            stat = readStatFromFile(statFile);
            Files.write(
                    aggregateStat.toPath(),
                    (FileHelper.getNameWithoutExtension(statFile.getName()) + "," + stat).getBytes(),
                    APPEND);
        }
    }

    private String readStatFromFile(File statFile) throws IOException {
        return new String(Files.readAllBytes(statFile.toPath()));
    }

}
