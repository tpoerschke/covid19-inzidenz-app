package timpo.incidence;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import timpo.incidence.utility.LocationUtility;
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

        final Button dashboardBtn = view.findViewById(R.id.dashboardBtn);
        dashboardBtn.setOnClickListener(event -> {
            Uri uri = Uri.parse("https://corona.rki.de");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        final Button rkiReportBtn = view.findViewById(R.id.rkiReportBtn);
        rkiReportBtn.setOnClickListener(event -> {
            Uri uri = Uri.parse("https://www.rki.de/DE/Content/InfAZ/N/Neuartiges_Coronavirus/Situationsberichte/Gesamt.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        final TextView incidenceTextView = view.findViewById(R.id.incidenceTextView);
        final TextView locationTextView = view.findViewById(R.id.locationTextView);
        LocationUtility.requestLocation(getContext(), locationResult -> {
            new IncidenceApi(getContext()).getIncidenceData(locationResult.getLastLocation(), incidenceResultContainer -> {
                if(incidenceResultContainer.features.size() < 1) {
                    return;
                }
                double incidence = Double.parseDouble(incidenceResultContainer.features.get(0).get("attributes").get("cases7_per_100k"));
                incidence = Math.round(incidence * 100) / 100.0;

                incidenceTextView.setTextColor(getResources().getColor(R.color.textColor));

                if(incidence >= 50) {
                    incidenceTextView.setTextColor(getResources().getColor(R.color.colorRed));
                }
                else if(incidence >= 35) {
                    incidenceTextView.setTextColor(getResources().getColor(R.color.colorYellow));
                }

                incidenceTextView.setText(String.valueOf(incidence).replace('.', ','));
                locationTextView.setText(incidenceResultContainer.features.get(0).get("attributes").get("GEN"));
            });
        });
    }
}
