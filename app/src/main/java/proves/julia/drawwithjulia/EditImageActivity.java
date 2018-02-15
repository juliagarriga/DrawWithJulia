package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by julia on 2/14/18.
 */

public class EditImageActivity extends Activity {

    private MyDrawView myDrawView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        Bitmap bitmap;

        myDrawView = findViewById(R.id.image);

        try {

            String filepath = getIntent().getStringExtra("path");

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            //Set phone metrics
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            options.inSampleSize = 2;
            options.inJustDecodeBounds = false;


            bitmap = BitmapFactory.decodeFile(filepath, options);

            myDrawView.setImageBitmap(bitmap);

        } catch (NullPointerException e) {

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
