package controller;

import nu.pattern.OpenCV;
import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Mat;

import java.io.IOException;

/**
 * Created by luiz on 08/06/17.
 */
public class TestJavaCV {

    @Test
    public void shouldLoadJavaCV() throws IOException {
        OpenCV.loadShared();
        Assert.assertNotNull("OpenCV not loaded correctly",
                Mat.ones(1,1, 1).toString().equals(Mat.ones(1,1, 1).toString()));
    }
}
