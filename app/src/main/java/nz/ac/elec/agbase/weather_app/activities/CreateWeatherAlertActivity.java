package nz.ac.elec.agbase.weather_app.activities;

import android.support.v4.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.WeatherAppActivity;
import nz.ac.elec.agbase.weather_app.AlertSummaryGenerator;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import nz.ac.elec.agbase.weather_app.fragments.WeatherAlertFormFragment;
import nz.ac.elec.agbase.weather_app.services.WeatherAlertService;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.dialogs.ConfirmAlertDialog;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * CreateWeatherAlertActivity.java
 * Created by tm on 23/03/16.
 */
public class CreateWeatherAlertActivity extends WeatherAppActivity implements WeatherAlertFormFragment.IWeatherAlertFormFragment,
            ConfirmAlertDialog.IConfirmAlertDialog {

    private final String TAG = "CreateWeatherAlert";

    // region constants
    private final String TOOLBAR_TITLE                          = "New Weather Alert";

    private final String FAIL_NO_CONDITION_MSG                  = "Failed to create Weather Alert.  " +
            "Weather alert must have at least 1 condition";
    private final String CREATE_CONDITION_MSG                   = "Created new Weather Alert";

    // endregion

    private Toolbar toolbar;
    private WeatherAlertFormFragment mFragment;
    private WeatherAlert weatherAlertBuffer;

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

        intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherSyncAdapter.STATION_UPDATE);
        registerReceiver(broadcastReceiver, intentFilter);
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

    public void startWeatherAlertService() {
        Intent weatherServiceIntent = new Intent(this, WeatherAlertService.class);
        startService(weatherServiceIntent);
    }

    /**
     * Displays a dialog to confirm the users decision to create
     * a new weather alert.
     */
    private void displayConfirmDialog() {

        if(weatherAlertBuffer != null) {
            // get weather station to monitor for alerts
            Sensor sensor = AgBaseDatabaseManager.getInstance().readSensorWithGuid(weatherAlertBuffer.getDeviceGuid());

            // create a summary of the alert
            AlertSummaryGenerator summaryGenerator = new AlertSummaryGenerator();
            String weatherAlertSummary = sensor.name + "\n" + summaryGenerator.writeAlertSummary(weatherAlertBuffer);

            // display dialog
            ConfirmAlertDialog dialog = new ConfirmAlertDialog(this, weatherAlertBuffer.getName(), weatherAlertSummary);
            dialog.getDialog().show();
        }
    }

    private void saveWeatherAlert() {
        if(weatherAlertBuffer != null) {

            AlertDatabaseManager.getInstance().createAlert(weatherAlertBuffer);
            weatherAlertBuffer = null;
            startWeatherAlertService();
            Toast.makeText(this, CREATE_CONDITION_MSG, Toast.LENGTH_SHORT);
        }
    }

    // region listeners

    /**
     * This function is called when the fragment creates a weather alert.
     */
    @Override
    public void getWeatherAlert(WeatherAlert weatherAlert) {
        weatherAlertBuffer = null;

        if(weatherAlert != null) {
            weatherAlertBuffer = weatherAlert;
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
        saveWeatherAlert();
    }

    /**
     * This function is called when a user clicks the cancel button in the confirm dialog
     */
    @Override
    public void cancelClick() {
        weatherAlertBuffer = null;
    }
    // endregion

}
