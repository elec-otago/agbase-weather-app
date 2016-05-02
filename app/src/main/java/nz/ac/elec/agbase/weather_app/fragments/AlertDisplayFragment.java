package nz.ac.elec.agbase.weather_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabase;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.AlertSummaryGenerator;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 2/05/16.
 */
public class AlertDisplayFragment extends Fragment {

    private final String TAG = "AlertDisplay";

    private TextView mWStationName, mWAlertDesc, mWCondition;
    private Button mDeleteBtn, mEditBtn;

    public WeatherAlert mWeatherAlert;

    private IAlertDisplayFragment mCallback;
    public interface IAlertDisplayFragment {
        void onClickEditButton(WeatherAlert alert);
        void onClickDeleteButton(WeatherAlert alert);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.display_alert_fragment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof IAlertDisplayFragment) {
            mCallback = (IAlertDisplayFragment)context;
        }
        else {
            throw new RuntimeException(context.toString() +
                    " must implement IAlertDisplayFragment");
        }
    }

    // region init
    private void init(View view) {
        mWStationName = (TextView)view.findViewById(R.id.display_alert_weatherstation_field);
        mWAlertDesc = (TextView)view.findViewById(R.id.display_alert_description_field);
        mWCondition = (TextView)view.findViewById(R.id.display_alert_condition_field);

        mEditBtn = (Button)view.findViewById(R.id.display_alert_edit_btn);
        mEditBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickEditButton(mWeatherAlert);
            }
        });

        mDeleteBtn = (Button)view.findViewById(R.id.display_alert_delete_btn);
        mDeleteBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClickDeleteButton(mWeatherAlert);
            }
        });
    }
    // endregion

    public void setWeatherAlert(WeatherAlert weatherAlert) {
        mWeatherAlert = weatherAlert;
        AgBaseDatabaseManager agBaseDb = AgBaseDatabaseManager.getInstance();
        Sensor weatherStation = agBaseDb.readSensorWithGuid(weatherAlert.getDeviceGuid());

        if(weatherStation != null && weatherStation.name != null) {
            mWStationName.setText(weatherStation.name);
        }
        else {
            mWStationName.setText("");
        }

        if(mWeatherAlert.getDescription() != null) {
            mWAlertDesc.setText(mWeatherAlert.getDescription());
        }
        else {
            mWAlertDesc.setText("");
        }
        AlertSummaryGenerator generator = new AlertSummaryGenerator();
        String alertConditions = generator.writeAlertSummary(weatherAlert);
        mWCondition.setText(alertConditions);
    }
}
