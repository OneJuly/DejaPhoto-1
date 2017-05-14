package team4.cse110.dejaphoto;


import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Convenience class for interfacing with application preferences.
 */
public class PrefUtils {

    private static final String PREF_DEJAVU_MODE = "dejaVuMode";
    private static final String PREF_INIT = "initialized";

    /* Determine if DejaVu mode is enabled */

    /**
     * This method determines if DejaVu mode is enabled.
     * @param context -
     * @return true if DejaVu mode is enabled; false otherwise.
     */
    public static boolean dejaVuEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_DEJAVU_MODE, false);
    }

    /**
     * This method sets DejaVu mode preferences.
     * @param context
     * @param mode
     */
    public static void setDejaVuMode(Context context, boolean mode) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_DEJAVU_MODE, mode)
                .apply();
    }

    public static boolean isInit(Context context) {
       return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_INIT, false);
    }

    public static void setInit(Context context, boolean mode) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_INIT, mode)
                .apply();
    }
}

