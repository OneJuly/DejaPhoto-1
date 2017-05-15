package team4.cse110.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.app.WallpaperManager;

import java.io.IOException;


/**
 * AppWidgetProvider for the DejaVu class.
 */
public class DejaAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "DEJA_PROVIDER";
    public static final String TAG_RECV = "Receive: ";

    // Intent actions.
    private static final String PREV_CLICKED = "PREV_BUTTON_CLICK";
    private static final String NEXT_CLICKED = "NEXT_BUTTON_CLICK";
    private static final String KARMA_CLICKED = "KARMA_BUTTON_CLICK";
    private static final String RELEASE_CLICKED = "RELEASE_BUTTON_CLICK";

    /**
     * This method TODO
     * @param context - environment data of the app.
     * @param appWidgetManager - TODO
     * @param appWidgetIds - TODO
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "In update function");

        // Get all associated widget IDs
        ComponentName widgetProvider = new ComponentName(context, DejaAppWidgetProvider.class);
        int[] allIds = appWidgetManager.getAppWidgetIds(widgetProvider);

        // Update all widgets with default layout
        for (int id : allIds) {
            RemoteViews remoteViews = getDefaultRemoteViews(context);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    /**
     * This method handles actions sensed by the app.
     * @param context - environment data of the app.
     * @param intent - TODO
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.v(TAG, TAG_RECV + intent.getAction());

        // Checks which button on the widget was clicked.
        if (PREV_CLICKED.equals(intent.getAction())) {
            onPrev(context, intent);
        } else if (NEXT_CLICKED.equals(intent.getAction())) {
            onNext(context, intent);
        } else if (KARMA_CLICKED.equals(intent.getAction())) {
            onKarma(context, intent);
        } else if (RELEASE_CLICKED.equals(intent.getAction())) {
            //onRelease(context, intent);
        }
    }

    /**
     * This method executes the "previous" button's actions.
     * @param context - environment data of the app.
     * @param intent - TODO
     */
    private void onPrev(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Prev button tapped");

        Algorithm algorithm = getAlgorithm(context);

        // Set the previous wallpaper.
        Bitmap bitmap = algorithm.prev();
        if (bitmap != null) {
            Log.v(TAG, "Setting prev bitmap"); // DEBUG
//            setWallpaper(bitmap, context);
        }

        // Enable the karma button if no karma.
/*        if (!algorithm.hasKarma()) {
            Log.v(TAG, "Enabling button karma after prev"); // DEBUG
            RemoteViews remoteViews = getDefaultRemoteViews(context);
            enableView(R.id.button_karma, remoteViews);
            updateRemoteViews(context, remoteViews);
        }*/
    }

    /**
     * This method executes the "next" button's actions.
     * @param context - environment data of the app.
     * @param intent - an abstract description of the operation to be
     * performed.
     */
    private void onNext(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Next button tapped");

        Algorithm algorithm = getAlgorithm(context);

        // Set the next wallpaper or default if none exits
        //Bitmap bitmap = algorithm.next();
        Photo photo = algorithm.next();
        if (photo != null) {
            Log.v(TAG, "Setting next bitmap"); // DEBUG
            setWallpaper(photo, context);
        } else {
            Log.v(TAG, "Setting next default photo"); // DEBUG
            setDefaultWallpaper(context);
        }

        // Enable the karma button if no karma
/*        if (!algorithm.hasKarma()) {
            Log.v(TAG, "Enabling button karma after next"); // DEBUG
            RemoteViews remoteViews = getDefaultRemoteViews(context);
            enableView(R.id.button_karma, remoteViews);
            updateRemoteViews(context, remoteViews);
        }*/
    }

    /**
     * This method TODO
     * @param context - environment data of the app.
     * @param intent - abstract description of the operation to be performed.
     */
    private void onKarma(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Karma button tapped");

        Algorithm algorithm = getAlgorithm(context);

        // Set karma and disable button
        algorithm.incKarma();
        RemoteViews remoteViews = getDefaultRemoteViews(context);
        disableView(R.id.button_karma, remoteViews);
        updateRemoteViews(context, remoteViews);
    }

    /**
     * This method TODO
     * @param context
     * @param intent
     */
