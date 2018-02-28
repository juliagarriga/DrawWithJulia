package com.byox.drawview.dictionaries;

import com.byox.drawview.utils.SerializablePath;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julia on 2/27/18.
 */

public class Move {

    private SerializablePath path;
    private float startX, startY, endX, endY;
    private List<Float> pointsX, pointsY;

    public Move() {
        pointsX = new ArrayList<>();
        pointsY = new ArrayList<>();
    }

    public SerializablePath getPath() {
        return path;
    }

    public void setPath(SerializablePath path) {
        this.path = path;
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
}
