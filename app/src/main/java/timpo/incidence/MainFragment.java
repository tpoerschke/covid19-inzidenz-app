package timpo.incidence;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import timpo.incidence.utility.LocationUtility;
import timpo.incidence.utility.SimpleLocationCallback;
import timpo.incidence.utility.api.IncidenceApi;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView textview_first = view.findViewById(R.id.textview_first);
        LocationUtility.requestLocation(getContext(), locationResult -> {
            new IncidenceApi(getContext()).getIncidenceData(locationResult.getLastLocation(), incidenceResultContainer -> {
                double incidence = Double.parseDouble(incidenceResultContainer.features.get(0).get("attributes").get("cases7_per_100k"));
                incidence = Math.round(incidence * 100) / 100.0;
                MainActivity.incidence = incidence;
                textview_first.setText(String.valueOf(incidence));
            });
        });
    }
}
