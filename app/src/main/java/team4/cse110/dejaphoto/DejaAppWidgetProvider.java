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

/**
 * AppWidgetProvider for the DejaVu class
 */
public class DejaAppWidgetProvider extends AppWidgetProvider {

    public static final String TAG = "DEJA_PROVIDER";
    public static final String TAG_RECV = "Receive: ";

    private static final String PREV_CLICKED = "PREV_BUTTON_CLICK";
    private static final String NEXT_CLICKED = "NEXT_BUTTON_CLICK";
    private static final String KARMA_CLICKED = "KARMA_BUTTON_CLICK";
    private static final String RELEASE_CLICKED = "RELEASE_BUTTON_CLICK";

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

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.v(TAG, TAG_RECV + intent.getAction());

        if (PREV_CLICKED.equals(intent.getAction())) {
            onPrev(context, intent);
        } else if (NEXT_CLICKED.equals(intent.getAction())) {
            onNext(context, intent);
        } else if (KARMA_CLICKED.equals(intent.getAction())) {
            onKarma(context, intent);
        } else if (RELEASE_CLICKED.equals(intent.getAction())) {
            onRelease(context, intent);
        }
    }

    private void onPrev(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Prev button tapped");

        RemoteViews remoteViews = getDefaultRemoteViews(context); // TODO query algorithm
        enableView(R.id.button_karma, remoteViews);
        enableView(R.id.button_release, remoteViews);
        updateRemoteViews(context, remoteViews);
    }

    private void onNext(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Next button tapped");
        RemoteViews remoteViews = getDefaultRemoteViews(context); // TODO query algorithm
        enableView(R.id.button_karma, remoteViews);
        enableView(R.id.button_release, remoteViews);
        updateRemoteViews(context, remoteViews);
    }

    private void onKarma(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Karma button tapped");
        RemoteViews remoteViews = getDefaultRemoteViews(context);
        disableView(R.id.button_karma, remoteViews);
        updateRemoteViews(context, remoteViews);
    }

    private void onRelease(Context context, Intent intent) {
        Log.v(TAG, TAG_RECV + "Release button tapped");
        RemoteViews remoteViews = getDefaultRemoteViews(context);
        disableView(R.id.button_release, remoteViews);
        updateRemoteViews(context, remoteViews);
    }

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

    private void enableView(int id, RemoteViews remoteViews) {
        remoteViews.setBoolean(id, "setEnabled", true);
    }

    private void disableView(int id, RemoteViews remoteViews) {
        remoteViews.setBoolean(id, "setEnabled", false);
    }

    private void setWallpaper(Bitmap bitmap) {

    }

    private void setDefaultWallpaper() {

    }
}
