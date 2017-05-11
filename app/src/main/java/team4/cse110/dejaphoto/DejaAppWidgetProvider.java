package team4.cse110.dejaphoto;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * AppWidgetProvider for the DejaVu class
 */
public class DejaAppWidgetProvider extends AppWidgetProvider {

    private static final String NEXT_CLICKED = "nextButtonClick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Inflate view for widget and set up identifier
        RemoteViews remoteViews =
                new RemoteViews(context.getPackageName(), R.layout.widget_deja);
        ComponentName widget
                = new ComponentName(context, DejaAppWidgetProvider.class);

        // Register pending intent for button
        remoteViews.setOnClickPendingIntent
                (R.id.button_next, getPendingSelfIntent(context, NEXT_CLICKED));

        // Update widget
        appWidgetManager.updateAppWidget(widget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NEXT_CLICKED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

            // Inflate view for widget and set up identifier
            RemoteViews remoteViews =
                    new RemoteViews(context.getPackageName(), R.layout.widget_deja);
            ComponentName widget
                    = new ComponentName(context, DejaAppWidgetProvider.class);

            // Change text of button
            remoteViews.setTextViewText(R.id.button_next, "CLICKED");

            // Update widget
            appWidgetManager.updateAppWidget(widget, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass()); // new intent to this class
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
