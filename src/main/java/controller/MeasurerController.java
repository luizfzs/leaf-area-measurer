package controller;

import com.ecyrd.speed4j.StopWatch;
import exceptions.MoreThanOneTemplateException;
import exceptions.NoAreaTemplateFoundException;
import helper.DirectoryHelper;
import model.Parameters;
import model.TemplateInfo;
import model.filefilter.AreaTemplateFileFilter;
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
    private AreaMeasurerController amc = new AreaMeasurerController();

    public void processFiles(File directory, TemplateInfo templateInfo){
        StopWatch swWholeMethod = new StopWatch("");
        logger.info(String.format("Entering directory %s", directory.getAbsolutePath()));
        swWholeMethod.start();

        if(checkTemplateFile(directory)){
            File template = getTemplateFile(directory);
            templateInfo = amc.processAreaTemplate(template);
            logger.debug(String.format("Current template file %s", templateInfo));
        }

        if(DirectoryHelper.checkLeafDirectory(directory)){
            if(templateInfo == null){
                throw new NoAreaTemplateFoundException(directory.getAbsolutePath());
            }

            if(checkImageFiles(directory)) {
                logger.info(String.format("Processing images at directory %s", directory.getAbsolutePath()));

                File[] imageFiles = getImageFiles(directory);
                logger.debug(Arrays.stream(imageFiles).map(x -> x.getName()).collect(Collectors.toList()));

                processImageFiles(imageFiles);
            } else {
                logger.warn(String.format("No image files found at %s. Skipping directory", directory.getAbsolutePath()));
            }
        } else {
            for (File child : Arrays.stream(directory.listFiles()).sorted(Comparator.comparing(File::getName)).toArray(File[]::new)) {
                if (child.isDirectory()) {
                    processFiles(child, templateInfo);
                }
            }
        }

        swWholeMethod.stop();
        logger.info(String.format("Directory processed in %s. Leaving directory %s", swWholeMethod, directory.getAbsolutePath()));
    }

    private void processImageFiles(File[] imageFiles) {



        for(File leafImage : imageFiles) {
            amc.processLeafImageFile(leafImage);
        }
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
