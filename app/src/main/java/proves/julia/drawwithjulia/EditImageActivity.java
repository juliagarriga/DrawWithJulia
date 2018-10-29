package proves.julia.drawwithjulia;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by julia on 2/14/18.
 */

public class EditImageActivity extends AppCompatActivity {

    private DrawView myDrawView;
    private FrameLayout parentLayout;
    private Button cancelEditButton, acceptEditButton;
    private LinearLayout pencilButton, undoButton, redoButton, eraseButton, textButton,
            figuresButton, rightLayout, lineButton, arrowButton, twoArrowButton, rectangleButton,
            circleButton, ellipseButton, drawAttrButton, moveButton, brightnessButton,
            saveButton, editLayout, buttonsLayout, seekBarButtonsLayout, exitButton, topLayout;
    private ImageView undoImage, redoImage, penImage, eraseImage, moveImage, exitImage,
            textImage, drawAttrImage, figuresImage, backgroundImage, brightnessImage, saveImage;
    private TextView undoText, redoText, penText, eraseText, moveText, textText,
            drawAttrText, figuresText, brightnessText, saveText, exitText;
    private SeekBar seekBar;
    private Bitmap bitmap;
    private String filepath, name;
    private OutputMediaFile outputMediaFile;
    private ImageView selectedImage;
    public static ImageButton deleteMoveButton;
    private TextView selectedText;
    private int brightnessProgress, savedBrightness, tempBrightnessProgress;
    private boolean brighten = false;
    private boolean newDraw = true;
    private boolean save = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        outputMediaFile = new OutputMediaFile(this);

        setLayouts();

        setListeners();

