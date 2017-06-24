package controller;

import model.TemplateInfo;
import model.filefilter.AreaTemplateFileFilter;
import nu.pattern.OpenCV;
import org.apache.log4j.Logger;
import org.opencv.core.Scalar;

import java.io.File;
import java.io.IOException;

/**
 * Created by luiz on 11/06/17.
 */
public class MainController {

    private static Logger logger = Logger.getLogger(MainController.class);

    public static void main(String[] args) throws IOException {
        loadOpenCV();

        AreaMeasurerController amc = new AreaMeasurerController();
        TemplateInfo templateInfo = amc.processAreaTemplate(new File("data").listFiles(new AreaTemplateFileFilter())[0]);
        logger.debug(templateInfo);
    }

    private static void loadOpenCV() throws IOException {
        OpenCV.loadShared();
        logger.debug("Successfully loaded OpenCV library");
    }

}
