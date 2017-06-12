package controller;

import helper.FileHelper;
import model.Parameters;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.highgui.Highgui.imwrite;

/**
 * Created by luiz on 11/06/17.
 */
public class AreaMeasurerController {

    public Double getAreaFromImageFile(File imageFile){
        return getAreaFromImageFile(imageFile, 2, 0);
    }

    private Double getAreaFromImageFile(File imageFile, int matSize, int algFlags){
        double sumAreas = 0.0;
        Double resultArea = null;
        ArrayList<MatOfPoint> imageContours = new ArrayList<>();

        Double templateArea = getAreaOfTemplateFromFileName(imageFile.getName());
        Mat image = loadImage(imageFile);

        preProcessImage(image, matSize, algFlags);
        processContours(imageContours, image);
        imageContours = filterAreasByMinimumValue(imageContours);

        Iterator<MatOfPoint> each = imageContours.iterator();
        while(each.hasNext()){
            MatOfPoint wrapper = each.next();
            double area = Imgproc.contourArea(wrapper);
            sumAreas += area;
        }

        System.out.println(matSize + "," + algFlags + "," + templateArea + "," + (templateArea / sumAreas));

        return resultArea;
    }

    private ArrayList<MatOfPoint> filterAreasByMinimumValue(ArrayList<MatOfPoint> imageContours) {
        ArrayList<MatOfPoint> filteredList = new ArrayList<>();
        for (MatOfPoint contour: imageContours) {
            if(Imgproc.contourArea(contour) > Parameters.MIN_AREA_THRESHOLD){
                filteredList.add(contour);
            }
        }
        return filteredList;
    }

    private void processContours(ArrayList<MatOfPoint> imageContours, Mat image) {
        Imgproc.findContours(image, imageContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);
        Imgproc.drawContours(image, imageContours, -1, new Scalar(255, 0, 0), 3);
    }

    private Double getAreaOfTemplateFromFileName(String fileName){
        String fileNameWithoutExtension = FileHelper.getNameWithoutExtension(fileName);
        String dimensionsStr = fileNameWithoutExtension.replace(Parameters.TEMPLATE_FILE_PREFIX, "");
        String[] dimensions = dimensionsStr.split(Parameters.AREA_TEMPLATE_DIMENSION_SEPARATOR);

        Pattern p = Pattern.compile(Parameters.AREA_TEMPLATE_DIMESION_REGEX);
        Matcher m1 = p.matcher(dimensions[0]);
        Matcher m2 = p.matcher(dimensions[1]);

        if(m1.find() && m2.find()){
            return Double.parseDouble(m1.group(1)) * Double.parseDouble(m2.group(1));
        }

        return 0.0;
    }

    private void preProcessImage(Mat image, int matSize, int algFlags){
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(image, image, 150, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        switch (algFlags){
            case 1:
                Imgproc.erode(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(matSize, matSize)));
                break;
            case 2:
                Imgproc.dilate(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(matSize, matSize)));
                break;
            case 3:
                Imgproc.erode(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(matSize, matSize)));
                Imgproc.dilate(image, image, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(matSize, matSize)));

        }
    }

    private Mat loadImage(File imageFile){
        return imread(imageFile.getAbsolutePath());
    }

    private void writeImage(Mat image, String name){
        imwrite(name, image);
    }

}
