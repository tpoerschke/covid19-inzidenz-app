package timpo.incidence;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import timpo.incidence.utility.LocationUtility;
import timpo.incidence.utility.api.IncidenceApi;

public class IncidenceWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "WPRVDR";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.incidence_widget);

        LocationUtility.requestLocation(context, locationResult -> {
            Log.d(TAG, "requestLocation Callback");
            Location location = locationResult.getLastLocation();

            if(location == null) {
                views.setTextViewText(R.id.timestampTextView,"");
                return;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
            Date locDate = new Date(location.getTime());
            views.setTextViewText(R.id.timestampTextView, formatter.format(locDate));

            new IncidenceApi(context).getIncidenceData(location, incidenceResultContainer -> {
                if(incidenceResultContainer.features.size() < 1) {
                    return;
                }
                try {
                    double incidence = Double.parseDouble(incidenceResultContainer.features.get(0).get("attributes").get("cases7_per_100k"));
                    incidence = Math.round(incidence * 100) / 100.0;
                    views.setTextViewText(R.id.incidenceTextView, String.valueOf(incidence).replace('.', ','));
                    views.setTextViewText(R.id.locationTextView, incidenceResultContainer.features.get(0).get("attributes").get("GEN"));
                    Log.d(TAG, "Inzidenz: " + incidence);
                }
                catch (NullPointerException npe) {
                    views.setTextViewText(R.id.incidenceTextView, context.getText(R.string.appwidget_no_data));
                    views.setTextViewText(R.id.locationTextView, "ERROR");
                }

                ComponentName thisWidget = new ComponentName(context, IncidenceWidgetProvider.class);
                AppWidgetManager manager = AppWidgetManager.getInstance(context);
                manager.updateAppWidget(thisWidget, views);

                Log.d(TAG, "end waterfall");

            });
        });
    }
}
