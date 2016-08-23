package nz.ac.elec.agbase.weather_app;

import android.support.v7.internal.widget.AdapterViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;

/**
 * SensorAdapter.java
 *
 * Array adapter for Sensors.
 *
 * Created by tm on 23/08/16.
 */
public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.ViewHolder> {

    private List<Sensor> mSensorList;

    private OnItemClickListener mListener;
    public interface OnItemClickListener {
        void onItemClick(Sensor item);
    }

    public SensorAdapter(List<Sensor> sensorList, OnItemClickListener listener) {
        mListener = listener;
        mSensorList = sensorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.weather_station_listview_item, parent, false); // todo row layout (currently a placeholder)
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sensor sensor = mSensorList.get(position);
        holder.name.setText(sensor.name);
        holder.itemId = sensor.id;
        holder.bind(mSensorList.get(position), mListener);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public int itemId;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.weather_station_name_output);
        }

        public void bind(final Sensor item, final SensorAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}