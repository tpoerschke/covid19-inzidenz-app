package timpo.incidence.utility.api

import android.content.Context
import android.location.Location
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

class IncidenceApi(private val context: Context) {

    private val url = "https://services7.arcgis.com/mOBPykOjAyBO2ZKk/arcgis/rest/services/RKI_Landkreisdaten/FeatureServer/0/query?where=1%3D1&outFields=GEN,cases7_per_100k&geometry={lon}%2C{lat}&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatialRelWithin&returnGeometry=false&outSR=4326&f=json"

    fun getIncidenceData(loc: Location?, responseListener: IncidenceApiResponseListener) {

        if(loc == null) {
            return
        }

        val queue = Volley.newRequestQueue(context)
        val url = url.replace("{lat}", loc.latitude.toString()).replace("{lon}", loc.longitude.toString())

        val stringRequest = StringRequest(Request.Method.GET, url,
                Response.Listener { response ->
                    var incidenceResultContainer = Gson().fromJson(response, IncidenceResultContainer::class.java)
                    responseListener.onResponse(incidenceResultContainer)
                },
                Response.ErrorListener { error ->  Log.d("API", "Uff... $error") })

        queue.add(stringRequest)
    }
}