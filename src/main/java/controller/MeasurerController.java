package controller;

import exceptions.MoreThanOneTemplateException;
import exceptions.NoAreaTemplateFoundException;
import helper.DirectoryHelper;
import model.filefilter.AreaTemplateFileFilter;
import model.Parameters;
import model.filefilter.LeafImageFileFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by luiz on 08/06/17.
 */
public class MeasurerController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public void processFiles(File directory, File template){
        logger.info(String.format("Entering directory %s", directory.getAbsolutePath()));

        if(checkTemplateFile(directory)){
            template = getTemplateFile(directory);
        }

        if(DirectoryHelper.checkLeafDirectory(directory)){
            if(template == null){
                throw new NoAreaTemplateFoundException(directory.getAbsolutePath());
            }

            if(checkImageFiles(directory)) {
                // TODO call processing method
                logger.info(String.format("Processing images at directory %s", directory.getAbsolutePath()));
                logger.debug(Arrays.stream(getImageFiles(directory)).map(x -> x.getName()).collect(Collectors.toList()));
            } else {
                logger.warn(String.format("No image files found at %s. Skipping directory", directory.getAbsolutePath()));
            }
        } else {
            for (File child : Arrays.stream(directory.listFiles()).sorted(Comparator.comparing(File::getName)).toArray(File[]::new)) {
                if (child.isDirectory()) {
                    processFiles(child, template);
                }
            }
        }
        logger.info(String.format("Leaving directory %s", directory.getAbsolutePath()));
    }

    private boolean checkTemplateFile(File currentDirectory){
        String[] templates = currentDirectory.list(((file, s) -> s.startsWith(Parameters.TEMPLATE_FILE_PREFIX)));
        if(templates != null && templates.length > 1){
            throw new MoreThanOneTemplateException(currentDirectory.getAbsolutePath());
        }
        return (templates != null && templates.length == 1);
    }

    private File getTemplateFile(File currentDirectory){
        if (checkTemplateFile(currentDirectory)){
            return currentDirectory.listFiles(new AreaTemplateFileFilter())[0];
        }
        throw new NoAreaTemplateFoundException(currentDirectory.getAbsolutePath());
    }

    private boolean checkImageFiles(File currentDirectory){
        return currentDirectory.listFiles(new LeafImageFileFilter()).length > 0;
    }

    private File[] getImageFiles(File currentDirectory){
        return currentDirectory.listFiles(new LeafImageFileFilter());
    }

}
