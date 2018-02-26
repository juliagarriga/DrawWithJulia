package proves.julia.drawwithjulia;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by julia on 2/14/18.
 */

public class EditImageActivity extends AppCompatActivity {

    private DrawView myDrawView;
    private FrameLayout parentLayout;
    private LinearLayout pencilButton, undoButton, redoButton, eraseButton, textButton,
            figuresButton, rightLayout, lineButton, arrowButton, rectangleButton,
            circleButton, ellipseButton, drawAttrButton;
    private ImageView figuresImage, backgroundImage;
    private TextView figuresText;
    private Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        setLayouts();

        try {

            final String filepath = getIntent().getStringExtra("path");

            myDrawView.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        //Set phone metrics
                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

                        options.inSampleSize = 2;
                        options.inJustDecodeBounds = false;

                        bitmap = BitmapFactory.decodeFile(filepath, options);

                        backgroundImage.setImageBitmap(bitmap);
                        myDrawView.setBackgroundColor(Color.TRANSPARENT);

                    } catch (NullPointerException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setLayouts() {

        myDrawView = (DrawView) findViewById(R.id.draw_view);

        parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        drawAttrButton = (LinearLayout) findViewById(R.id.drawAttrButton);
        pencilButton = (LinearLayout) findViewById(R.id.pencilButton);
        undoButton = (LinearLayout) findViewById(R.id.undoButton);
        redoButton = (LinearLayout) findViewById(R.id.redoButton);
        eraseButton = (LinearLayout) findViewById(R.id.eraseButton);
        textButton = (LinearLayout) findViewById(R.id.textButton);
        figuresButton = (LinearLayout) findViewById(R.id.figuresButton);
        lineButton = (LinearLayout) findViewById(R.id.lineButton);
        arrowButton = (LinearLayout) findViewById(R.id.arrowButton);
        rectangleButton = (LinearLayout) findViewById(R.id.rectangleButton);
        circleButton = (LinearLayout) findViewById(R.id.circleButton);
        ellipseButton = (LinearLayout) findViewById(R.id.ellipseButton);

        figuresImage = (ImageView) findViewById(R.id.figuresImage);
        figuresText = (TextView) findViewById(R.id.figuresText);

        backgroundImage = (ImageView) findViewById(R.id.backgroundImage);

        parentLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        drawAttrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDrawAttribs();
            }
        });

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.PEN);
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));
            }
        });

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For make Undo action
                if (myDrawView.canUndo())
                    myDrawView.undo();

            }
        });

        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // For make Redo action
                if (myDrawView.canRedo())
                    myDrawView.redo();
            }
        });

        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.ERASER);
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.TEXT);
            }
        });

        figuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invertRightLayout();
            }
        });

        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.LINE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.line);
                figuresText.setText(getResources().getString(R.string.line));
            }
        });

        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.ARROW);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.arrow);
                figuresText.setText(getResources().getString(R.string.arrow));
            }
        });

        rectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.RECTANGLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.rectangle);
                figuresText.setText(getResources().getString(R.string.rectangle));
            }
        });

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.CIRCLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.circle);
                figuresText.setText(getResources().getString(R.string.circle));
            }
        });

        ellipseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingTool(DrawingTool.ELLIPSE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.ellipse);
                figuresText.setText(getResources().getString(R.string.ellipse));
            }
        });
    }

    private void invertRightLayout() {

        ViewGroup.LayoutParams params = rightLayout.getLayoutParams();

        params.height = params.height == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : 0;

        rightLayout.setLayoutParams(params);
    }

    private void changeDrawAttribs() {
        DrawAttrDialog drawAttribsDialog = DrawAttrDialog.newInstance();
        drawAttribsDialog.setPaint(myDrawView.getCurrentPaintParams());
        drawAttribsDialog.setOnCustomViewDialogListener(new DrawAttrDialog.OnCustomViewDialogListener() {
            @Override
            public void onRefreshPaint(Paint newPaint) {
                myDrawView.setDrawColor(newPaint.getColor())
                        .setPaintStyle(newPaint.getStyle())
                        .setDither(newPaint.isDither())
                        .setDrawWidth((int) newPaint.getStrokeWidth())
                        .setDrawAlpha(newPaint.getAlpha())
                        .setAntiAlias(newPaint.isAntiAlias())
                        .setLineCap(newPaint.getStrokeCap())
                        .setFontFamily(newPaint.getTypeface())
                        .setFontSize(newPaint.getTextSize());
//                If you prefer, you can easily refresh new attributes using this method
//                mDrawView.refreshAttributes(newPaint);
            }
        });
        drawAttribsDialog.show(getSupportFragmentManager(), "drawAttribs");

    }
}
