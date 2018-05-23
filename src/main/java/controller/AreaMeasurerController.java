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
import java.util.List;
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
        boundingRectangle = getContourMaxArea(imageContours);

        if(Parameters.IMAGE_DEBUGGING) {
            drawBoundingRectangle(boundingRectangle, coloredAreaTemplateMat);
            writeImage(coloredAreaTemplateMat, "identified-template.jpg");
        }

        templateInfo.setRealDimension(new Point(dimensions.x, dimensions.y));
        templateInfo.setCalculatedDimension(new Point(boundingRectangle.width, boundingRectangle.height));

        swWholeMethod.stop();
        logger.info(String.format("Template processed in %s", swWholeMethod));
        return templateInfo;
    }

    public TemplateInfo processLeafImageFile(File leafImageFile){
        StopWatch swWholeMethod = new StopWatch("");
        TemplateInfo templateInfo = new TemplateInfo();
        ArrayList<MatOfPoint> imageContours = new ArrayList<>();
        Rect boundingRectangle;
        logger.info(String.format("Processing image %s", leafImageFile.getAbsolutePath()));
        swWholeMethod.start();

        Mat coloredAreaTemplateMat = loadImage(leafImageFile);
        Mat grayAreaTemplateMat = coloredAreaTemplateMat.clone();
        preProcessImage(grayAreaTemplateMat, 0, 0);
        getHoughTransform2(grayAreaTemplateMat, 1, Math.PI/180, 200);
//        Imgproc.morphologyEx(grayAreaTemplateMat, grayAreaTemplateMat, Imgproc.MORPH_CLOSE,
//                Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
//        if(Parameters.IMAGE_DEBUGGING) {
//            writeImage(grayAreaTemplateMat, String.format("%s-closed.jpg", FileHelper.getNameWithoutExtension(leafImageFile.getName())));
//        }

        processContours(imageContours, grayAreaTemplateMat);
        List<Rect> boundingRectangles = getBoundingRects(imageContours);
        if(Parameters.IMAGE_DEBUGGING) {
            for(Rect rect : boundingRectangles){
                drawBoundingRectangle(rect, coloredAreaTemplateMat);
            }
            writeImage(coloredAreaTemplateMat, String.format("%s-countour.jpg", FileHelper.getNameWithoutExtension(leafImageFile.getName())));
        }
//
//        templateInfo.setRealDimension(new Point(dimensions.x, dimensions.y));
//        templateInfo.setCalculatedDimension(new Point(boundingRectangle.width, boundingRectangle.height));

        swWholeMethod.stop();
        logger.info(String.format("Template processed in %s", swWholeMethod));
        return templateInfo;
    }

    private void drawBoundingRectangle(Rect boundingRectangle, Mat coloredAreaTemplateMat) {
        Core.rectangle(
                coloredAreaTemplateMat,
                new Point(boundingRectangle.x, boundingRectangle.y),
                new Point(boundingRectangle.x + boundingRectangle.width, boundingRectangle.y + boundingRectangle.height),
                new Scalar(0, 255, 0, 0),
                6);
    }

    private List<Rect> getBoundingRects(ArrayList<MatOfPoint> imageContours) {
        MatOfPoint2f imageContour2f;
        MatOfPoint2f approxContour2f;
        MatOfPoint approxContour;
        List<Rect> boundingRects = new ArrayList<>();

        imageContours = filterAreasByMinimumValue(imageContours);
        for(MatOfPoint matOfPoint : imageContours) {
            imageContour2f = new MatOfPoint2f();
            approxContour2f = new MatOfPoint2f();
            approxContour = new MatOfPoint();

            matOfPoint.convertTo(imageContour2f, CvType.CV_32FC2);
            Imgproc.approxPolyDP(imageContour2f, approxContour2f, Imgproc.arcLength(imageContour2f, true) * 0.04, true);
            approxContour2f.convertTo(approxContour, CvType.CV_32S);

            boundingRects.add(Imgproc.boundingRect(approxContour));
        }

        return boundingRects;
    }

    private Rect getContourMaxArea(ArrayList<MatOfPoint> imageContours) {
        double maxContourArea = 0.0;
        double currentContourArea;
        MatOfPoint maxContour = null;
        imageContours = filterAreasByMinimumValue(imageContours);
        for(MatOfPoint matOfPoint : imageContours){
            currentContourArea = Imgproc.contourArea(matOfPoint);
            if(currentContourArea > maxContourArea){
                maxContourArea = currentContourArea;
                maxContour = matOfPoint;
            }
        }
        return Imgproc.boundingRect(maxContour);
    }

    private ArrayList<MatOfPoint> filterAreasByMinimumValue(ArrayList<MatOfPoint> imageContours) {
        double contourArea = 0.0;
        ArrayList<MatOfPoint> filteredList = new ArrayList<>();
        for (MatOfPoint contour: imageContours) {
            contourArea = Imgproc.contourArea(contour);
            if(contourArea > 0.1) {
//                logger.debug(String.format("Area %f", contourArea));
            }
            if(contourArea > Parameters.MIN_AREA_THRESHOLD){
                filteredList.add(contour);
            }
        }
        return filteredList;
    }

    private void processContours(ArrayList<MatOfPoint> imageContours, Mat image) {
//        Imgproc.getStructuringElement()
        Imgproc.findContours(image, imageContours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
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

    public Mat getHoughTransform(Mat image, double rho, double theta, int threshold) {
        Mat result = image.clone();
        Mat lines = new Mat();
        Imgproc.HoughLinesP(image, lines, rho, theta, threshold, 00.0, 300.0);

        for (int i = 0; i < lines.cols(); i++) {
            double data[] = lines.get(0, i);
            double rho1 = data[0];
            double theta1 = data[1];
            double cosTheta = Math.cos(theta1);
            double sinTheta = Math.sin(theta1);
            double x0 = cosTheta * rho1;
            double y0 = sinTheta * rho1;
            Point pt1 = new Point(x0 + 10000 * (-sinTheta), y0 + 10000 * cosTheta);
            Point pt2 = new Point(x0 - 10000 * (-sinTheta), y0 - 10000 * cosTheta);
            Core.line(result, pt1, pt2, new Scalar(0, 0, 255), 10);
        }
        writeImage(result, "houghlines.jpg");
        return result;
    }

    public Mat getHoughTransform2(Mat image, double rho, double theta, int threshold) {
        Mat result = image.clone();
        Mat lines = new Mat();
        Mat edges = new Mat();

        Imgproc.Canny(image, edges, 50, 200, 3, true);
        writeImage(edges, "canny.jpg");
        Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180, 1, 30.0, 100.0);

        writeImage(result, "result.jpg");
        for (int i = 0; i < lines.cols(); i++) {
            double data[] = lines.get(0, i);
            double rho1 = data[0];
            double theta1 = data[1];
            double cosTheta = Math.cos(theta1);
            double sinTheta = Math.sin(theta1);
            double x0 = cosTheta * rho1;
            double y0 = sinTheta * rho1;
            Point pt1 = new Point(x0 + 10000 * (-sinTheta), y0 + 10000 * cosTheta);
            Point pt2 = new Point(x0 - 10000 * (-sinTheta), y0 - 10000 * cosTheta);
            Core.line(result, pt1, pt2, new Scalar(0, 0, 255), 10);
        }
        writeImage(result, "houghlines2.jpg");
        return result;
    }

    private void preProcessImage(Mat image, int matSize, int algFlags){
        changeLight(image, 2.0);

        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(image, image, 125, 255, Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
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
