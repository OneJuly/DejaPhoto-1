package team4.cse110.dejaphoto.settings;


import android.content.Context;
import android.preference.PreferenceManager;

import team4.cse110.dejaphoto.R;

/**
 * Convenience class for interfacing with application preferences.
 */
public class PrefUtils {

    private static final String TAG = "PrefUtils";

    private static Context context;

    public PrefUtils(Context context) {
        this.context = context;
    }



    /**
     * This method determines if DejaVu mode is enabled.
     * @param context -
     * @return true if DejaVu mode is enabled; false otherwise.
     */
    public static boolean dejaVuEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getString(R.string.pref_dejavu_mode_key), false);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean showFriendsPhotosEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getString(R.string.pref_show_friends_key), false);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean showOwnPhotos(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getString(R.string.pref_show_own_key), true);
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean shareOwnPhotos(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(getString(R.string.pref_share_own_key), true);
    }
    /**
     * Get string from resource ID helper
     * @param resID
     * @return
     */
    private static String getString(int resID) {
        return context.getString(resID);
    }

}

