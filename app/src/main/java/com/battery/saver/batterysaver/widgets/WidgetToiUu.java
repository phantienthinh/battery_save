package com.battery.saver.batterysaver.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.battery.saver.batterysaver.R;
import com.battery.saver.batterysaver.activitys.MainActivity;

public class WidgetToiUu extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for ( int appWidgetId : appWidgetIds ){
            Intent intent  = new Intent(context,MainActivity.class);
            PendingIntent pendingIntent  = PendingIntent.getActivity(context,0,intent,0);
            RemoteViews views =  new RemoteViews(context.getPackageName(), R.layout.widget_toi_uu);
            views.setOnClickPendingIntent(R.id.btn_toi_uu_widget,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }
}
