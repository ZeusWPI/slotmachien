package be.ugent.zeus.slotmachien.services;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import be.ugent.zeus.slotmachien.R;
import be.ugent.zeus.slotmachien.data.IntentConstants;
import be.ugent.zeus.slotmachien.data.Model;
import be.ugent.zeus.slotmachien.data.State;

/**
 * @author Wouter Pinnoo <wouter@zeus.ugent.be>
 */
public class WidgetProvider extends AppWidgetProvider {
    private final String SET_BACKGROUND_COLOR = "setBackgroundColor";

    private Intent generateOnClickIntent(Context c) {
        Intent intent = new Intent(c, RequestService.class);

        State state = State.values()[Model.getState(c)];

        switch (state) {
            case OPEN:
                intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.CLOSE.ordinal());
                break;
            default:
                intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.OPEN.ordinal());
                break;
        }
        return intent;
    }

    private PendingIntent getPendingSelfIntent(Context c) {
        Intent intent = new Intent(c, getClass());
        intent.setAction(IntentConstants.INTENT_ACTION_WIDGET_BUTTON);
        return PendingIntent.getBroadcast(c, 0, intent, 0);
    }

    @Override
    public void onReceive(Context c, Intent intent) {
        super.onReceive(c, intent);
        if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSED)) {
            refreshView(c);
        } else if (intent.getAction().equals(IntentConstants.INTENT_ACTION_WIDGET_BUTTON)) {
            c.startService(generateOnClickIntent(c));
        } else if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSING)) {
            setLoadingView(c);
        } else if (intent.getAction().equals(IntentConstants.INTENT_ACTION_PROCESSING_ERROR)) {
            setErrorView(c);
        }
    }

    @Override
    public void onEnabled(Context c) {
        if (Model.getState(c) == State.UNKNOWN.ordinal()) {
            Intent intent = new Intent(c, RequestService.class);
            intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.STATUS.ordinal());
            c.startService(intent);
        }
    }

    @Override
    public void onUpdate(Context c, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Intent intent = new Intent(c, RequestService.class);
        intent.putExtra(IntentConstants.INTENT_EXTRA_REQUEST, RequestType.STATUS.ordinal());
        c.startService(intent);
    }

    private void refreshView(Context c) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c.getApplicationContext());
        ComponentName thisWidget = new ComponentName(c, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(c.getPackageName(), R.layout.widget);
            State state = State.values()[Model.getState(c)];
            remoteViews.setTextViewText(R.id.widgetText, "State: " + state.getText());
            remoteViews.setInt(R.id.widgetText, SET_BACKGROUND_COLOR, state.getColor());
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

    private void setErrorView(Context c) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(c.getApplicationContext());
        ComponentName thisWidget = new ComponentName(c, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(c.getPackageName(), R.layout.widget_error);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
