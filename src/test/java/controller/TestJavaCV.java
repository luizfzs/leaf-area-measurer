package controller;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.io.File;
import java.io.IOException;

/**
 * Created by luiz on 08/06/17.
 */
public class TestJavaCV {

    @Test
    public void shouldLoadJavaCV() throws IOException {
        File curDir = new File(".");
        System.load(curDir.getCanonicalPath() + "/libopencv_java249.so");
        Assert.assertNotNull("Incorrect value for JavaCV.SQRT2",
                Mat.ones(1,1, 1).toString().equals(Mat.ones(1,1, 1).toString()));
    }
}
