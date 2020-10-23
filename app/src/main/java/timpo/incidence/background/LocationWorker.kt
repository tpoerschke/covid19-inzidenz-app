package timpo.incidence.background

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import timpo.incidence.MainActivity
import timpo.incidence.R
import timpo.incidence.utility.LocationUtility
import timpo.incidence.utility.api.IncidenceApi
import timpo.incidence.utility.api.IncidenceApiResponseListener
import timpo.incidence.utility.api.IncidenceResultContainer
import kotlin.math.roundToInt

class LocationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val appContext = applicationContext

        LocationUtility.requestLocation(appContext)

        IncidenceApi(appContext).getIncidenceData(LocationUtility.lastLocation, object : IncidenceApiResponseListener {
            override fun onResponse(incidenceResultContainer: IncidenceResultContainer) {
                var incidence = incidenceResultContainer.features[0]["attributes"]?.get("cases7_per_100k")?.toDouble()
                if (incidence != null) {
                    incidence = (incidence * 100).roundToInt() / 100.0
                    MainActivity.incidence = incidence
                }
                else {
                    MainActivity.incidence = -1.0
                }
            }
        })

        return Result.success()
    }
}