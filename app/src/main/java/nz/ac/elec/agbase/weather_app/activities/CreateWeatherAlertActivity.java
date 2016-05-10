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


    private void createWeatherAlert() {
        // create weather alert

        if (weatherAlertBuffer != null) {
            AlertDatabaseManager db = AlertDatabaseManager.getInstance();

            int alertTotal = db.getAlertCount();
            db.createAlert(weatherAlertBuffer);

            // start service if this is first weather alert request.
            if (alertTotal < 1) {
                startWeatherAlertService();
            }
            Toast.makeText(CreateWeatherAlertActivity.this, CREATE_CONDITION_MSG, Toast.LENGTH_SHORT).show();
            weatherAlertBuffer = null;
        }
    }

    public void startWeatherAlertService() {
        Intent weatherServiceIntent = new Intent(this, WeatherAlertService.class);
        startService(weatherServiceIntent);
    }

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

    private void displayConfirmDialog() {

        if(weatherAlertBuffer != null) {
            AlertSummaryGenerator summaryGenerator = new AlertSummaryGenerator();
            Sensor sensor = AgBaseDatabaseManager.getInstance().readSensorWithGuid(weatherAlertBuffer.getDeviceGuid());
            String weatherAlertSummary = sensor.name + "\n" + summaryGenerator.writeAlertSummary(weatherAlertBuffer);
            ConfirmAlertDialog dialog = new ConfirmAlertDialog(this, weatherAlertBuffer.getName(), weatherAlertSummary);
            dialog.getDialog().show();
        }
    }

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

    @Override
    public void cancelBtnClicked() {
        finish();
    }

    @Override
    public void confirmOk() {
        if(weatherAlertBuffer != null) {
            AlertDatabaseManager.getInstance().createAlert(weatherAlertBuffer);
            weatherAlertBuffer = null;
            Toast.makeText(this, CREATE_CONDITION_MSG, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void cancelClick() {
        weatherAlertBuffer = null;
    }
    // endregion

}
