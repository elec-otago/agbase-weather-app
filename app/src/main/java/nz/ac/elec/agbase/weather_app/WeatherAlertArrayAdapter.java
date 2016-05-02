package nz.ac.elec.agbase.weather_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabase;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 28/04/16.
 */
public class WeatherAlertArrayAdapter extends ArrayAdapter<WeatherAlert> {

    public WeatherAlertArrayAdapter(Context context, List<WeatherAlert> alertList) {
        super(context, R.layout.alerts_listview_item, alertList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return convertView(position, convertView, parent);
    }

    private View convertView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        View row = convertView;

        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();

        if(convertView == null) {
            row = inflater.inflate(R.layout.alerts_listview_item, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder)row.getTag();
        }

        WeatherAlert alert = getItem(position);
        Sensor weatherStation = AgBaseDatabaseManager.getInstance()
                .readSensorWithGuid(alert.getDeviceGuid());

        holder.getAlertNameView().setText(alert.getName());
        holder.getWeatherStationView().setText(weatherStation.name);

        return row;
    }

    private static class ViewHolder {

        private View row;
        private TextView alertNameView, weatherStationView;

        public ViewHolder(View row) {
            this.row = row;
            getAlertNameView();
            getWeatherStationView();
        }

        public TextView getAlertNameView() {
            if(alertNameView == null) {
                alertNameView = (TextView)row.findViewById(R.id.alert_listview_item_name);
            }
            return alertNameView;
        }

        public TextView getWeatherStationView() {
            if(weatherStationView == null) {
                weatherStationView = (TextView)row.findViewById(R.id.alert_listview_item_station);
            }
            return weatherStationView;
        }
    }
}
