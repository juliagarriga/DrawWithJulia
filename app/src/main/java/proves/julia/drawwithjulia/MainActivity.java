package proves.julia.drawwithjulia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class MainActivity extends Activity {

    private LinearLayout okButton, cancelButton, cameraButton, editButton;
    private ImageView image;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Matrix originalMatrix;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri uri;
    private OutputMediaFile outputMediaFile;
    private Bitmap bitmap;
    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputMediaFile = new OutputMediaFile(this);

        setLayouts();
    }

    private void setLayouts() {

        image = findViewById(R.id.image);

        okButton = findViewById(R.id.okButton);
        editButton = findViewById(R.id.editButton);
        cancelButton = findViewById(R.id.cancelButton);
        cameraButton = findViewById(R.id.cameraButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                Intent intent = new Intent(MainActivity.this, EditImageActivity.class);
                intent.putExtra("path", filepath);
                startActivity(intent);

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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
                if(captureBmp.getWidth()>captureBmp.getHeight() && display.getRotation()!=Surface.ROTATION_90
                        && display.getRotation()!=Surface.ROTATION_270)
                    matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(captureBmp, 0, 0, captureBmp.getWidth(), captureBmp.getHeight(), matrix, true);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                outStream.flush();
                outStream.close();

                image.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    }
}
