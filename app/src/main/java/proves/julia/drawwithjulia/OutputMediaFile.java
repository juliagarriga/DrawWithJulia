package proves.julia.drawwithjulia;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.Random;

/**
 * Created by julia on 03/08/16.
 */
public class OutputMediaFile {

    private Activity activity;
    private Random random;

    public OutputMediaFile(Activity activity) {

        this.activity = activity;
        random = new Random();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public Uri getOutputMediaFileUri(String filename) {
        return Uri.fromFile(getOutputMediaFile(filename, true));
    }

    /**
     * Create a File for saving an image or video
     */
    public File getOutputMediaFile(String filename, boolean isRandom) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.


        File mediaStorageDir = new File(Utilitats.getWorkFolder(activity, Utilitats.IMAGES), "");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("OMF", "failed to create directory");
                return null;
            }
        }

        File mediaFile;
        String ext = ".jpg";

        if (filename == "MOVE")
            ext = ".png";

        if (isRandom)
            filename = mediaStorageDir.getPath() + File.separator + filename + "_" +
                    String.format("%09d", Math.abs(random.nextInt())) + ext;
        else
            filename = filename.replace("PIC", "DRW");

        mediaFile = new File(filename);

        return mediaFile;
    }
}
