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
    private ImageButton addButton;
    private RecyclerView recyclerView;

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

        delButton = findViewById(R.id.delete_image_button);

        String path = Utilitats.getWorkFolder(this, Utilitats.IMAGES).getPath();
        File files[] = new File(path).listFiles();

        // List of images to show in the gallery
        images = new ArrayList();

        if (files != null) {
            for (File file : files) {
                if (file.length() != 0L)
                    if (file.getName().contains("PIC") || file.getName().contains("DRW"))
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

                Intent intent = new Intent(ActivityGallery.this, MainActivity.class);

                startActivity(intent);
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

                                String path = Utilitats.getWorkFolder(ActivityGallery.this, Utilitats.IMAGES).getPath();
                                dataChanged(path);

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
    public static void dataChanged(String path) {
        File files[] = new File(path).listFiles();
        images.clear();

        if (files != null) {
            for (File file : files) {
                if (file.length() != 0L)
                    if (file.getName().contains("PIC") || file.getName().contains("DRW"))
                        images.add(new MyImage(file));
                    else if (file.getName().contains("MOVE"))
                        file.delete();
            }
        }

        adapter.notifyDataSetChanged();
    }

}