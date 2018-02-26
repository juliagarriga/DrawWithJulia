package proves.julia.drawwithjulia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class MyDrawView extends PhotoView {
    public Bitmap  mBitmap;
    public Canvas  mCanvas;
    private Path    mPath;
    private Paint   mBitmapPaint;
    private Paint   mPaint;
    private boolean touchable;
    private boolean isTouched;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();


    public MyDrawView(Context c, AttributeSet attrs) {
        super(c, attrs);

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        touchable = false;
        isTouched  = false;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(9);

    }

    public MyDrawView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);

        mPath = new Path();

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        touchable = false;
        isTouched = false;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /*if (w <= 0 || h <= 0)
            mBitmap = Bitmap.createBitmap(250, 250, Bitmap.Config.ARGB_8888);
        else
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        mCanvas = new Canvas(mBitmap);
        if (touchable) {

            for (Path p : paths){
                canvas.drawPath(p, mPaint);
                mCanvas.drawPath(p, mPaint);
            }

            canvas.drawPath(mPath, mPaint);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        undonePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        //mCanvas.drawPath(mPath, mPaint);
        // save to the list of drawn paths
        paths.add(mPath);
        // kill this so we don't double draw
        mPath.reset();
    }

    public void onClickUndo () {
        if (paths.size()>0) {
            undonePaths.add(paths.remove(paths.size()-1));
            invalidate();
        }
        //toast the user
    }

    public void onClickRedo (){

        if (undonePaths.size()>0) {
            paths.add(undonePaths.remove(undonePaths.size()-1));
            invalidate();
        }
        //toast the user
    }

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    public boolean isTouched() {
        return isTouched;
    }

    public void unTouch() {
        isTouched = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        isTouched = true;

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public Bitmap getBitmap()
    {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {

        this.setImageBitmap(bitmap);
        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas(mBitmap);
    }



    public void clear(){

        mBitmap.eraseColor(Color.GREEN);
        invalidate();
        System.gc();

    }

}