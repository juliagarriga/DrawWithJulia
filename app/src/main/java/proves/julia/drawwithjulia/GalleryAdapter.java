package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

/**
 * Created by julia on 26/08/16.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    private final Activity mContext;
    private ArrayList<MyImage> images;
    private static final int REQUEST_EDIT_IMAGE = 2;

    /**
     * Adapter used to show a list of images
     *
     * @param activity Activity where the adapter is shown
     * @param images   List of images to show
     */
    public GalleryAdapter(Activity activity, ArrayList<MyImage> images) {
        super();
        mContext = activity;
        this.images = images;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_list_view, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        /*viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;*/

        //Set phone metrics
        DisplayMetrics displaymetrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenWidth = displaymetrics.widthPixels;

        // Calculates the sample size of the bitmap based on the screen dimensions.
        //options.inJustDecodeBounds = false;

        // Depending on the rotation of the screen, the number of pictures shown in a line is different.
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int imageWidth;

        if (display.getRotation() == Surface.ROTATION_90 || display.getRotation() == Surface.ROTATION_270)
            imageWidth = screenWidth / Constants.NUM_HORIZONTAL_IMAGES;
        else
            imageWidth = screenWidth / Constants.NUM_VERTICAL_IMAGES;

        /*options.inSampleSize = calculateInSampleSize(options, 100, 100);
        Bitmap scaledBitmap = BitmapFactory.decodeFile(images.get(position).getFile().getPath(), options);
        viewHolder.image.setImageBitmap(Bitmap.createScaledBitmap(scaledBitmap, imageWidth, imageWidth, true));*/

        Glide.with(mContext)
                .load(images.get(position).getFile().getAbsolutePath())
                .apply(new RequestOptions().override(imageWidth, imageWidth)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(viewHolder.image);

        if (images.get(position).isVisible()) {
            viewHolder.del.setVisibility(View.VISIBLE);

            if (images.get(position).isTicked())
                viewHolder.del.setImageDrawable(mContext.getResources().getDrawable(R.drawable.tick_icon));
            else
                viewHolder.del.setImageDrawable(null);

        } else
            viewHolder.del.setVisibility(View.INVISIBLE);

        //final DataBase db = DataBase.getInstance(mContext);

        // The pictures are editable (can be deleted) only if the seen session is the last one.
        viewHolder.image.setOnLongClickListener(new View.OnLongClickListener() {
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

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constants.DELETE_MODE) {
                    //If image is clicked in delete mode, it passes to be a possible deleted picture.
                    if (images.get(position).isTicked())
                        images.get(position).setTicked(false);
                    else
                        images.get(position).setTicked(true);
                    notifyDataSetChanged();
                } else {

                    // Activity that shows each image in bigger dimensions (occupying all the screen).
                    Intent intent = new Intent(mContext, ActivityImage.class);
                    intent.putExtra(Constants.ID, position);
                    mContext.startActivityForResult(intent, REQUEST_EDIT_IMAGE);

                }
            }
        });

        viewHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 4;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageButton del;
        private ImageView image;

        public ViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.item);
            del = view.findViewById(R.id.delete);
        }
    }
}
