package com.example.hellowidget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

public class Widget extends AppWidgetProvider {
    private SimpleDateFormat formatter = new SimpleDateFormat("EEEEEEEEE\nd MMM yyyy");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // จัดรูปแบบวันที่
        String now = formatter.format(new Date());

        // แสดงวันที่ในวิดเจ็ต
        RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.main);
        updateViews.setTextViewText(R.id.text, now);
        appWidgetManager.updateAppWidget(appWidgetIds, updateViews);

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
