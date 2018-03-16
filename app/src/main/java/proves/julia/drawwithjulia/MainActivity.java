package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.SeekBar;

import com.byox.drawview.enums.DrawingCapture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final String TAG = "MainActivity";

    private LinearLayout okButton, cancelButton, cameraButton, toolsButton,
            editLayout, brightnessButton, buttonsLayout, seekBarButtonsLayout;
    private Button cancelEditButton, acceptEditButton;
    private SeekBar seekBar;
    private ZoomableImageView image;
    private Matrix originalMatrix;
    private Uri uri;
    private OutputMediaFile outputMediaFile;
    private Bitmap bitmap;
    private String filepath;
    private int brightnessProgress;
    private boolean isModified;

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

                bitmap = BitmapFactory.decodeFile(filepath);
                image.setImageBitmap(bitmap);
                brightnessButton.setVisibility(View.VISIBLE);

            } else {

                brightnessButton.setVisibility(View.GONE);
            }

        } catch (NullPointerException e) {

            brightnessButton.setVisibility(View.GONE);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (brightnessProgress != 100 || image.isZoomed())
                    saveDraw();
                Intent intent = new Intent(MainActivity.this, ActivityGallery.class);
                startActivity(intent);
                finish();
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
                brightnessProgress = 100;
                image.setImageBitmap(applyLightness(bitmap, brightnessProgress));
                invertEditLayout();
                toggleVisibilities(true);
            }
        });

        acceptEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                startActivity(intent);
                finish();
            }
        });

        seekBar.setMax(200);
        brightnessProgress = 100;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brightnessProgress = i;
                image.setImageBitmap(applyLightness(bitmap, i));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void saveDraw() {
        try {

            File file = outputMediaFile.getOutputMediaFile(filepath, false);
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap = image.getPhotoBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        } catch (IOException e) {
            e.printStackTrace();
            // TODO: ADD TO LOG
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT_IMAGE) {
            try {
                String path = data.getStringExtra("filepath");

                if (resultCode == RESULT_OK) {
                    filepath = path;
                    bitmap = BitmapFactory.decodeFile(filepath);
                    image.setImageBitmap(bitmap);
                } else if (resultCode == RESULT_CANCELED) {
                    // if the image was saved
                    if (path != null) {
                        File file = new File(path);
                        file.delete();
                    }
                }
            } catch (NullPointerException e) {

            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {

                Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                File file = new File(uri.getPath());
                filepath = file.getAbsolutePath();
                OutputStream outStream = new FileOutputStream(file);

                Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                Matrix matrix = new Matrix();
                //If the image is not horizontal, if the width is bigger that the height means it is rotated 90º
                if (captureBmp.getWidth() > captureBmp.getHeight() && display.getRotation() != Surface.ROTATION_90
                        && display.getRotation() != Surface.ROTATION_270)
                    matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(captureBmp, 0, 0, captureBmp.getWidth(), captureBmp.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.PNG, 60, outStream);
                outStream.flush();
                outStream.close();

                image.setImageBitmap(bitmap);
                brightnessButton.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }

    @Override
    public void onBackPressed() {

        if (toggleVisibilities(false))
            if (editLayout.getLayoutParams().height != 0)
                invertEditLayout();
            else
                super.onBackPressed();
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
