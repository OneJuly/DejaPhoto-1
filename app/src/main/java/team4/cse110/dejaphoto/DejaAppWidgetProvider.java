package team4.cse110.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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
        ComponentName widget = new ComponentName(context, DejaAppWidgetProvider.class);
        int[] allIds = appWidgetManager.getAppWidgetIds(widget);

        for (int id : allIds) {
            // Inflate the view hierarchy
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

            // Update remoteView then set to widget
            remoteViews.setOnClickPendingIntent(R.id.button_prev, prevPendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.button_karma, karmaPendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.button_release, releasePendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.v(TAG, TAG_RECV + intent.getAction());

        if (PREV_CLICKED.equals(intent.getAction())) {
            onPrev();
        } else if (NEXT_CLICKED.equals(intent.getAction())) {
            onNext();
        } else if (KARMA_CLICKED.equals(intent.getAction())) {
            onKarma();
        } else if (RELEASE_CLICKED.equals(intent.getAction())) {
            onRelease();
        }
    }

    private void onPrev() {
        Log.v(TAG, TAG_RECV + "Prev button tapped");
    }

    private void onNext() {
        Log.v(TAG, TAG_RECV + "Next button tapped");
    }

    private void onKarma() {
        Log.v(TAG, TAG_RECV + "Karma button tapped");
    }

    private void onRelease() {
        Log.v(TAG, TAG_RECV + "Release button tapped");
    }

}
