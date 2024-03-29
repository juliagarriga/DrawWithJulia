package proves.julia.drawwithjulia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.byox.drawview.enums.DrawingCapture;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private static final int REQUEST_EDIT_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final String TAG = "MainActivity";

    private LinearLayout okButton, cancelButton, cameraButton, toolsButton,
            editLayout, brightnessButton, buttonsLayout, seekBarButtonsLayout;
    private Button cancelEditButton, acceptEditButton;
    private SeekBar seekBar;
    private PhotoView image;
    private Matrix originalMatrix;
    private Uri uri;
    private OutputMediaFile outputMediaFile;
    private Bitmap bitmap;
    private Bitmap drawBitmap;
    private String filepath, drawPath;
    private int brightnessProgress, tempBrightnessProgress;
    private boolean isModified;
    private ProgressBar progressBar;
    private boolean new_image;
    private boolean isFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputMediaFile = new OutputMediaFile(this);

        isModified = false;

        setLayouts();
    }

    private void setLayouts() {

        image = findViewById(R.id.image);

        okButton = (LinearLayout) findViewById(R.id.okButton);
        cancelButton = (LinearLayout) findViewById(R.id.cancelButton);
        cameraButton = (LinearLayout) findViewById(R.id.cameraButton);
        toolsButton = (LinearLayout) findViewById(R.id.toolsButton);
        brightnessButton = (LinearLayout) findViewById(R.id.brightnessButton);

        cancelEditButton = (Button) findViewById(R.id.cancelEditButton);
        acceptEditButton = (Button) findViewById(R.id.acceptEditButton);

        editLayout = (LinearLayout) findViewById(R.id.editLayout);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekBarButtonsLayout = (LinearLayout) findViewById(R.id.seekBarButtonsLayout);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        try {

            filepath = getIntent().getStringExtra("image");

            if (filepath != null) {

                new_image = false;

                /*bitmap = BitmapFactory.decodeFile(filepath);
                image.setImageBitmap(bitmap);*/
                Glide.with(this)
                        .asBitmap()
                        .load(filepath)
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                bitmap = resource;
                                image.setImageBitmap(bitmap);
                            }
                        });
                brightnessButton.setVisibility(View.VISIBLE);

            } else {
                new_image = true;
                brightnessButton.setVisibility(View.GONE);
            }

        } catch (NullPointerException e) {

            brightnessButton.setVisibility(View.GONE);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brightnessProgress != 100 || drawBitmap != null) {
                    saveDraw();
                } else {
                    finish();
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        toolsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
                intent.putExtra("path", filepath);
                intent.putExtra("drawPath", drawPath);
                intent.putExtra("brightness", brightnessProgress);
                startActivityForResult(intent, REQUEST_EDIT_IMAGE);
            }
        });

        brightnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(brightnessProgress);
                invertEditLayout();
                toggleVisibilities(true);
            }
        });

        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image.setImageBitmap(applyLightness(bitmap, brightnessProgress));
                invertEditLayout();
                toggleVisibilities(true);
            }
        });

        acceptEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brightnessProgress = tempBrightnessProgress;
                invertEditLayout();
                toggleVisibilities(true);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {
                    File file = new File(filepath);
                    file.delete();
                    if (filepath.contains("DRW")) {
                        file = new File(filepath.replace("DRW", "PIC"));
                        file.delete();
                    }
                }
                Intent intent = new Intent(MainActivity.this, ActivityGallery.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        seekBar.setMax(200);
        brightnessProgress = 100;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tempBrightnessProgress = i;
                image.setImageBitmap(applyLightness(bitmap, i));

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

    private void saveFile(String filepath) {

        try {

            File file = new File(filepath);
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDraw() {

        File file;
        if (filepath == null)
            file = outputMediaFile.getOutputMediaFile("DRW", true);
        else
            file = outputMediaFile.getOutputMediaFile(filepath, false);


        filepath = file.getAbsolutePath();

        final Intent intent = new Intent(MainActivity.this, ActivityGallery.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveFile(filepath);
                        startActivity(intent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        filepath += "1";
                        saveFile(filepath);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        };

        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.save_image)).setPositiveButton(getResources().getString(R.string.replace_image),
                    clickListener)
                    .setNegativeButton(getResources().getString(R.string.new_image), clickListener).show();
        } else {
            saveFile(filepath);
            startActivity(intent);
            finish();
        }
    }

    public static Bitmap applyLightness(Bitmap bmp, int progress) {

        PorterDuffColorFilter porterDuffColorFilter;

        if (progress > 100) {
            int value = (progress - 100) * 255 / 100;
            porterDuffColorFilter = new PorterDuffColorFilter(Color.argb(value, 255, 255, 255), PorterDuff.Mode.SRC_OVER);
        } else {
            int value = (100 - progress) * 255 / 100;
            porterDuffColorFilter = new PorterDuffColorFilter(Color.argb(value, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);
        }

        Bitmap ret = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        Canvas canvas = new Canvas(ret);

        Paint paint = new Paint();
        paint.setColorFilter(porterDuffColorFilter);
        canvas.drawBitmap(bmp, 0, 0, paint);

        return ret;

    }

    private void invertEditLayout() {

        ViewGroup.LayoutParams params = editLayout.getLayoutParams();

        params.height = params.height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;

        editLayout.setLayoutParams(params);
    }

    private void startCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        uri = outputMediaFile.getOutputMediaFileUri("PIC");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp2.getWidth(), bmp2.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(Bitmap.createScaledBitmap(bmp1, bmp2.getWidth(), bmp2.getHeight(), false), new Matrix(), null);
        canvas.drawBitmap(bmp2, 0, 0, null);
        return bmOverlay;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT_IMAGE) {

            try {
                String path = data.getStringExtra("filepath");

                if (resultCode == RESULT_OK) {

                    if (drawPath != null) {
                        drawBitmap = overlay(BitmapFactory.decodeFile(drawPath), BitmapFactory.decodeFile(path));
                        OutputStream outStream = new FileOutputStream(new File(drawPath));
                        drawBitmap.compress(Bitmap.CompressFormat.PNG, 20, outStream);
                        outStream.flush();
                        outStream.close();
                    } else {
                        drawPath = path;
                        drawBitmap = BitmapFactory.decodeFile(drawPath);
                    }
                    if (bitmap == null) {
                        bitmap = Bitmap.createBitmap(drawBitmap.getWidth(), drawBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawColor(Color.WHITE);
                    }
                    bitmap = overlay(bitmap, drawBitmap);
                    image.setImageBitmap(applyLightness(bitmap, brightnessProgress));

                } else if (resultCode == RESULT_CANCELED) {
                    // if the image was saved
                    if (path != null) {
                        File file = new File(path);
                        file.delete();
                    }
                }
            } catch (NullPointerException e) {

                e.printStackTrace();

            } catch (IOException e2) {

                e2.printStackTrace();

            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            filepath = uri.getPath();

            Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            bitmap = resource;

                            if (drawBitmap != null)
                                bitmap = overlay(bitmap, drawBitmap);

                            image.setImageBitmap(bitmap);
                        }
                    });

            brightnessProgress = 100;
            brightnessButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (toggleVisibilities(false))
            if (editLayout.getLayoutParams().height != 0)
                invertEditLayout();
            else {
                DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                saveDraw();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                if (filepath != null && new_image) {
                                    File file = new File(filepath);
                                    file.delete();
                                    if (filepath.contains("DRW")) {
                                        file = new File(filepath.replace("DRW", "PIC"));
                                        file.delete();
                                    }
                                }
                                finish();
                                break;
                        }
                    }
                };
                if (drawBitmap != null || (new_image && filepath != null) || brightnessProgress != 100) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.save_image)).setPositiveButton(getResources().getString(R.string.yes),
                            clickListener)
                            .setNegativeButton(getResources().getString(R.string.no), clickListener).show();

                } else {
                    super.onBackPressed();
                }
            }
    }

    private boolean toggleVisibilities(boolean toToggle) {

        if (seekBarButtonsLayout.getVisibility() == View.VISIBLE) {

            seekBarButtonsLayout.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.VISIBLE);

            return false;

        } else if (toToggle) {

            seekBarButtonsLayout.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.GONE);

        }

        return true;
    }
}
