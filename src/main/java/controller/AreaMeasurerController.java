package controller;

import com.ecyrd.speed4j.StopWatch;
import helper.FileHelper;
import model.Parameters;
import model.TemplateInfo;
import org.apache.log4j.Logger;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opencv.highgui.Highgui.imread;
import static org.opencv.highgui.Highgui.imwrite;

/**
 * Created by luiz on 11/06/17.
 */
public class AreaMeasurerController {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public TemplateInfo processAreaTemplate(File areaTemplateImage){
        StopWatch swWholeMethod = new StopWatch("");
        logger.info(String.format("Processing template %s", areaTemplateImage.getAbsolutePath()));

        swWholeMethod.start();
        TemplateInfo templateInfo = new TemplateInfo();
        ArrayList<MatOfPoint> imageContours = new ArrayList<>();
        Rect boundingRectangle;

        Point dimensions = getAreaOfTemplateFromFileName(areaTemplateImage.getName());
        Mat coloredAreaTemplateMat = loadImage(areaTemplateImage);
        Mat grayAreaTemplateMat = coloredAreaTemplateMat.clone();
        preProcessImage(grayAreaTemplateMat, 0, 0);
        processContours(imageContours, grayAreaTemplateMat);
        boundingRectangle = getBoundingRect(imageContours);

        if(Parameters.IMAGE_DEBUGGING) {
            drawBoundingRectangle(boundingRectangle, dimensions, coloredAreaTemplateMat);
            writeImage(coloredAreaTemplateMat, "identified-template.jpg");
        }

        templateInfo.setRealDimension(new Point(dimensions.x, dimensions.y));
        templateInfo.setCalculatedDimension(new Point(boundingRectangle.width, boundingRectangle.height));

        swWholeMethod.stop();
        logger.info(String.format("Template processed in %s", swWholeMethod));
        return templateInfo;
    }

    private void drawBoundingRectangle(Rect boundingRectangle, Point dimensions, Mat coloredAreaTemplateMat) {
        Core.rectangle(
                coloredAreaTemplateMat,
                new Point(boundingRectangle.x, boundingRectangle.y),
                new Point(boundingRectangle.x + boundingRectangle.width, boundingRectangle.y + boundingRectangle.height),
                new Scalar(0, 255, 0, 0),
                6);
    }

    private Rect getBoundingRect(ArrayList<MatOfPoint> imageContours) {
        MatOfPoint2f imageContour2f = new MatOfPoint2f();
        MatOfPoint2f approxContour2f = new MatOfPoint2f();
        MatOfPoint approxContour = new MatOfPoint();

        imageContours = filterAreasByMinimumValue(imageContours);
        imageContours.get(0).convertTo(imageContour2f, CvType.CV_32FC2);
        Imgproc.approxPolyDP(imageContour2f, approxContour2f, Imgproc.arcLength(imageContour2f, true) * 0.04, true);
        approxContour2f.convertTo(approxContour, CvType.CV_32S);

        return Imgproc.boundingRect(approxContour);
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

    private Point getAreaOfTemplateFromFileName(String fileName){
        String fileNameWithoutExtension = FileHelper.getNameWithoutExtension(fileName);
        String dimensionsStr = fileNameWithoutExtension.replace(Parameters.TEMPLATE_FILE_PREFIX, "");
        String[] dimensions = dimensionsStr.split(Parameters.AREA_TEMPLATE_DIMENSION_SEPARATOR);

        Pattern p = Pattern.compile(Parameters.AREA_TEMPLATE_DIMENSION_REGEX);
        Matcher m1 = p.matcher(dimensions[0]);
        Matcher m2 = p.matcher(dimensions[1]);

        if(m1.find() && m2.find()){
            return new Point(Double.parseDouble(m1.group(1)), Double.parseDouble(m2.group(1)));
        }

        return new Point(0,0);
    }

    private void preProcessImage(Mat image, int matSize, int algFlags){
        changeLight(image, 2.0);

        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(image, image, 100, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
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
                break;
        }
    }

    private void changeLight(Mat image, double factor) {
        double[] point;
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HLS);
        for(int i = 0; i < image.height(); ++i){
            for (int j = 0; j < image.width(); ++j){
                point = image.get(i, j);
                point[1] *= factor;
                image.put(i, j, point);
            }
        }
        Imgproc.cvtColor(image, image, Imgproc.COLOR_HLS2BGR);
    }

    private Mat loadImage(File imageFile){
        return imread(imageFile.getAbsolutePath());
    }

    private void writeImage(Mat image, String name){
        imwrite(name, image);
    }

}
