package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = "MainActivity";

    private LinearLayout okButton, cancelButton, cameraButton, filterButton, toolsButton,
            editLayout, contrastButton, brightnessButton, adjustButton, configLayout,
            buttonsLayout, seekBarButtonsLayout;
    private Button cancelEditButton, acceptEditButton;
    private SeekBar seekBar;
    private ImageView image;
    private Matrix originalMatrix;
    private Uri uri;
    private OutputMediaFile outputMediaFile;
    private Bitmap bitmap;
    private String filepath;
    private Edits actualEdit = Edits.NONE;
    private int contrastProgress, brightnessProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputMediaFile = new OutputMediaFile(this);

        setLayouts();
    }

    private void setLayouts() {

        image = (ImageView) findViewById(R.id.image);

        okButton = (LinearLayout) findViewById(R.id.okButton);
        filterButton = (LinearLayout) findViewById(R.id.editButton);
        cancelButton = (LinearLayout) findViewById(R.id.cancelButton);
        cameraButton = (LinearLayout) findViewById(R.id.cameraButton);
        toolsButton = (LinearLayout) findViewById(R.id.toolsButton);
        contrastButton = (LinearLayout) findViewById(R.id.contrastButton);
        brightnessButton = (LinearLayout) findViewById(R.id.brightnessButton);
        adjustButton = (LinearLayout) findViewById(R.id.adjustButton);

        cancelEditButton = (Button) findViewById(R.id.cancelEditButton);
        acceptEditButton = (Button) findViewById(R.id.acceptEditButton);

        editLayout = (LinearLayout) findViewById(R.id.editLayout);
        configLayout = (LinearLayout) findViewById(R.id.configLayout);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekBarButtonsLayout = (LinearLayout) findViewById(R.id.seekBarButtonsLayout);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        try {

            filepath = getIntent().getStringExtra("image");

            if (filepath != null) {

                bitmap = BitmapFactory.decodeFile(filepath);
                image.setImageBitmap(bitmap);
                filterButton.setVisibility(View.VISIBLE);

            } else {

                filterButton.setVisibility(View.GONE);
            }

        } catch (NullPointerException e) {

            filterButton.setVisibility(View.GONE);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityGallery.class);
                startActivity(intent);
                //finish();
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
                try {
                    FileOutputStream out = new FileOutputStream(filepath);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {

                }
                Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
                intent.putExtra("path", filepath);
                startActivity(intent);

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                invertEditLayout();
            }
        });

        contrastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(contrastProgress);
                toggleVisibilities(true);
                actualEdit = Edits.CONTRAST;
            }
        });

        brightnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(brightnessProgress);
                toggleVisibilities(true);
                actualEdit = Edits.BRIGHTNESS;
            }
        });

        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibilities(true);
                actualEdit = Edits.ADJUST;
            }
        });

        cancelEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibilities(true);
            }
        });

        acceptEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibilities(true);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityGallery.class);
                startActivity(intent);
            }
        });

        seekBar.setMax(200);
        contrastProgress = 100;
        brightnessProgress = 100;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch (actualEdit) {
                    case CONTRAST:
                        contrastProgress = i;
                        image.setImageBitmap(applyContrast(bitmap, i));
                        break;

                    case BRIGHTNESS:
                        brightnessProgress = i;
                        image.setImageBitmap(applyLightness(bitmap, i));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private Bitmap applyLightness(Bitmap bmp, int progress) {

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

    private Bitmap applyContrast(Bitmap bmp, int progress) {

        return null;

        /*addFragmentToStack(ContrastFragment.create(filepath, new OnContrastListener() {
            @Override
            public void onContrastPhotoCompleted(String s) {

            }
        }));*/
    }

    /*private Bitmap increaseBrightness(Bitmap bitmap, int value) {

        Utils.bitmapToMat(bitmap, imageMat);
        imageMat.convertTo(imageMat, -1, 1, value);
        Bitmap result = Bitmap.createBitmap(imageMat.cols(), imageMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(imageMat, result);
        return result;
    }*/

    private void invertEditLayout() {

        ViewGroup.LayoutParams params = editLayout.getLayoutParams();

        params.height = params.height == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : 0;

        editLayout.setLayoutParams(params);
    }

    private void startCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        uri = outputMediaFile.getOutputMediaFileUri("prova");

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                outStream.flush();
                outStream.close();

                image.setImageBitmap(bitmap);
                filterButton.setVisibility(View.VISIBLE);

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

        if (seekBar.getVisibility() == View.VISIBLE) {

            seekBar.setVisibility(View.GONE);
            seekBarButtonsLayout.setVisibility(View.GONE);
            configLayout.setVisibility(View.VISIBLE);
            buttonsLayout.setVisibility(View.VISIBLE);

            return false;

        } else if (toToggle) {

            seekBar.setVisibility(View.VISIBLE);
            seekBarButtonsLayout.setVisibility(View.VISIBLE);
            configLayout.setVisibility(View.GONE);
            buttonsLayout.setVisibility(View.GONE);

        }

        return true;
    }

    private enum Edits {
        NONE,
        CONTRAST,
        BRIGHTNESS,
        ADJUST
    }


}
