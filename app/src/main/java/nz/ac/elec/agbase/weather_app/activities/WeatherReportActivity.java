package nz.ac.elec.agbase.weather_app.activities;

import android.accounts.Account;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorCategory;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorType;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.AgBaseAccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.StartActivityHandler;
import nz.ac.elec.agbase.weather_app.WeatherAlertService;
import nz.ac.elec.agbase.weather_app.agbase_sync.SyncAdapterHandler;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.fragments.WeatherDisplayFragment;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;

/**
 * Created by tm on 18/04/16.
 */
public class WeatherReportActivity extends AppCompatActivity implements WeatherDisplayFragment.OnFragmentInteractionListener {

    private final String TAG = "WeatherReportActivity";
    private final String TOOLBAR_TITLE = "Weather";
    private final String WAIT_SYNC_MSG = "Please wait while we get your account details from AgBase...";

    private Toolbar toolbar;

    // navigation drawer
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    // weather station spinner
    private Spinner weatherStationSpinner;
    private ArrayAdapter weatherStationArrayAdapter;
    private List<Sensor> weatherStationList;

    // weather now display
    private WeatherDisplayFragment weatherDisplay;

    private Sensor mSensor;

    private Handler getWeatherHandler;

    private ProgressDialog mProgressDialog;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_weather_activity);

        // get last logged in account
        mAccount = getIntent().getParcelableExtra(getString(R.string.ARGS_ACCOUNT));

        if (mAccount == null) {
            AccountWorker worker = new AgBaseAccountWorker(this, getString(R.string.AGBASE_ACCOUNT));
            mAccount = worker.getLastAccount();
        }

        // If an account hasn't been found, redirect to login activity.
        if (mAccount == null) {
            StartActivityHandler.startLoginActivity(this);
            finish();
        } else {
            initToolbar();
            initNavigationView();
            initDrawerLayout();
            initWeatherDisplay();
            initWeatherStationSpinner();

            PreferenceHandler prefs = PreferenceHandler.getInstance();
            if (!prefs.getInitCompletePreference(mAccount)) {
                prefs.setPerformInitPreference(mAccount, true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherSyncAdapter.SYNC_FINISHED);
        intentFilter.addAction(WeatherSyncAdapter.WEATHER_UPDATE);
        registerReceiver(broadcastReceiver, intentFilter);

        if (PreferenceHandler.getInstance().getPerformInitPreference(mAccount)) {

            if (mProgressDialog == null) {
                initProgressDialog();
            }
            SyncAdapterHandler syncAdapterHandler = new SyncAdapterHandler(getString(R.string.content_authority));
            syncAdapterHandler.performInitSync(this, mAccount);
        } else {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            setupWeatherStationList();
            setupWeatherRequests();
        }

        if(AlertDatabaseManager.getInstance().getAlertCount() > 0) {
            Intent weatherServiceIntent = new Intent(this, WeatherAlertService.class);
            startService(weatherServiceIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(broadcastReceiver);
        if (getWeatherHandler != null) {
            getWeatherHandler.removeCallbacks(getWeatherRunnable);
        }
    }

    // region initialization
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        setSupportActionBar(toolbar);
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void initWeatherDisplay() {
        weatherDisplay = new WeatherDisplayFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.display_weather_activity_fragment_container, weatherDisplay);
        transaction.commit();
        getFragmentManager().executePendingTransactions();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(WAIT_SYNC_MSG);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void initWeatherStationSpinner() {
        weatherStationSpinner = (Spinner) findViewById(R.id.display_weather_activity_weather_station_dropdown);
        weatherStationList = new ArrayList<>();
        weatherStationArrayAdapter = new ArrayAdapter(this, R.layout.report_spinner_item, R.id.report_spinner_item_textview, weatherStationList);
        //weatherStationArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        weatherStationSpinner.setAdapter(weatherStationArrayAdapter);
        weatherStationSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSensor = weatherStationList.get(position);
                if (getWeatherHandler == null) {
                    getWeatherHandler = new Handler();
                } else {
                    // stop timer
                    getWeatherHandler.removeCallbacks(getWeatherRunnable);
                }
                // start timer with immediate runnable execution
                getWeatherHandler.post(getWeatherRunnable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    // endregion

    private void setupWeatherStationList() {
        AgBaseDatabaseManager db = AgBaseDatabaseManager.getInstance();
        weatherStationList.clear();
        // add sensors
        SensorCategory weatherStation = db.readSensorCategoryWithName("Weather Station");
        List<SensorType> sensorTypes = db.readSensorTypesWithCategory(weatherStation.id);

        for (SensorType sensorType : sensorTypes) {
            List<Sensor> sensors = db.readSensorsWithType(sensorType.id);
            weatherStationList.addAll(sensors);
        }
        weatherStationArrayAdapter.notifyDataSetChanged();
    }

    private void setupWeatherRequests() {
        if (weatherStationSpinner.getAdapter().getCount() > 0) {
            mSensor = (Sensor) weatherStationSpinner.getItemAtPosition(0);

            if (getWeatherHandler == null) {
                getWeatherHandler = new Handler();
            } else {
                // stop timer
                getWeatherHandler.removeCallbacks(getWeatherRunnable);
            }
            // start timer with immediate runnable execution
            getWeatherHandler.post(getWeatherRunnable);
        }
    }

    /**
     * Updates the UI when an initialization sync has been completed
     */
    private void onInitSyncFinished(Intent intent) {

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        // set default data if database has finished setup
        if (intent.getBooleanExtra(getString(R.string.ARGS_DB_INIT), false)) {
            setupWeatherStationList();
            setupWeatherRequests();
        }
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            drawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.weather_report_item:
                    break;
                case R.id.weather_alert_create_item:
                    startCreateWeatherActivity();
                    break;
                case R.id.weather_alert_view_item:
                    startViewWeatherActivity();
                default:
                    Log.d(TAG, "not found");
                    break;
            }
            return true;
        }
    };

    private void startCreateWeatherActivity() {
        StartActivityHandler.startCreateWeatherAlertActivity(this, mAccount, null);
    }

    private void startViewWeatherActivity() {
        StartActivityHandler.startViewWeatherAlertsActivity(this, mAccount);
        finish();
    }

    // region broadcast receiver
    private IntentFilter intentFilter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (WeatherSyncAdapter.SYNC_FINISHED.equals(action)) {
                onInitSyncFinished(intent);
            }
            if (WeatherSyncAdapter.WEATHER_UPDATE.equals(action)) {
                Weather weather = intent.getExtras().getParcelable(WeatherSyncAdapter.ARGS_GET_WEATHER);

                weatherDisplay.displayWeatherMeasurement(weather);
            }
        }
    };
    // endregion

    // region get weather request runnable
    private final Runnable getWeatherRunnable = new Runnable() {
        @Override
        public void run() {
            if (mSensor != null) {
                SyncAdapterHandler syncAdapterHandler = new SyncAdapterHandler(getString(R.string.content_authority));
                Log.d(TAG, "GET WEATHER!!!!!!!!!!!");
                syncAdapterHandler.getLastWeatherMeasurement(getApplicationContext(), mAccount, mSensor.guid);
            }
            getWeatherHandler.postDelayed(this, (60 * 1000));
        }
    };
    // endregion
}