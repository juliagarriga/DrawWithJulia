package proves.julia.drawwithjulia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


import java.util.ArrayList;


public class CanvasView extends View {


    private Paint mPenPainter;
    public int width;

    public int height;


    private Bitmap mBitmap;
    private Canvas mCanvas;

    private Path mPath;

    Context context;

    private Paint mPaint;

    private float mX, mY;

    private static final float TOLERANCE = 5;
    private ArrayList<Path> paths = new ArrayList<Path>();
    private ArrayList<Path> undonePaths = new ArrayList<Path>();


    private int paintColor = 0xFF000000;

    public CanvasView(Context c, AttributeSet attrs) {

        super(c, attrs);

        context = c;


        // we set a new Path

        mPath = new Path();


        // and we set a new Paint with the desired attributes

        mPaint = new Paint();

        mPaint.setAntiAlias(true);

        mPaint.setColor(paintColor);

        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mPaint.setStrokeWidth(4f);


        //float mEraserWidth = getResources().getDimension(R.dimen.eraser_size);
        mPenPainter = new Paint();
        mPenPainter.setColor(Color.BLUE);

    }


    // override onSizeChanged
    @Override

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);


        // your Canvas will draw onto the defined Bitmap

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);

    }


    // override onDraw

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        for (Path p : paths) {
            canvas.drawPath(p, mPaint);
        }
        canvas.drawPath(mPath, mPaint);
        // paths.add(mPath);

    }

    private void startTouch(float x, float y) {

        undonePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);

        mX = x;

        mY = y;
    }

    public void onClickUndo() {
        if (paths.size() > 0) {
            undonePaths.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {
            //Util.Imageview_undo_redum_Status=false;
        }
        //toast the user
    }

    public void onClickRedo() {
        if (undonePaths.size() > 0) {
            paths.add(undonePaths.remove(undonePaths.size() - 1));
            invalidate();
        } else {
            // Util.Imageview_undo_redum_Status=false;
        }
        //toast the user
    }

    // when ACTION_MOVE move touch according to the x,y values

    private void moveTouch(float x, float y) {

        float dx = Math.abs(x - mX);

        float dy = Math.abs(y - mY);

        if (dx >= TOLERANCE || dy >= TOLERANCE) {

            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            mX = x;

            mY = y;

        }
    }

    public void setBitmap(Bitmap bitmap) {
        setBackgroundDrawable(new BitmapDrawable(bitmap));
    }


    private void upTouch() {


        mPath.lineTo(mX, mY);

        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        paths.add(mPath);
        mPath = new Path();
    }


    //override the onTouchEvent

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();

        float y = event.getY();
        float mCurX;
        float mCurY;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:


                mX = event.getX();

                mY = event.getY();


                startTouch(x, y);
                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:


                moveTouch(x, y);

                invalidate();

                break;

            case MotionEvent.ACTION_UP:

                upTouch();
                invalidate();

                break;
        }

        return true;

    }

}
