package model;

import org.opencv.core.Scalar;

import java.awt.*;

/**
 * Created by luiz on 12/06/17.
 */
public class TemplateInfo {

    private Double ratio;

    private Scalar color;

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Scalar getColor() {
        return color;
    }

    public void setColor(Scalar color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TemplateInfo that = (TemplateInfo) o;

        if (ratio != null ? !ratio.equals(that.ratio) : that.ratio != null) return false;
        return color != null ? color.equals(that.color) : that.color == null;
    }

    @Override
    public int hashCode() {
        int result = ratio != null ? ratio.hashCode() : 0;
        result = 31 * result + (color != null ? color.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TemplateInfo{" +
                "ratio=" + ratio +
                ", color=" + color +
                '}';
    }
}
