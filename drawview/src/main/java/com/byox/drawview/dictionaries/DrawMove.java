package com.byox.drawview.dictionaries;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.utils.SerializableMatrix;
import com.byox.drawview.utils.SerializablePaint;
import com.byox.drawview.utils.SerializablePath;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ing. Oscar G. Medina Cruz on 07/11/2016.
 * <p>
 * Dictionary class that save move for draw in the view, this allow the user to make a history
 * of the user movements in the view and make a redo/undo.
 *
 * @author Ing. Oscar G. Medina Cruz
 */

public class DrawMove implements Serializable {

    private SerializablePaint mPaint;
    private int originalColor;
    private DrawingMode mDrawingMode = null;
    private DrawingTool mDrawingTool = null;
    private List<Move> mDrawingMovesList;
    private int mDrawingMovesListIndex = -1;
    private String mText;
    private SerializableMatrix mBackgroundMatrix;
    private byte[] mBackgroundImage;
    private List<DrawMove> movedMoves;     // only for DrawingMode MOVE
    private float tempX, tempY;

    // METHODS
    public DrawMove() {

        mDrawingMovesList = new ArrayList<>();
        movedMoves = new ArrayList<>();

        addMove();

    }

    // GETTERS

    public SerializablePaint getPaint() {
        return mPaint;
    }

    public void resetColor() {
        mPaint.setColor(originalColor);
    }

    public DrawingMode getDrawingMode() {
        return mDrawingMode;
    }

    public DrawingTool getDrawingTool() {
        return mDrawingTool;
    }

    public SerializablePath getDrawingPath() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getPath();
    }

    public float getStartX() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getStartX();
    }

    public float getStartY() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getStartY();
    }

    public float getEndX() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getEndX();
    }

    public float getEndY() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getEndY();
    }

    public String getText() {
        return mText;
    }

    public SerializableMatrix getBackgroundMatrix(){
        return mBackgroundMatrix;
    }

    public byte[] getBackgroundImage() {
        return mBackgroundImage;
    }

    // SETTERS

    public void setPaint(SerializablePaint paint) {
        mPaint = paint;
        originalColor = paint.getColor();
    }

    public void setDrawingMode(DrawingMode drawingMode) {
        mDrawingMode = drawingMode;
    }

    public void setDrawingTool(DrawingTool drawingTool) {
        mDrawingTool = drawingTool;
    }

    public void setDrawingPathList(SerializablePath drawingPath) {
        mDrawingMovesList.get(mDrawingMovesListIndex).setPath(drawingPath);
    }

    public void setStartX(float startX) {
        mDrawingMovesList.get(mDrawingMovesListIndex).setStartX(startX);
    }

    public void setStartY(float startY) {
        mDrawingMovesList.get(mDrawingMovesListIndex).setStartY(startY);
    }

    public void setTemp(float tempX, float tempY) {
        this.tempX = tempX;
        this.tempY = tempY;
    }

    public float[] getTemp() {
        return new float[]{tempX, tempY};
    }

    public void setEndX(float endX) {
        mDrawingMovesList.get(mDrawingMovesListIndex).setEndX(endX);
    }

    public void setEndY(float endY) {
        mDrawingMovesList.get(mDrawingMovesListIndex).setEndY(endY);
    }

    public void setText(String text) {
        mText = text;
    }

    public void setBackgroundImage(byte[] backgroundImage, SerializableMatrix backgroundMatrix) {
        mBackgroundImage = backgroundImage;
        mBackgroundMatrix = backgroundMatrix;
    }

    public void addPoint(float x, float y) {
        mDrawingMovesList.get(mDrawingMovesListIndex).addPoint(x, y);
    }

    public void addMove() {

        if (mDrawingMovesListIndex >= -1 &&
                mDrawingMovesListIndex < mDrawingMovesList.size() - 1)
            mDrawingMovesList = mDrawingMovesList.subList(0, mDrawingMovesListIndex + 1);

        Move move = new Move();

        mDrawingMovesList.add(move);

        mDrawingMovesListIndex++;
    }

    public void clearPoints() {
        mDrawingMovesList.get(mDrawingMovesListIndex).clearPoints();
    }

    public List<Float> getmPointsX() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getPointsX();
    }

    public List<Float> getmPointsY() {
        return mDrawingMovesList.get(mDrawingMovesListIndex).getPointsY();
    }

    public void addMove(DrawMove move) {
        movedMoves.add(move);
    }

    public List<DrawMove> getMovedMoves() {
        return movedMoves;
    }

    public void undo() {
        mDrawingMovesListIndex--;
    }

    public void redo() {
        mDrawingMovesListIndex++;
    }
}
