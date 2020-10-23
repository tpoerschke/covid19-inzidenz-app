package timpo.incidence.background

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import timpo.incidence.utility.LocationUtility

class LocationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val appContext = applicationContext

        LocationUtility.requestLocation(appContext)
        Log.d("WORKER", LocationUtility.lastLocation?.time.toString())
        return Result.success()
    }
}