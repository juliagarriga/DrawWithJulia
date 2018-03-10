package proves.julia.drawwithjulia;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

public class Utilitats {
    public final static int ACT_FAM_RETURN = 123;
    public final static int RETURN_ARTICLE = 124;
    public final static int REQUEST_ARTICLE = 125;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_WHITE = 1;
    public final static int THEME_BLUE = 2;
    public final static int SQL_TEXT = 0;
    public final static int SQL_REAL = 1;
    public final static int SQL_INT = 2;
    public final static String CONFIG = "config";
    public final static String BACKUP = "backup";
    public final static String WORK = "work";
    public final static String IMPORTED = "imported";
    public final static String EXPORTED = "exported";
    public final static String IMPORT = "import";
    public final static String EXPORT = "export";
    public final static String LOGS = "logs";
    public final static String IMAGES = "pictures";
    public final static String FOTOS = "fotos";
    public static double lat, lng;
    public static String adr;
    public static int numId;
    static Cursor curEsp;
    static Cursor curArt;
    static Cursor curCli;
    static Cursor tmpCur;
    static double preu = 0;
    static double preutarifa = 0;
    static double dte = 0;
    static String tipdte;
    static boolean mExternalStorageAvailable = false;
    static boolean mExternalStorageWriteable = false;
    private static int sTheme;

    public static void Toast(final Activity act, final String txt) {
        act.runOnUiThread(new Runnable() {
            public void run() {

                Toast toast = Toast.makeText(act, txt, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL
                        | Gravity.CENTER_HORIZONTAL, 0, -100);
                toast.show();

            }
        });

    }



	/*static public Cursor Query(DataBase helper, String sql) {
        tmpCur = helper.getWritableDatabase().rawQuery(sql, new String[]{});
		if (tmpCur.getCount() > 0) {
			tmpCur.moveToFirst();
			return tmpCur;
		} else
			return null;

	}

	static String getProfile(Activity act, Properties propInp, String file,
							 String text) {
		String ret = "_";
		Properties prop = propInp;
		if (prop == null)
			prop = new Properties();
		InputStream input = null;

		try {
			String dir = Utilitats.getWorkFolder(act, Utilitats.CONFIG)
					.getAbsolutePath() + "/" + file;

			input = new FileInputStream(dir);
			prop.load(input);
			ret = prop.getProperty(text);

		} catch (IOException ex) {
			Errors.appendLog(act, Errors.ERROR, "getProfile",
					"Error Carregant properties", ex, null, true);
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	static void Toast(final Activity act, final String txt, boolean so) {
		Toast(act, txt, R.raw.capella);

	}

	static void Toast(final Activity act, final String txt, int so) {
		if (so > 0)
			so(act, so);
		Toast(act, txt);

	}*/

    static public void so(Context ct, int i) {
        final MediaPlayer mp = MediaPlayer.create(ct, i);
        mp.start();

    }

    static File comprovaFolder(Activity act, String dir) {

        Boolean isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        File folder;

        if (isSDPresent)
            folder = new File(Environment.getExternalStorageDirectory(), dir);
        else
            folder = new File(act.getFilesDir(), dir);

        if (!folder.exists())
            if (folder.mkdirs() == false)
                return null;
        return folder;
    }
	/*
	static public boolean isOnline(Activity act) {
		ConnectivityManager cm = (ConnectivityManager) act
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			MediaPlayer mp = MediaPlayer.create(act.getApplicationContext(),
					R.raw.color);
			mp.start();

			return true;
		}
		Toast.makeText(
				act.getApplicationContext(),
				"No hi ha connexió Internet. Revisi xarxa i torni a intentar-ho",
				Toast.LENGTH_LONG).show();
		MediaPlayer mp = MediaPlayer.create(act.getApplicationContext(),
				R.raw.capella);
		mp.start();
		return false;
	}*/

    public static File getWorkFolder(Activity act, String fold) {
        if (comprovaFolder(act, "/sgb.orders") == null) {
            return null;
        } else {
            if (fold.equals(Utilitats.CONFIG))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.CONFIG);
            if (fold.equals(Utilitats.WORK))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.WORK);
            if (fold.equals(Utilitats.BACKUP))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.BACKUP);
            if (fold.equals(Utilitats.IMPORT))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.IMPORT);
            if (fold.equals(Utilitats.EXPORT))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.EXPORT);
            if (fold.equals(Utilitats.IMPORTED))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.IMPORTED);
            if (fold.equals(Utilitats.EXPORTED))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.EXPORTED);
            if (fold.equals(Utilitats.LOGS))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.LOGS);
            if (fold.equals(Utilitats.IMAGES))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.IMAGES);
            if (fold.equals(Utilitats.FOTOS))
                return comprovaFolder(act, "/sgb.orders/" + Utilitats.FOTOS);
        }
        return null;
    }

    public static String getString(Cursor c, String name) {
        String rt = c.getString(c.getColumnIndex(name));
        if (rt == null)
            rt = "";
        return rt;
    }

    static public class TPreus {
        public String article;
        public String familia;
        public String linia;
        public String descripcio;

        public String tipDte; // Tipus de descompte a aplicar
        // '=','-','+',' ','%'
        public double quantitat;
        public double dte; // Valor del dte o import
        public double preuBase; // Preu base sobre el que s'aplica el dte
        public double preuTarifa; // Preu seguin la tarifa del client
        public double preuNet;
        public double quantitatRegal;
        public String modeRegal; // Si '*' dividim la quantitat per la
        // quantitatRegal
        public String articleRegal;
    }

}
