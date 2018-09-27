package com.byox.drawview.dictionaries;

import android.graphics.Color;
import android.support.annotation.ColorInt;

import com.byox.drawview.utils.SerializablePaint;
import com.byox.drawview.utils.SerializablePath;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julia on 2/27/18.
 */

public class Move implements Serializable {

    private SerializablePath path;
    private SerializablePaint paint;
    private float startX, startY, endX, endY;
    private List<Float> pointsX, pointsY;

    public Move(SerializablePaint paint) {
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
        this.paint = paint;
    }

    public SerializablePath getPath() {
        return path;
    }

    public void setPath(SerializablePath path) {
        this.path = path;
    }

    public SerializablePaint getPaint() {
        int color = paint.getColor();
        return paint;
    }

    public void setPaint(SerializablePaint paint) {
        this.paint = paint;
    }

    public float getStartX() {
        return startX;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public float getStartY() {
        return startY;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public void addPoint(float pointX, float pointY) {
        pointsX.add(pointX);
        pointsY.add(pointY);
    }

    public List<Float> getPointsX() {
        return pointsX;
    }

    public List<Float> getPointsY() {
        return pointsY;
    }

    public void clearPoints() {
        pointsX.clear();
        pointsY.clear();
    }
}
