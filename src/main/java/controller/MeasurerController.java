package controller;

import exceptions.MoreThanOneTemplateException;
import exceptions.NoAreaTemplateFoundException;
import model.filefilter.AreaTemplateFileFilter;
import model.Parameters;
import model.filefilter.LeafImageFileFilter;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by luiz on 08/06/17.
 */
public class MeasurerController {

    public static void main(String[] args){
        new MeasurerController().processFiles(new File("data"), null);
    }

    public void processFiles(File directory, File template){
        System.out.println(directory.getAbsolutePath());

        if(checkTemplateFile(directory)){
            template = getTemplateFile(directory);
        }

        if(checkLeafDirectory(directory)){
            if(template == null){
                throw new NoAreaTemplateFoundException(directory.getAbsolutePath());
            }
            // TODO call processing method

            if(checkImageFiles(directory)) {
                System.out.println("Processing images at directory " + directory.getAbsolutePath());
                System.out.println(Arrays.stream(getImageFiles(directory)).map(x -> x.getName()).collect(Collectors.toList()));
            } else {
                System.out.println("No image files found at " + directory.getAbsolutePath());
                System.out.println("Skipping directory " + directory.getAbsolutePath());
            }
        } else {
            for (File child : directory.listFiles()) {
                if (child.isDirectory()) {
                    processFiles(child, template);
                }
            }
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

    private boolean checkLeafDirectory(File directory){
        return Arrays.stream(directory.listFiles()).filter(x -> x.isDirectory()).toArray().length == 0;
    }

    private boolean checkImageFiles(File currentDirectory){
        return currentDirectory.listFiles(new LeafImageFileFilter()).length > 0;
    }

    private File[] getImageFiles(File currentDirectory){
        return currentDirectory.listFiles(new LeafImageFileFilter());
    }

}