        try {

            filepath = getIntent().getStringExtra("image");
            name = getIntent().getStringExtra("name");
            //bitmap = BitmapFactory.decodeFile(path, options);

            if (filepath != null)
                myDrawView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Glide.with(EditImageActivity.this)
                                    .asBitmap()
                                    .load(filepath)
                                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true))
                                    .into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            bitmap = resource;
                                            backgroundImage.setImageBitmap(bitmap);

                                            brighten = true;

                                            brightnessImage.setColorFilter(getResources().getColor(android.R.color.black));
                                            brightnessText.setTextColor(getResources().getColor(android.R.color.black));
                                        }
                                    });

                            /*if (bitmap != null) {

                                undoImage.setColorFilter(getResources().getColor(R.color.buttons_tint3));
                                redoImage.setColorFilter(getResources().getColor(R.color.buttons_tint3));
                                applyImage.setColorFilter(getResources().getColor(R.color.buttons_tint3));

                                undoText.setTextColor(getResources().getColor(R.color.buttons_tint3));
                                redoText.setTextColor(getResources().getColor(R.color.buttons_tint3));
                                applyText.setTextColor(getResources().getColor(R.color.buttons_tint3));
                            }*/

                        } catch (NullPointerException e) {
                            brightnessButton.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            myDrawView.setBackgroundColor(Color.TRANSPARENT);


        } catch (NullPointerException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteMoveButton.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myDrawView.setDeleteCoord(deleteMoveButton.getLeft(),
                                deleteMoveButton.getTop(),
                                deleteMoveButton.getRight(),
                                deleteMoveButton.getBottom());

                        // Don't forget to remove your listener when you are done with it.
                        deleteMoveButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void setLayouts() {

        myDrawView = (DrawView) findViewById(R.id.draw_view);

        cancelEditButton = (Button) findViewById(R.id.cancelEditButton);
        acceptEditButton = (Button) findViewById(R.id.acceptEditButton);

        parentLayout = (FrameLayout) findViewById(R.id.parentLayout);
        rightLayout = (LinearLayout) findViewById(R.id.rightLayout);
        topLayout = findViewById(R.id.topLayout);
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
        twoArrowButton = (LinearLayout) findViewById(R.id.twoarrowButton);
        rectangleButton = (LinearLayout) findViewById(R.id.rectangleButton);
        circleButton = findViewById(R.id.circleButton);
        ellipseButton = findViewById(R.id.ellipseButton);
        saveButton = findViewById(R.id.saveButton);
        brightnessButton = findViewById(R.id.brightnessButton);
        exitButton = findViewById(R.id.exitButton);
        deleteMoveButton = findViewById(R.id.deleteMoveButton);

        undoImage = findViewById(R.id.undo_icon);
        redoImage = findViewById(R.id.redo_icon);
        brightnessImage = findViewById(R.id.brightness_icon);
        saveImage = findViewById(R.id.save_icon);
        penImage = findViewById(R.id.pen_icon);
        eraseImage = findViewById(R.id.erase_icon);
        moveImage = findViewById(R.id.move_icon);
        textImage = findViewById(R.id.text_icon);
        figuresImage = findViewById(R.id.figures_icon);
        exitImage = findViewById(R.id.exit_icon);

        undoText = findViewById(R.id.undo_text);
        redoText = findViewById(R.id.redo_text);
        saveText = findViewById(R.id.save_text);
        brightnessText = findViewById(R.id.brightness_text);
        penText = findViewById(R.id.pen_text);
        eraseText = findViewById(R.id.erase_text);
        moveText = findViewById(R.id.move_text);
        textText = findViewById(R.id.text_text);
        figuresText = findViewById(R.id.figures_text);
        exitText = findViewById(R.id.exit_text);

        seekBar = findViewById(R.id.seekBar);

        editLayout = findViewById(R.id.editLayout);
        seekBarButtonsLayout = findViewById(R.id.seekBarButtonsLayout);
        buttonsLayout = findViewById(R.id.buttonsLayout);

        backgroundImage = findViewById(R.id.backgroundImage);

        //parentLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

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

        undoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        undoImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                        undoText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        if (myDrawView.canUndo()) {
                            undoImage.setColorFilter(getResources().getColor(android.R.color.black));
                            undoText.setTextColor(getResources().getColor(android.R.color.black));
                            myDrawView.undo();
                            canUndoRedoSave();
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        redoButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        redoImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                        redoText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        if (myDrawView.canRedo()) {
                            redoImage.setColorFilter(getResources().getColor(android.R.color.black));
                            redoText.setTextColor(getResources().getColor(android.R.color.black));
                            myDrawView.redo();
                            canUndoRedoSave();
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });


        brightnessButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        brightnessImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                        brightnessText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        if (brighten) {
                            brightnessImage.setColorFilter(getResources().getColor(android.R.color.black));
                            brightnessText.setTextColor(getResources().getColor(android.R.color.black));
                            seekBar.setProgress(brightnessProgress);
                            //invertEditLayout();
                            toggleVisibilities(true);
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Glide.with(EditImageActivity.this)
                        .asBitmap()
                        .load(bitmap)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Canvas canvas = new Canvas(resource);

                                Paint paint = new Paint();
                                paint.setColorFilter(applyLightness(resource, brightnessProgress));
                                canvas.drawBitmap(bitmap, 0, 0, paint);
                                backgroundImage.setImageBitmap(resource);
                            }
                        });
                //invertEditLayout();
                toggleVisibilities(true);
            }
        });

        acceptEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brightnessProgress = tempBrightnessProgress;
                //invertEditLayout();
                toggleVisibilities(true);
                canUndoRedoSave();
            }
        });

        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        saveImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                        saveText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        if (save) {
                            saveImage.setColorFilter(getResources().getColor(android.R.color.black));
                            saveText.setTextColor(getResources().getColor(android.R.color.black));
                            saveDraw(false);
                        }
                        return true; // if you want to handle the touch event
                }
                return false;
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

        twoArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDrawView.setDrawingMode(DrawingMode.DRAW);
                myDrawView.setDrawingTool(DrawingTool.TWOARROW);
                invertRightLayout();
                figuresImage.setImageResource(R.drawable.twoarrow);
                figuresText.setText(getResources().getString(R.string.two_arrow));
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

        exitButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        exitImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                        exitText.setTextColor(getResources().getColor(android.R.color.darker_gray));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        exitImage.setColorFilter(getResources().getColor(android.R.color.black));
                        exitText.setTextColor(getResources().getColor(android.R.color.black));
                        exit();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        seekBar.setMax(200);
        brightnessProgress = savedBrightness = 100;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int i, boolean b) {
                tempBrightnessProgress = i;

                if (bitmap != null)
                    Glide.with(EditImageActivity.this)
                            .asBitmap()
                            .load(bitmap)
                            .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bmp, @Nullable Transition<? super Bitmap> transition) {
                                    Bitmap ret= Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.RGB_565);

                                    Canvas canvas = new Canvas(ret);

                                    Paint paint = new Paint();
                                    paint.setColorFilter(applyLightness(ret, i));
                                    canvas.drawBitmap(bitmap, 0, 0, paint);
                                    backgroundImage.setImageBitmap(ret);
                                }
                            });
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tempBrightnessProgress = brightnessProgress;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void saveFile(String filepath, final Bitmap bitmap) {

        try {

            File file = new File(filepath);
            file.createNewFile();
            final FileOutputStream outputStream = new FileOutputStream(file);

            Glide.with(EditImageActivity.this)
                    .asBitmap()
                    .load(bitmap)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Canvas canvas = new Canvas(resource);

                            Paint paint = new Paint();
                            paint.setColorFilter(applyLightness(resource, brightnessProgress));
                            canvas.drawBitmap(bitmap, 0, 0, paint);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        }
                    });
            myDrawView.save();
            savedBrightness = brightnessProgress;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDraw(final boolean finish) {

        final File file;
        if (filepath == null)
            file = outputMediaFile.getOutputMediaFile(name + "_DRW", true);
        else
            file = outputMediaFile.getOutputMediaFile(filepath, false);

        filepath = file.getAbsolutePath();

        Object[] createCaptureResponse = myDrawView.createCapture(DrawingCapture.BITMAP);

        final Bitmap drawBitmap;
        drawBitmap = (Bitmap) createCaptureResponse[0];
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(drawBitmap.getWidth(), drawBitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
        }

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveFile(filepath, overlay(bitmap, drawBitmap));
                        if (finish)
                            finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        final String[] filename = file.getName().split("\\.");
                        Random random = new Random();

                        saveFile(filepath.replace(filename[0], filename[0] + String.valueOf(random.nextInt(100))), overlay(bitmap, drawBitmap));
                        if (finish)
                            finish();
                        break;
                }
            }
        };

        if (file.exists() && newDraw) {
            newDraw = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.save_image)).setPositiveButton(getResources().getString(R.string.replace_image),
                    clickListener)
                    .setNegativeButton(getResources().getString(R.string.new_image), clickListener).show();
        } else {
            newDraw = false;
            saveFile(filepath, overlay(bitmap, drawBitmap));
            if (finish)
                finish();
        }
    }

    private static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bmp2, bmp1.getWidth(), bmp1.getHeight(), false), 0, 0, null);
        return bmOverlay;
    }

    private void invertRightLayout() {

        ViewGroup.LayoutParams params = rightLayout.getLayoutParams();

        params.height = params.height == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : 0;

        rightLayout.setLayoutParams(params);
    }

    public PorterDuffColorFilter applyLightness(final Bitmap bmp, int progress) {

        final PorterDuffColorFilter porterDuffColorFilter;

        if (progress > 100) {
            int value = (progress - 100) * 255 / 100;
            porterDuffColorFilter = new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        } else {
            int value = (100 - progress) * 255 / 100;
            porterDuffColorFilter = new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }

        return porterDuffColorFilter;

    }

    private void changeDrawAttribs() {
        DrawAttrDialog drawAttribsDialog = DrawAttrDialog.newInstance();
        drawAttribsDialog.setPaint(myDrawView.getPaintParameters(null));
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



                //Once entered, the user information must be saved to an internal, non-deleted source
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                        getString(R.string.preference_attributes), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.draw_color), newPaint.getColor());
                int paintStyle = 2;
                switch (newPaint.getStyle()) {
                    case FILL:
                        paintStyle = 0;
                        break;
                    case FILL_AND_STROKE:
                        paintStyle = 1;
                        break;
                    case STROKE:
                        paintStyle = 2;

                }

                editor.putInt(getString(R.string.draw_paint_style), paintStyle);
                editor.putBoolean(getString(R.string.draw_dither), newPaint.isDither());
                editor.putInt(getString(R.string.draw_alpha), newPaint.getAlpha());
                editor.putInt(getString(R.string.draw_width), (int) newPaint.getStrokeWidth());
                editor.putBoolean(getString(R.string.draw_anti_alias), newPaint.isAntiAlias());

                int cap = 0;
                switch (newPaint.getStrokeCap()) {
                    case BUTT:
                        cap = 0;
                        break;
                    case ROUND:
                        cap = 1;
                        break;
                    case SQUARE:
                        cap = 2;
                        break;
                }

                editor.putInt(getString(R.string.draw_corners), cap);

                int typeface = 0;

                if (newPaint.getTypeface() == Typeface.MONOSPACE)
                    typeface = 1;
                else if (newPaint.getTypeface() == Typeface.SANS_SERIF)
                    typeface = 2;
                else if (newPaint.getTypeface() == Typeface.SERIF)
                    typeface = 3;

                editor.putInt(getString(R.string.draw_typeface), typeface);
                editor.putInt(getString(R.string.font_size), (int) newPaint.getTextSize());
                editor.commit();
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
                if (myDrawView.getDrawingMode() == DrawingMode.MOVE) {
                    invertLayouts(true);
                }
            }

            @Override
            public void onDeleteDrawing(boolean toDelete) {

                if (toDelete)
                    deleteMoveButton.setImageDrawable(getResources().getDrawable(R.drawable.red_trash));
                else
                    deleteMoveButton.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon2));

            }

            @Override
            public void onEndDrawing() {
                deleteMoveButton.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon2));
                invertLayouts(false);
                canUndoRedoSave();
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
                requestTextDialog.show(getFragmentManager(), "requestText");
            }

            @Override
            public void onAllMovesPainted() {

            }
        });
    }

    private void canUndoRedoSave() {

        if (myDrawView.canUndo()) {
            undoImage.setColorFilter(getResources().getColor(android.R.color.black));
            undoText.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            undoImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
            undoText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }

        if (myDrawView.canRedo()) {
            redoImage.setColorFilter(getResources().getColor(android.R.color.black));
            redoText.setTextColor(getResources().getColor(android.R.color.black));
        } else {
            redoImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
            redoText.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        if (((!myDrawView.isSaved() && myDrawView.isDrawn() && myDrawView.canUndo()) || brightnessProgress != savedBrightness)) {
            saveImage.setColorFilter(getResources().getColor(android.R.color.black));
            saveText.setTextColor(getResources().getColor(android.R.color.black));
            save = true;
        } else {
            saveImage.setColorFilter(getResources().getColor(android.R.color.darker_gray));
            saveText.setTextColor(getResources().getColor(android.R.color.darker_gray));
            save = false;
        }
    }

    private boolean toggleVisibilities(boolean toToggle) {

        if (seekBarButtonsLayout.getVisibility() == View.VISIBLE) {

            myDrawView.setEnabled(true);
            seekBarButtonsLayout.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.VISIBLE);

            return false;

        } else if (toToggle) {
            myDrawView.setEnabled(false);
            seekBarButtonsLayout.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.GONE);

        }

        return true;
    }

    private void invertLayouts(boolean toDelete) {
        if (toDelete) {
            deleteMoveButton.setVisibility(View.VISIBLE);
        } else {
            deleteMoveButton.setVisibility(View.INVISIBLE);
        }
    }

    private void exit() {
        final Intent intent = new Intent();

        if (!myDrawView.isSaved() && (myDrawView.isDrawn() && myDrawView.canUndo()|| brightnessProgress != 100)) {
            DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            setResult(RESULT_OK, intent);
                            saveDraw(true);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            setResult(RESULT_CANCELED, intent);
                            finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.apply_changes)).setPositiveButton(getResources().getString(R.string.yes),
                    clickListener)
                    .setNegativeButton(getResources().getString(R.string.no), clickListener).show();

        } else if (myDrawView.isSaved()) {
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (toggleVisibilities(false))
            exit();
    }
}
