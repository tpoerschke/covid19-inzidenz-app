package timpo.incidence

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import timpo.incidence.utility.LocationUtility
import timpo.incidence.utility.api.IncidenceApi
import timpo.incidence.utility.api.IncidenceApiResponseListener
import timpo.incidence.utility.api.IncidenceResultContainer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

/**
 * Implementation of App Widget functionality.
 */
class IncidenceWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.incidence_widget)

    if(MainActivity.incidence >= 0.0) {
        views.setTextViewText(R.id.incidenceTextView, MainActivity.incidence.toString().replace('.', ','))
    }
    else {
        views.setTextViewText(R.id.incidenceTextView, context.getText(R.string.appwidget_no_data))
    }

    if(LocationUtility.lastLocation != null) {
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMANY)
        val locDate = Date(LocationUtility.lastLocation.time)
        views.setTextViewText(R.id.timestampTextView, formatter.format(locDate))
    }
    else {
        views.setTextViewText(R.id.timestampTextView,"")
    }

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}