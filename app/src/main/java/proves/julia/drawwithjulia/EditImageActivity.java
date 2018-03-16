package proves.julia.drawwithjulia;

import android.animation.LayoutTransition;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by julia on 2/14/18.
 */

public class EditImageActivity extends AppCompatActivity {

    private DrawView myDrawView;
    private FrameLayout parentLayout;
    private LinearLayout pencilButton, undoButton, redoButton, eraseButton, textButton,
            figuresButton, rightLayout, lineButton, arrowButton, rectangleButton,
            circleButton, ellipseButton, drawAttrButton, moveButton, curveButton, applyButton;
    private ImageView figuresImage, backgroundImage;
    private TextView figuresText;
    private Bitmap bitmap;
    private String filepath;
    private OutputMediaFile outputMediaFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        outputMediaFile = new OutputMediaFile(this);

        setLayouts();

        setListeners();

        try {

            filepath = getIntent().getStringExtra("path");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            //Set phone metrics
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(filepath, options);

            int brightness = getIntent().getIntExtra("brightness", 100);

            if (brightness != 100)
                bitmap = MainActivity.applyLightness(bitmap, brightness);

            myDrawView.post(new Runnable() {
                @Override
                public void run() {
                    try {

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
        moveButton = (LinearLayout) findViewById(R.id.moveButton);
        undoButton = (LinearLayout) findViewById(R.id.undoButton);
        redoButton = (LinearLayout) findViewById(R.id.redoButton);
        eraseButton = (LinearLayout) findViewById(R.id.eraseButton);
        textButton = (LinearLayout) findViewById(R.id.textButton);
        figuresButton = (LinearLayout) findViewById(R.id.figuresButton);
        curveButton = (LinearLayout) findViewById(R.id.curveButton);
        lineButton = (LinearLayout) findViewById(R.id.lineButton);
        arrowButton = (LinearLayout) findViewById(R.id.arrowButton);
        rectangleButton = (LinearLayout) findViewById(R.id.rectangleButton);
        circleButton = (LinearLayout) findViewById(R.id.circleButton);
        ellipseButton = (LinearLayout) findViewById(R.id.ellipseButton);
        applyButton = findViewById(R.id.applyButton);

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
            }
        });

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.MOVE);
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

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDraw();
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

        curveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.PEN);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));

            }
        });

        lineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.LINE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.line);
                figuresText.setText(getResources().getString(R.string.line));
            }
        });

        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.ARROW);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.arrow);
                figuresText.setText(getResources().getString(R.string.arrow));
            }
        });

        rectangleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.RECTANGLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.rectangle);
                figuresText.setText(getResources().getString(R.string.rectangle));
            }
        });

        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.CIRCLE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.circle);
                figuresText.setText(getResources().getString(R.string.circle));
            }
        });

        ellipseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.ELLIPSE);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.ellipse);
                figuresText.setText(getResources().getString(R.string.ellipse));
            }
        });
    }

    private void saveDraw() {
        try {
            Object[] createCaptureResponse = myDrawView.createCapture(DrawingCapture.BITMAP);
            Bitmap drawBitmap;
            File file;

            if (bitmap != null) {
                drawBitmap = overlay(bitmap, (Bitmap) createCaptureResponse[0]);
            } else {
                drawBitmap = (Bitmap) createCaptureResponse[0];
            }

            if (filepath != null)
                file = outputMediaFile.getOutputMediaFile(filepath, false);
            else
                file = outputMediaFile.getOutputMediaFile("DRW", true);

            file.createNewFile();
            filepath = file.getAbsolutePath();
            FileOutputStream outputStream = new FileOutputStream(file);
            drawBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            myDrawView.save();

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: ADD TO LOG
        }
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

    private void setListeners() {
        myDrawView.setOnDrawViewListener(new DrawView.OnDrawViewListener() {
            @Override
            public void onStartDrawing() {
                //canUndoRedo();
            }

            @Override
            public void onEndDrawing() {

            }

            @Override
            public void onClearDrawing() {

            }

            @Override
            public void onRequestText() {
                RequestTextDialog requestTextDialog =
                        RequestTextDialog.newInstance("");
                requestTextDialog.setOnRequestTextListener(new RequestTextDialog.OnRequestTextListener() {
                    @Override
                    public void onRequestTextConfirmed(String requestedText) {
                        myDrawView.refreshLastText(requestedText);
                    }

                    @Override
                    public void onRequestTextCancelled() {
                        myDrawView.cancelTextRequest();
                    }
                });
                requestTextDialog.show(getSupportFragmentManager(), "requestText");
            }

            @Override
            public void onAllMovesPainted() {

            }
        });
    }

    private static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    @Override
    public void onBackPressed() {
        final Intent intent = new Intent();

        if (!myDrawView.isSaved() && myDrawView.isDrawn()) {
            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            saveDraw();
                            setResult(RESULT_OK, intent);
                            intent.putExtra("filepath", filepath);
                            finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            setResult(RESULT_CANCELED, intent);
                            intent.putExtra("filepath", filepath);
                            finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.save_image)).setPositiveButton(getResources().getString(R.string.yes),
                    clickListener)
                    .setNegativeButton(getResources().getString(R.string.no), clickListener).show();

        } else if (myDrawView.isSaved()) {
            setResult(RESULT_OK, intent);
            intent.putExtra("filepath", filepath);
            finish();
        } else {
            setResult(RESULT_CANCELED, intent);
            intent.putExtra("filepath", filepath);
            finish();
        }
    }
}
