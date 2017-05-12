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

    private static final String NEXT_CLICKED = "NEXT_BUTTON_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.v(TAG, "In update function");

        // Get all associated ids
        ComponentName widget = new ComponentName(context, DejaAppWidgetProvider.class);
        int[] allIds = appWidgetManager.getAppWidgetIds(widget);

        for (int id : allIds) {
            // Inflate the view hierarchy
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_deja);

            // Register a pending intent
            Intent intent = new Intent(context, DejaAppWidgetProvider.class);
            intent.setAction(NEXT_CLICKED);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button_next, pendingIntent);
            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.v(TAG, "Receive: " + intent.getAction());

        if (NEXT_CLICKED.equals(intent.getAction())) {
            Log.v(TAG, "Receive: next clicked");
        }
    }
}
