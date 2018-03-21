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

import com.byox.drawview.enums.BackgroundScale;
import com.byox.drawview.enums.BackgroundType;
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
    private ImageView undoImage, redoImage, applyImage, penImage, eraseImage, moveImage,
            textImage, drawAttrImage, figuresImage, backgroundImage;
    private TextView undoText, redoText, applyText, penText, eraseText, moveText, textText,
            drawAttrText, figuresText;
    private Bitmap bitmap;
    private String filepath;
    private OutputMediaFile outputMediaFile;
    private ImageView selectedImage;
    private TextView selectedText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        outputMediaFile = new OutputMediaFile(this);

        setLayouts();

        setListeners();

        try {

            String path = getIntent().getStringExtra("path");
            final String drawPath = getIntent().getStringExtra("drawPath");

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            //Set phone metrics
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, options);

            int brightness = getIntent().getIntExtra("brightness", 100);

            if (brightness != 100)
                bitmap = MainActivity.applyLightness(bitmap, brightness);

            myDrawView.post(new Runnable() {
                @Override
                public void run() {
                    try {

                        backgroundImage.setImageBitmap(bitmap);

                        if (drawPath != null)
                            myDrawView.setBackgroundImage(BitmapFactory.decodeFile(drawPath), BackgroundType.BITMAP, BackgroundScale.CENTER_CROP);
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
        lineButton = (LinearLayout) findViewById(R.id.lineButton);
        arrowButton = (LinearLayout) findViewById(R.id.arrowButton);
        rectangleButton = (LinearLayout) findViewById(R.id.rectangleButton);
        circleButton = (LinearLayout) findViewById(R.id.circleButton);
        ellipseButton = (LinearLayout) findViewById(R.id.ellipseButton);
        applyButton = findViewById(R.id.applyButton);

        undoImage = findViewById(R.id.undo_icon);
        redoImage = findViewById(R.id.redo_icon);
        applyImage = findViewById(R.id.apply_icon);
        penImage = findViewById(R.id.pen_icon);
        eraseImage = findViewById(R.id.erase_icon);
        moveImage = findViewById(R.id.move_icon);
        textImage = findViewById(R.id.text_icon);
        figuresImage = findViewById(R.id.figures_icon);

        undoText = findViewById(R.id.undo_text);
        redoText = findViewById(R.id.redo_text);
        applyText = findViewById(R.id.apply_text);
        penText = findViewById(R.id.pen_text);
        eraseText = findViewById(R.id.erase_text);
        moveText = findViewById(R.id.move_text);
        textText = findViewById(R.id.text_text);
        figuresText = findViewById(R.id.figures_text);

        backgroundImage = findViewById(R.id.backgroundImage);

        parentLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        drawAttrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDrawAttribs();
            }
        });

        penImage.setColorFilter(getResources().getColor(android.R.color.black));
        penText.setTextColor(getResources().getColor(android.R.color.black));

        selectedImage = penImage;
        selectedText = penText;

        pencilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.PEN);
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));

                selectedImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                selectedText.setTextColor(getResources().getColor(android.R.color.darker_gray));

                penImage.setColorFilter(getResources().getColor(android.R.color.black));
                penText.setTextColor(getResources().getColor(android.R.color.black));
                selectedImage = penImage;
                selectedText = penText;
            }
        });

        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.MOVE);
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));

                selectedImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                selectedText.setTextColor(getResources().getColor(android.R.color.darker_gray));

                moveImage.setColorFilter(getResources().getColor(android.R.color.black));
                moveText.setTextColor(getResources().getColor(android.R.color.black));
                selectedImage = moveImage;
                selectedText = moveText;
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
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));

                selectedImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                selectedText.setTextColor(getResources().getColor(android.R.color.darker_gray));

                eraseImage.setColorFilter(getResources().getColor(android.R.color.black));
                eraseText.setTextColor(getResources().getColor(android.R.color.black));
                selectedImage = eraseImage;
                selectedText = eraseText;
            }
        });

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDrawView.setDrawingMode(DrawingMode.TEXT);
                figuresImage.setImageResource(R.drawable.group);
                figuresText.setText(getResources().getString(R.string.figures));

                selectedImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                selectedText.setTextColor(getResources().getColor(android.R.color.darker_gray));

                textImage.setColorFilter(getResources().getColor(android.R.color.black));
                textText.setTextColor(getResources().getColor(android.R.color.black));
                selectedImage = textImage;
                selectedText = textText;
            }
        });

        figuresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invertRightLayout();

                selectedImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                selectedText.setTextColor(getResources().getColor(android.R.color.darker_gray));

                figuresImage.setColorFilter(getResources().getColor(android.R.color.black));
                figuresText.setTextColor(getResources().getColor(android.R.color.black));
                selectedImage = figuresImage;
                selectedText = figuresText;
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

            drawBitmap = (Bitmap) createCaptureResponse[0];

            file = outputMediaFile.getOutputMediaFile("MOVE", true);

            file.createNewFile();
            filepath = file.getAbsolutePath();
            FileOutputStream outputStream = new FileOutputStream(file);
            drawBitmap.compress(Bitmap.CompressFormat.PNG, 20, outputStream);
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
