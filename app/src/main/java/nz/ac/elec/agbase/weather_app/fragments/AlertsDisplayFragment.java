package nz.ac.elec.agbase.weather_app.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.WeatherAlertArrayAdapter;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 28/04/16.
 */
public class AlertsDisplayFragment extends Fragment {

    private final String TAG = "AlertsDisplayFragment";

    private ListView alertsListView;
    private WeatherAlertArrayAdapter alertArrayAdapter;
    private List<WeatherAlert> alertList;

    private IAlertsDisplayFragment mCallback;
    public interface IAlertsDisplayFragment {
        void onWeatherAlertItemClick(WeatherAlert alert);
        void onWeatherAlertItemLongClick(WeatherAlert alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.display_alerts_fragment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof IAlertsDisplayFragment) {
            mCallback = (IAlertsDisplayFragment)context;
        }
        else {
            throw new RuntimeException(context.toString() +
                " must implement IAlertsDisplayFragment");
        }
    }

    private void init(View view) {
        alertsListView = (ListView)view.findViewById(R.id.display_alerts_fragment_listView);
        alertList = new ArrayList<WeatherAlert>();
        alertArrayAdapter = new WeatherAlertArrayAdapter(view.getContext(), alertList);
        alertsListView.setAdapter(alertArrayAdapter);

        alertsListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mCallback.onWeatherAlertItemClick(alertList.get(position));
            }
        });

        alertsListView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onWeatherAlertItemLongClick(alertList.get(position));
                return true;
            }
        });
    }

    public void setWeatherAlerts(List<WeatherAlert> weatherAlerts) {
        alertList.clear();
        alertList.addAll(weatherAlerts);
        alertArrayAdapter.notifyDataSetChanged();
    }
}
