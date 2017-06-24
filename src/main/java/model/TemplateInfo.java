package model;

import org.opencv.core.Point;

/**
 * Created by luiz on 12/06/17.
 */
public class TemplateInfo {

    private Point realDimension;

    private Point calculatedDimension;

    public Point getRealDimension() {
        return realDimension;
    }

    public void setRealDimension(Point realDimension) {
        this.realDimension = realDimension;
    }

    public Point getCalculatedDimension() {
        return calculatedDimension;
    }

    public void setCalculatedDimension(Point calculatedDimension) {
        this.calculatedDimension = calculatedDimension;
    }

    public double getRealDimensioRatio(){
        return (realDimension.x / realDimension.y);
    }

    public double getCalculatedDimensioRatio(){
        return (calculatedDimension.x / calculatedDimension.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateInfo that = (TemplateInfo) o;

        if (realDimension != null ? !realDimension.equals(that.realDimension) : that.realDimension != null)
            return false;
        return calculatedDimension != null ? calculatedDimension.equals(that.calculatedDimension) : that.calculatedDimension == null;
    }

    @Override
    public int hashCode() {
        int result = realDimension != null ? realDimension.hashCode() : 0;
        result = 31 * result + (calculatedDimension != null ? calculatedDimension.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TemplateInfo{" +
                "realDimension=" + realDimension +
                ", calculatedDimension=" + calculatedDimension +
                ", realRatio=" + this.getRealDimensioRatio() +
                ", calculatedRatio= " + this.getCalculatedDimensioRatio() +
                ", offBy(R/C)=" + (100 - (100.0 *(this.getRealDimensioRatio() / this.getCalculatedDimensioRatio()))) + "%" +
                "}";
    }
}