/*    private void onRelease(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Release button tapped");

        Algorithm algorithm = getAlgorithm(context);

        // Release image and set replacement
        Bitmap bitmap = algorithm.release();
        if (bitmap != null) {
            Log.v(TAG, "Setting release bitmap"); // DEBUG
            setWallpaper(bitmap, context);
        } else {
            Log.v(TAG, "Setting release default photo"); // DEBUG
            setDefaultWallpaper(context);
        }
    }*/

    /**
     * Inflates a default view hierarchy.
     * @param context current application context.
     * @return the default view hierarchy.
     */
    private RemoteViews getDefaultRemoteViews(Context context) {
        // Inflate view hierarchy
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_deja);

        // Create intents for each button
        Intent prevIntent = new Intent(context, DejaAppWidgetProvider.class);
        prevIntent.setAction(PREV_CLICKED);
        Intent nextIntent = new Intent(context, DejaAppWidgetProvider.class);
        nextIntent.setAction(NEXT_CLICKED);
        Intent karmaIntent = new Intent(context, DejaAppWidgetProvider.class);
        karmaIntent.setAction(KARMA_CLICKED);
        Intent releaseIntent = new Intent(context, DejaAppWidgetProvider.class);
        releaseIntent.setAction(RELEASE_CLICKED);

        // Create pending intents
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0,
                prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0,
                nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent karmaPendingIntent = PendingIntent.getBroadcast(context, 0,
                karmaIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent releasePendingIntent = PendingIntent.getBroadcast(context, 0,
                releaseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Update buttons
        remoteViews.setOnClickPendingIntent(R.id.button_prev, prevPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.button_karma, karmaPendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.button_release, releasePendingIntent);

        return remoteViews;
    }

    /**
     * Updates all associated widgets with the given view hierarchy.
     * @param context - current application context.
     * @param remoteViews view hierarchy.
     */
    private void updateRemoteViews(Context context, RemoteViews remoteViews) {
        // Get widget manager and all associated ids
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetProvider = new ComponentName(context, DejaAppWidgetProvider.class);
        int[] allIds = appWidgetManager.getAppWidgetIds(widgetProvider);

        // Update all widgets with new views
        for (int id : allIds) {
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    /**
     * Enable a view in the given view hierarchy.
     * @param id - id of the view.
     * @param remoteViews view hierarchy.
     */
    private void enableView(int id, RemoteViews remoteViews) {
        remoteViews.setBoolean(id, "setEnabled", true);
    }

    /**
     * Disable a view in the given view hierarchy.
     * @param id - id of the view.
     * @param remoteViews - view hierarchy.
     */
    private void disableView(int id, RemoteViews remoteViews) {
        remoteViews.setBoolean(id, "setEnabled", false);
    }

    /**
     * Set the wallpaper of the device to the bitmap.
     * @param photo - bitmap of new wallpaper.
     * @param context - current application context.
     */
    private void setWallpaper(Photo photo, Context context) {
        SetWallpaper sp = new SetWallpaper(context);
        sp.execute(photo);
    }

    /***
     * Set the wallpaper of the device to the default bitmap.
     * @param context - current application context.
     */
    private void setDefaultWallpaper(Context context) {
        WallpaperManager myWallpaperManager =
                WallpaperManager.getInstance(context);
        try {
            myWallpaperManager.setResource(+R.drawable.defaultimage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a new algorithm. Convenience methods to ease swapping between
     * algorithm implementations.
     * @param context current application context.
     * @return a new algorithm.
     */
    private Algorithm getAlgorithm(Context context) {
        return new DejaAlgorithm(context);
    }
}
