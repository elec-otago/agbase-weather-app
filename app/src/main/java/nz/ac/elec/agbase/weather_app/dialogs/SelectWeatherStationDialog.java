package nz.ac.elec.agbase.weather_app.dialogs;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.weather_app.SensorAdapter;
import nz.ac.elec.agbase.weather_app.dialogs.base_classes.ListViewDialog;

/**
 * Created by tm on 23/08/16.
 */
public class SelectWeatherStationDialog extends ListViewDialog implements SensorAdapter.OnItemClickListener {

    private List<Sensor> mWeatherStations;

    private ISelectWeatherStationDialog mCallback;

    @Override
    public void onItemClick(Sensor item) {
        //todo: return and dismiss dialog
    }

    public interface ISelectWeatherStationDialog {
        void getWeatherStation(Sensor weatherStation);
    }

    public SelectWeatherStationDialog(Context context, String title,
            List<Sensor> weatherStations, ISelectWeatherStationDialog callback) {
        super(context, title);
        mCallback = callback;
        mWeatherStations = weatherStations;
        displayWeatherStations();

    }

    @Override
    protected void init(Context context) {
        super.init(context);
        // init view
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        mListView.setLayoutManager(layoutManager);
        mListView.setItemAnimator(new DefaultItemAnimator());
    }

    private void displayWeatherStations() {

        final SensorAdapter adapter = new SensorAdapter(mWeatherStations, this);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onOkClick() {
        //todo: return selected
    }
}