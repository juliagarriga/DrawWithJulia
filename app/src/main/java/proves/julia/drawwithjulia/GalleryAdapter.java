package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by julia on 26/08/16.
 */
public class GalleryAdapter extends BaseAdapter {
    private final Activity mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<MyImage> images;
    private ImageButton del;

    /**
     * Adapter used to show a list of images
     *
     * @param activity Activity where the adapter is shown
     * @param images   List of images to show
     */
    public GalleryAdapter(Activity activity, ArrayList<MyImage> images) {
        super();
        mContext = activity;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.images = images;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        view = mLayoutInflater.inflate(R.layout.gallery_list_view, null);
        final ImageView image = (ImageView) view.findViewById(R.id.item);
        del = (ImageButton) view.findViewById(R.id.delete);
        //image.setPadding(5, 10, 5, 10);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        //Set phone metrics
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;
        int screenHeight = displaymetrics.heightPixels;

        // Calculates the sample size of the bitmap based on the screen dimensions.
        options.inSampleSize = calculateInSampleSize(options, screenWidth, screenHeight);
        options.inJustDecodeBounds = false;

        // Depending on the rotation of the screen, the number of pictures shown in a line is different.
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display.getRotation() == Surface.ROTATION_90 || display.getRotation() == Surface.ROTATION_270)
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(images.get(i).getFile().getPath(),
                    options), screenWidth / Constants.NUM_HORIZONTAL_IMAGES, screenWidth / Constants.NUM_HORIZONTAL_IMAGES, true));
        else
            image.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeFile(images.get(i).getFile().getPath(),
                    options), screenWidth / Constants.NUM_VERTICAL_IMAGES, screenWidth / Constants.NUM_VERTICAL_IMAGES, true));

        if (images.get(i).isVisible()) {
            del.setVisibility(View.VISIBLE);

            if (images.get(i).isTicked())
                del.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tick_icon));
            else
                del.setImageDrawable(null);

        } else
            del.setVisibility(View.INVISIBLE);

        //final DataBase db = DataBase.getInstance(mContext);

        // The pictures are editable (can be deleted) only if the seen session is the last one.
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                for (MyImage image : images)
                    image.setVisibility(true);
                //FragmentGallery.setDelButton(false);
                //When called, the gallery is actualized.
                notifyDataSetChanged();
                return true;
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.DELETE_MODE) {
                    //If image is clicked in delete mode, it passes to be a possible deleted picture.
                    if (images.get(i).isTicked())
                        images.get(i).setTicked(false);
                    else
                        images.get(i).setTicked(true);
                    notifyDataSetChanged();
                } else {

                    // Activity that shows each image in bigger dimensions (occupying all the screen).
                    Intent intent = new Intent(mContext, ActivityImage.class);
                    intent.putExtra(Constants.ID, i);
                    //intent.putExtra(Constants.SERVICE, service);
                    //intent.putExtra(Constants.SESSION, db.getNumSession(session));
                    // intent.putExtra(Constants.DRIVER, db.getDriver(session));
                    mContext.startActivity(intent);

                }
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyDataSetChanged();
            }
        });


        return view;
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }
}
