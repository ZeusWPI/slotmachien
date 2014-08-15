package be.ugent.zeus.slotmachien;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import be.ugent.zeus.slotmachien.service.PostRequestService;
import be.ugent.zeus.slotmachien.service.RequestService;

/**
 *
 * @author Wouter Pinnoo <wouter@zeus.ugent.be>
 */
public class WidgetProvider extends AppWidgetProvider {
    private final String SET_BACKGROUND_COLOR = "setBackgroundColor";

    private Intent generateOnClickIntent(Context c){
        Intent intent = new Intent(c, PostRequestService.class);

        switch(getDoorState(c)) {
            case OPEN:
                intent.putExtra(RequestService.MESSAGE, "close");
                break;
            default:
                intent.putExtra(RequestService.MESSAGE, "open");
                break;
        }
        return intent;
    }

    private DoorState getDoorState(Context c){
        int newStateInt = PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext())
                .getInt(LocalConstants.SHARED_PREF_DOORSTATE, 3);
        return DoorState.values()[newStateInt];
    }

    private PendingIntent getPendingSelfIntent(Context c) {
        Intent intent = new Intent(c, getClass());
        intent.setAction(LocalConstants.INTENT_ACTION_WIDGET_BUTTON);
        return PendingIntent.getBroadcast(c, 0, intent, 0);
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        super.onReceive(c, intent);
        if (intent.getAction().equals(LocalConstants.INTENT_ACTION_PROCESSED) && intent.hasExtra(PostRequestService.STATE)) {
            int newState = intent.getIntExtra(PostRequestService.STATE, 3);
            PreferenceManager.getDefaultSharedPreferences(c.getApplicationContext())
                    .edit()
                    .putInt(LocalConstants.SHARED_PREF_DOORSTATE, newState)
                    .commit();

            ComponentName thisWidget = new ComponentName(c, WidgetProvider.class);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c.getApplicationContext());
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            onUpdate(c, appWidgetManager, allWidgetIds);
        } else if (intent.getAction().equals(LocalConstants.INTENT_ACTION_WIDGET_BUTTON)){
            c.startService(generateOnClickIntent(c));
        } else if (intent.getAction().equals(LocalConstants.INTENT_ACTION_PROCESSING)) {
            setLoadingView(c);
        }
    }

    @Override
    public void onUpdate(Context c, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName thisWidget = new ComponentName(c, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(c.getPackageName(), R.layout.widget);
            remoteViews.setTextViewText(R.id.widgetText, "State: " + getDoorState(c).getText());
            remoteViews.setInt(R.id.widgetText, SET_BACKGROUND_COLOR, getDoorState(c).getColor());
            remoteViews.setOnClickPendingIntent(R.id.widgetButton, getPendingSelfIntent(c));
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void setLoadingView(Context c) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c.getApplicationContext());
        ComponentName thisWidget = new ComponentName(c, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(c.getPackageName(), R.layout.widget_loading);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
