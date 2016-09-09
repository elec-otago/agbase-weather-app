package nz.ac.elec.agbase.weather_app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.AlertSummaryGenerator;
import nz.ac.elec.agbase.weather_app.WeatherAppActivity;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.dialogs.ConfirmAlertDialog;
import nz.ac.elec.agbase.weather_app.fragments.WeatherAlertFormFragment;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * EditWeatherActivity.java
 *
 * Created by tm on 3/05/16.
 */
public class EditWeatherAlertActivity extends WeatherAppActivity implements WeatherAlertFormFragment.IWeatherAlertFormFragment,
        ConfirmAlertDialog.IConfirmAlertDialog {

    private final String TAG =  "EditWeatherAlert";

    // region constants
    private final String TOOLBAR_TITLE                          = "Edit Weather Alert";


    private final String EDIT_CONDITION_MSG                     = "Updated Weather Alert";
    private final String FAIL_NO_CONDITION_MSG                  = "Failed to update Weather Alert.  " +
            "A weather alert needs at least 1 trigger condition";

    private final String[] RB_ABOVE_BELOW                       = new String[] {"above", "below"};

    public static String ARGS_WEATHER_ALERT                     = "nz.ac.elec.agbase.weather_app.activities.EditWeatherAlert.ARGS_WEATHER_ALERT";
    // endregion

    private Toolbar toolbar;
    private WeatherAlertFormFragment mFragment;
    private WeatherAlert mWeatherAlert;

    // region broadcast receiver
    private IntentFilter intentFilter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(WeatherSyncAdapter.STATION_UPDATE.equals(action)) {
                mFragment.updateWeatherStations();
            }
        }
    };
    // endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_fragment_layout);
        initToolbar();
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int weatherAlertId = getIntent().getIntExtra(ARGS_WEATHER_ALERT, -1);
        if(weatherAlertId != -1) {
            mWeatherAlert = AlertDatabaseManager.getInstance().readWeatherAlert(weatherAlertId);
        }
        if(mWeatherAlert == null) {
            finish();
        }
        else {
            displayAlertDetails();

            intentFilter = new IntentFilter();
            intentFilter.addAction(WeatherSyncAdapter.STATION_UPDATE);
            registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    // region initialization
    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
    }

    private void initFragment() {
        mFragment= new WeatherAlertFormFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
    // endregion

    // region display alert in fragment code
    private void displayAlertDetails() {
        setAlertNameDisplay();
        setAlertDescriptionDisplay();
        setTemperatureDisplay();
        setWindSpeedDisplay();
        setHumidityDisplay();
        setAirPressureDisplay();
        setRainDisplay();
       // setSnowDisplay();
        setWeatherStation();
    }

    private void setAlertNameDisplay() {

        if(!TextUtils.isEmpty(mWeatherAlert.getName())) {
            mFragment.setName(mWeatherAlert.getName());
        }
    }

    private void setAlertDescriptionDisplay() {

        if(!TextUtils.isEmpty(mWeatherAlert.getDescription())) {
            mFragment.setDescription(mWeatherAlert.getDescription());
        }
    }

    private void setSnowDisplay() {

        /*if(mWeatherAlert.getCheckSnow()) {
            WeatherAlert.CheckCondition snowCondition = mWeatherAlert.getCheckSnowCondition();

            if(snowCondition == WeatherAlert.CheckCondition.IS_TRUE) {
                mFragment.setSnowCondition(WeatherAlertFormFragment.RB_SNOW_CONDITION[1]);
            }
            else {
                double snowValue = mWeatherAlert.getSnowIntensityValue();

                mFragment.setSnowCondition(WeatherAlertFormFragment.RB_SNOW_CONDITION[0]);
                mFragment.setSnowValue(snowValue);
            }
            mFragment.setSnowCheckbox(true);
        }
        else {
            mFragment.setSnowCheckbox(false);
        }*/
    }

    private void setRainDisplay() {

        if(mWeatherAlert.getCheckRain()) {
            WeatherAlert.CheckCondition rainCondition = mWeatherAlert.getCheckRainCondition();

            if(rainCondition == WeatherAlert.CheckCondition.IS_TRUE) {
                mFragment.setRainCondition(WeatherAlertFormFragment.RB_RAIN_CONDITION[1]);
            }
            else {
                double rainValue = mWeatherAlert.getRainIntensityValue();

                mFragment.setRainCondition(WeatherAlertFormFragment.RB_RAIN_CONDITION[0]);
                mFragment.setRainValue(rainValue);
            }
            mFragment.setRainCheckbox(true);
        }
        else {
            mFragment.setRainCheckbox(false);
        }
    }

    private void setAirPressureDisplay() {

        if(mWeatherAlert.getCheckAirPressure()) {
            WeatherAlert.CheckCondition airPressureCondition = mWeatherAlert.getCheckAirPressureCondition();
            double airPressureValue = mWeatherAlert.getAirPressureValue();

            if(airPressureCondition == WeatherAlert.CheckCondition.ABOVE) {
                mFragment.setAirPressureCondition(RB_ABOVE_BELOW[0]);
            }
            else {
                mFragment.setAirPressureCondition(RB_ABOVE_BELOW[1]);
            }
            mFragment.setAirPressureValue(airPressureValue);
            mFragment.setAirPressureCheckbox(true);
        }
        else {
            mFragment.setAirPressureCheckbox(false);
        }
    }

    private void setHumidityDisplay() {

        if(mWeatherAlert.getCheckHumidity()) {
            WeatherAlert.CheckCondition humidityCondition = mWeatherAlert.getCheckHumidityCondition();
            double humidityValue = mWeatherAlert.getHumidityValue();

            if(humidityCondition == WeatherAlert.CheckCondition.ABOVE) {
                mFragment.setHumidityCondition(RB_ABOVE_BELOW[0]);
            }
            else {
                mFragment.setHumidityCondition(RB_ABOVE_BELOW[1]);
            }
            mFragment.setHumidityValue(humidityValue);
            mFragment.setHumidityCheckbox(true);
        }
        else {
            mFragment.setHumidityCheckbox(false);
        }
    }

    private void setTemperatureDisplay() {

        if(mWeatherAlert.getCheckTemp()) {
            WeatherAlert.CheckCondition tempCondition = mWeatherAlert.getCheckTempCondition();
            double tempValue = mWeatherAlert.getTempValue();

            if(tempCondition == WeatherAlert.CheckCondition.ABOVE) {
                mFragment.setTemperatureCondition(RB_ABOVE_BELOW[0]);
            }
            else {
                mFragment.setTemperatureCondition(RB_ABOVE_BELOW[1]);
            }
            mFragment.setTemperatureValue(tempValue);
            mFragment.setTemperatureCheckbox(true);
        }
        else {
            mFragment.setTemperatureCheckbox(false);
        }
    }

    private void setWindSpeedDisplay() {

        if(mWeatherAlert.getCheckWindSpeed()) {
            WeatherAlert.CheckCondition windSpeedCondition = mWeatherAlert.getCheckWindSpeedCondition();
            double windSpeedValue = mWeatherAlert.getWindSpeedValue();

            if(windSpeedCondition == WeatherAlert.CheckCondition.ABOVE) {
                mFragment.setWindSpeedCondition(RB_ABOVE_BELOW[0]);
            }
            else if(windSpeedCondition == WeatherAlert.CheckCondition.BELOW) {
                mFragment.setWindSpeedCondition(RB_ABOVE_BELOW[1]);
            }
            mFragment.setWindSpeedValue(windSpeedValue);
            mFragment.setWindSpeedCheckbox(true);
        }
        else {
            mFragment.setWindSpeedCheckbox(false);
        }
    }

    private void setWeatherStation() {
        Sensor sensor = AgBaseDatabaseManager.getInstance()
                .readSensorWithGuid(mWeatherAlert.getDeviceGuid());

        if(sensor != null) {
            mFragment.setWeatherStation(sensor);
        }
    }
    // endregion

    private void displayConfirmDialog() {

        AlertSummaryGenerator summaryGenerator = new AlertSummaryGenerator();
        Sensor sensor = AgBaseDatabaseManager.getInstance().readSensorWithGuid(mWeatherAlert.getDeviceGuid());
        String weatherAlertSummary = sensor.name + "\n" + summaryGenerator.writeAlertSummary(mWeatherAlert);
        ConfirmAlertDialog dialog = new ConfirmAlertDialog(this, mWeatherAlert.getName(), weatherAlertSummary);
        dialog.getDialog().show();

    }

    private void updateAlert() {
        AlertDatabaseManager.getInstance().updateWeatherAlert(mWeatherAlert);
        Toast.makeText(this, EDIT_CONDITION_MSG, Toast.LENGTH_SHORT);
        finish();
    }

    // region listeners

    /**
     * This function is called when the fragment creates a weather alert.
     */
    @Override
    public void getWeatherAlert(WeatherAlert weatherAlert) {
        if(weatherAlert != null) {
            int weatherAlertId = mWeatherAlert.getId();
            mWeatherAlert = weatherAlert;
            mWeatherAlert.setId(weatherAlertId);
            displayConfirmDialog();
        }
        else {
            Toast.makeText(this, FAIL_NO_CONDITION_MSG, Toast.LENGTH_SHORT);
        }
    }

    /**
     * This function is called when the used clicks the cancel button on the fragment
     */
    @Override
    public void cancelBtnClicked() {
        finish();
    }

    /**
     * This function is called when a user confirms a new weather alert in the confirm dialog
     */
    @Override
    public void confirmOk() {
        updateAlert();
    }

    @Override
    public void cancelClick() {
        //do NOTHING!
    }
    // endregion
}