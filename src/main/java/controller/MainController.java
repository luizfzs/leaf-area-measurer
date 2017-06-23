package controller;

import model.TemplateInfo;
import model.filefilter.AreaTemplateFileFilter;
import org.opencv.core.Scalar;

import java.io.File;
import java.io.IOException;

/**
 * Created by luiz on 11/06/17.
 */
public class MainController {

    public static void main(String[] args) throws IOException {
        loadOpenCV();
        AreaMeasurerController amc = new AreaMeasurerController();
        TemplateInfo templateInfo = amc.processAreaTemplate(new File("data").listFiles(new AreaTemplateFileFilter())[0]);
        System.out.println(templateInfo);

    }

    private static void loadOpenCV() throws IOException {
        File curDir = new File(".");
        System.load(curDir.getCanonicalPath() + "/libopencv_java249.so");
    }

}
