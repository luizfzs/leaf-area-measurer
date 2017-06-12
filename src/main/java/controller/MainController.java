package controller;

import model.filefilter.AreaTemplateFileFilter;

import java.io.File;
import java.io.IOException;

/**
 * Created by luiz on 11/06/17.
 */
public class MainController {

    public static void main(String[] args) throws IOException {
        loadOpenCV();
        AreaMeasurerController amc = new AreaMeasurerController();
        amc.getAreaFromImageFile(new File("data").listFiles(new AreaTemplateFileFilter())[0]);
    }

    private static void loadOpenCV() throws IOException {
        File curDir = new File(".");
        System.load(curDir.getCanonicalPath() + "/libopencv_java249.so");
    }

}
