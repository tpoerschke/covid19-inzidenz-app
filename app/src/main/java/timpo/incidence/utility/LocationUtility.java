package timpo.incidence.utility;

import android.content.Context;
import android.location.Location;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationUtility {

    public static Location lastLocation;

    private static final String TAG = "LU";

    public static void requestLocation(Context context) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setNumUpdates(1);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    lastLocation = location;
                }
            }
        };

        HandlerThread handlerThread = new HandlerThread("Incidence Background Location Thread");
        handlerThread.start();
        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, locationCallback, handlerThread.getLooper());
    }

    public static void requestLocation(Context context, final SimpleLocationCallback callback) {
        Log.d(TAG, "in locationRequest");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setNumUpdates(1);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d(TAG, "onLocationResult");
                LocationUtility.lastLocation = locationResult.getLastLocation();
                callback.onResult(locationResult);
            }
        };

        LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(locationRequest, locationCallback, null);
        Log.d(TAG, "LocationUpdates angefordert");
    }
}
