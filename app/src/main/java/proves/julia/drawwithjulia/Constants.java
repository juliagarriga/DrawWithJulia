package proves.julia.drawwithjulia;

import android.net.Uri;

/**
 * Created by root on 7/1/16.
 */
public final class Constants {

    public static final boolean SERVICE_ON = false;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_SIGNATURE = 2;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int INIT_SESSION = 200;
    public static final int NUM_HORIZONTAL_IMAGES = 4;
    public static final int NUM_VERTICAL_IMAGES = 3;
    /**
     * Used in EXTRAS
     */

    public static final String ID = "ID";
    public static final String IS_CRANE = "IS_GRUA";
    public static final String NEW_SERVICE = "NOU_SERVEI";
    public static final String SESSION = "SESSIO";
    public static final String SERVICE = "SERVICE";
    public static final String DRIVER = "CONDUCTOR";
    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in seconds
    private static final int UPDATE_INTERVAL_IN_SECONDS = 2 * 60;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 60;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    public static boolean GOOGLE_PLAY_SERVICES = true;
    public static boolean DELETE_MODE = false;
    public static boolean DELETE_SERVICE = false;
    public static boolean NOU_SERVEI = false;
    public static Uri uri;


    /**
     * Suppress default constructor for noninstantiability
     */
    private Constants() {
        throw new AssertionError();
    }
}
