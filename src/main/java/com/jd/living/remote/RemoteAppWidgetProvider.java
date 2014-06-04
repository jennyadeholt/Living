package com.jd.living.remote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.jd.living.R;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link DemoAppWidgetConfigureActivity DemoAppWidgetConfigureActivity}
 */
public class RemoteAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_BUTTON = "com.jayway.com.remoteviews_demo.ACTION_BUTTON";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            DemoAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = DemoAppWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.demo_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        //views.setOnClickPendingIntent(R.id.appwidget_button, createButtonPendingIntent(context, ACTION_BUTTON, appWidgetId));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Creates a pending intent for using the
     * provided action and request code.
     *
     * @param buttonAction The intent action for the button.
     * @param requestCode  The request code for this pending intent.
     */
    private static PendingIntent createButtonPendingIntent(Context context, String buttonAction, int requestCode) {
        final Intent intent = new Intent(context, DemoAppWidgetConfigureActivity.class);
        intent.setAction(buttonAction);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, requestCode);

        return PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}


