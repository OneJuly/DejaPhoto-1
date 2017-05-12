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

            // Create pending intents
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(context, 0, prevIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Update remoteView then set to widget
            remoteViews.setOnClickPendingIntent(R.id.button_prev, prevPendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.v(TAG, TAG_RECV + intent.getAction());

        if (PREV_CLICKED.equals(intent.getAction())) {
            Log.v(TAG, TAG_RECV + "Prev button tapped");
        } else if (NEXT_CLICKED.equals(intent.getAction())) {
            Log.v(TAG, TAG_RECV + "Next button tapped");
        }
    }
}
