package proves.julia.drawwithjulia;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by julia on 30/08/16.
 * <p>
 * Image enlarged Gallery: uses a PagerAdapter to imitate the slippery from image to image of an ordinary Gallery.
 */
public class ActivityImage extends Activity {

    private MyPagerAdapter adapter;
    private ViewPager myView;
    private ZoomableImageView imageView;
    private ArrayList<File> images;
    private LinearLayout deleteButton, editButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        Bundle extras = getIntent().getExtras();

        int position = 0;
        if (extras != null) {
            position = extras.getInt(Constants.ID);
        }
        String path = Utilitats.getWorkFolder(this, Utilitats.IMAGES).getPath();
        File files[] = new File(path).listFiles();

        images = new ArrayList();

        if (files != null) {
            for (File file : files) {
                if (file.length() != 0L)
                    if (file.getName().contains("PIC") || file.getName().contains("DRW"))
                        images.add(file);
                /*if (file.getName().contains("PIC")) {
                    images.add(file);
                }*/
            }
        }

        adapter = new MyPagerAdapter();
        myView = findViewById(R.id.viewPager);
        myView.setAdapter(adapter);
        myView.setCurrentItem(position);

    }

    private class MyPagerAdapter extends PagerAdapter {

        public int getCount() {
            return images.size();
        }

        /**
         * Creates the page for the given position.
         *
         * @param collection The containing View in which the page will be shown
         * @param position   The image to be shown
         * @return the new view page
         */
        public Object instantiateItem(ViewGroup collection, final int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.farright, null);

            if (images.get(position).exists()) {
                imageView = view.findViewById(R.id.imatgeCataleg);
                deleteButton = view.findViewById(R.id.deleteButton);
                editButton = view.findViewById(R.id.editButton);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;

                options.inSampleSize = 2;
                //calculateInSampleSize(options, screenWidth, screenHeight);
                options.inJustDecodeBounds = false;


                Bitmap mBitmap = BitmapFactory.decodeFile(images.get(position).getAbsolutePath(), options);

                if (imageView != null && mBitmap != null)
                    imageView.setImageBitmap(mBitmap);

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ActivityImage.this, MainActivity.class);
                        intent.putExtra("image", images.get(position).getAbsolutePath());
                        startActivity(intent);
                    }
                });

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    //The user wants to delete the selected images
                                    case DialogInterface.BUTTON_POSITIVE:
                                        // Deletes the image
                                        File image = images.get(position);
                                        images.remove(position);
                                        image.delete();
                                        break;
                                    // The user does not want to delete the images
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplication());
                        builder.setMessage(getResources().getString(R.string.delete_image)).setPositiveButton(getResources().getString(R.string.yes),
                                clickListener)
                                .setNegativeButton(getResources().getString(R.string.no), clickListener).show();

                    }
                });
            }

            collection.addView(view, 0);

            return view;

        }

        @Override
        public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
            arg0.removeView((View) arg2);

        }

        @Override
        public void finishUpdate(ViewGroup arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;

        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {
            // TODO Auto-generated method stub

        }

    }

}

