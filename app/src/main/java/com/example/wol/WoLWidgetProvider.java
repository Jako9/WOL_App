package com.example.wol;

import android.app.PendingIntent;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WoLWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, android.appwidget.AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wol_widget);
            views.setOnClickPendingIntent(R.id.widget_name, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        Log.d("Widget", "onUpdate");
    }

}
