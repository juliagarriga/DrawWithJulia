package proves.julia.drawwithjulia;

/**
 * Created by julia on 02/08/16.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ActivityGallery extends Activity {


    private static ImageButton delButton;
    private static GalleryAdapter adapter;
    private static ArrayList<MyImage> images;
    private ImageButton addButton, add2Button;
    private RecyclerView recyclerView;
    private long service;
    private Uri uri;
    private String num_session;
    private String driver;

    private OutputMediaFile outputMediaFile;

    /**
     * Changes the states of the images
     * Ticked: image that will be deleted if the user presses the delete button
     * Visible: image that can be ticked
     *
     * @param isVisible Boolean that changes the visibility of the images
     */
    public static void setDelButton(boolean isVisible) {

        if (isVisible) {
            delButton.setVisibility(View.INVISIBLE);

            for (MyImage image : images) {
                image.setVisibility(false);
                image.setTicked(false);
            }
        } else {
            delButton.setVisibility(View.VISIBLE);
            Constants.DELETE_MODE = true;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        outputMediaFile = new OutputMediaFile(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        addButton = findViewById(R.id.add_image_button);

        // The bottom part of the activity (if not filled) is used as a button to initiate the camera
        add2Button = findViewById(R.id.add_button);

        delButton = findViewById(R.id.delete_image_button);

        String path = Utilitats.getWorkFolder(this, Utilitats.IMAGES).getPath();
        File files[] = new File(path).listFiles();

        // List of images to show in the gallery
        images = new ArrayList();

        if (files != null) {
            for (File file : files) {
                if (file.length() != 0L)
                    images.add(new MyImage(file));
                /*if (file.getName().contains("PIC")) {
                    images.add(new MyImage(file));
                }*/
            }
        }

        recyclerView = findViewById(R.id.gridView);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), Constants.NUM_VERTICAL_IMAGES);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GalleryAdapter(this, images);
        recyclerView.setAdapter(adapter);

        setButtons();

    }

    private void setButtons() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCamera();
            }
        });

        add2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCamera();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            //The user wants to delete the selected images
                            case DialogInterface.BUTTON_POSITIVE:
                                // Deletes the images selected
                                for (MyImage image : images) {
                                    if (image.isTicked())
                                        image.getFile().delete();
                                    else
                                        image.setVisibility(false);
                                }
                                // Once deleted, the paper has to be invisible again
                                delButton.setVisibility(View.INVISIBLE);
                                Constants.DELETE_MODE = false;
                                dataChanged();

                                break;
                            // The user does not want to delete the images
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                builder.setMessage(getResources().getString(R.string.delete_images)).setPositiveButton(getResources().getString(R.string.yes),
                        clickListener)
                        .setNegativeButton(getResources().getString(R.string.no), clickListener).show();

            }

        });
    }

    /**
     * The images list is cleared and refilled with the new changes
     */
    public void dataChanged() {
        String path = Utilitats.getWorkFolder(this, Utilitats.IMAGES).getPath();
        File files[] = new File(path).listFiles();
        images.clear();

        if (files != null) {
            for (File file : files) {
                if (file.length() != 0L)
                    images.add(new MyImage(file));
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void startCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        uri = outputMediaFile.getOutputMediaFileUri("");
        //If not saved, if the screen is rotated the uri happens to be null
        Constants.uri = uri;

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // start the image capture Intent
        startActivityForResult(intent, Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    /**
     * If the image taken is accepted, this function checks if it is correctly rotated.
     * It is compressed to a smaller size
     *
     * @param requestCode Used to know which activity's result we are receiving
     * @param resultCode  Used to know which is the activity result
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Constants.DELETE_SERVICE = true;
                OutputStream outStream = null;


                try {

                    Bitmap captureBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), Constants.uri);
                    File file = new File(Constants.uri.getPath());
                    outStream = new FileOutputStream(file);

                    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

                    Matrix matrix = new Matrix();
                    //If the image is not horizontal, if the width is bigger that the height means it is rotated 90º
                    if (captureBmp.getWidth() > captureBmp.getHeight() && display.getRotation() != Surface.ROTATION_90
                            && display.getRotation() != Surface.ROTATION_270)
                        matrix.postRotate(90);
                    Bitmap bitmap = Bitmap.createBitmap(captureBmp, 0, 0, captureBmp.getWidth(), captureBmp.getHeight(), matrix, true);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
                    outStream.flush();
                    outStream.close();

                } catch (Exception e) {
                    e.printStackTrace();

                }

                startCamera();

            }

            dataChanged();
        }
    }


}