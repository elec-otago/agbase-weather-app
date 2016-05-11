package nz.ac.elec.agbase.weather_app.activities;

import android.accounts.Account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.WeatherAppActivity;
import nz.ac.elec.agbase.weather_app.AgBaseAccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.StartActivityHandler;
import nz.ac.elec.agbase.weather_app.dialogs.ConfirmAlertDialog;
import nz.ac.elec.agbase.weather_app.dialogs.EditDeleteAlertDialog;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;
import nz.ac.elec.agbase.weather_app.services.WeatherAlertService;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.fragments.AlertsDisplayFragment;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;


/**
 * Created by tm on 28/04/16.
 */
public class ViewWeatherAlertsActivity extends WeatherAppActivity
            implements AlertsDisplayFragment.IAlertsDisplayFragment,
            EditDeleteAlertDialog.IEditDeleteAlertDialog,
            ConfirmAlertDialog.IConfirmAlertDialog {

    private final String TAG = "ViewAlertsActivity";
    private final String TOOLBAR_TITLE = "Weather Alerts";
    private final String HEADER_TITLE = "Alerts";
    private final String Delete_WEATHER_ALERT_TITLE = "Delete Weather Alert";
    private final String DELETE_WEATHER_ALERT_MSG = "Are you sure you want to delete ";

    private Toolbar toolbar;

    // navigation drawer
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private AlertsDisplayFragment alertsDisplay;

    WeatherAlert deleteAlert;

    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_activity_layout_drawer);

        // get last logged in account
        mAccount = getIntent().getParcelableExtra(getString(R.string.ARGS_ACCOUNT));

        if(mAccount == null) {
            AccountWorker worker = new AgBaseAccountWorker(this, getString(R.string.AGBASE_ACCOUNT));
            mAccount = worker.getLastAccount();
        }

        // If an account hasn't been found, redirect to login activity.
        if(mAccount == null) {
            StartActivityHandler.startLoginActivity(this);
            finish();
        }
        else {
            initToolbar();
            initNavigationView();
            initContentFragment();

            if(!isDeviceTablet()) {

                initHeader();
                initDrawerLayout();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContent();

        if(AlertDatabaseManager.getInstance().getAlertCount() > 0) {
            Intent weatherServiceIntent = new Intent(this, WeatherAlertService.class);
            startService(weatherServiceIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // region initialization
    private void initToolbar() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(TOOLBAR_TITLE);
        setSupportActionBar(toolbar);
    }

    private void initNavigationView() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

    }

    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.openDrawer, R.string.closeDrawer);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
    }

    private void initHeader() {
        TextView header = (TextView)findViewById(R.id.title_textView);
        header.setText(HEADER_TITLE);
    }

    private void initContentFragment() {
        alertsDisplay = new AlertsDisplayFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, alertsDisplay);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
    // endregion

    /**
     * Gets list of alerts for content fragment
     */
    private void getContent() {
        List<WeatherAlert> alerts = AlertDatabaseManager.getInstance().readWeatherAlerts();
        alertsDisplay.setWeatherAlerts(alerts);
    }

    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            if(drawerLayout != null) {
                drawerLayout.closeDrawers();
            }
            switch(menuItem.getItemId()) {
                case R.id.weather_report_item:
                    StartActivityHandler.startWeatherActivity(ViewWeatherAlertsActivity.this, mAccount);
                    finish();
                    break;
                case R.id.weather_alert_create_item:
                    StartActivityHandler.startCreateWeatherAlertActivity(ViewWeatherAlertsActivity.this, mAccount, null);
                    break;
                case R.id.weather_alert_logout_item:
                    PreferenceHandler.getInstance().setAccountLoggedIn(false);
                    PreferenceHandler.getInstance().setLastLoginAccount(null, -1);
                    StartActivityHandler.startLoginActivity(ViewWeatherAlertsActivity.this);
                    finish();
                default:
                    Log.d(TAG, "not found");
                    break;
            }
            return true;
        }
    };

    @Override
    public void onWeatherAlertItemClick(WeatherAlert alert) {
        StartActivityHandler.startViewWeatherAlertActivity(this, alert.getId());
    }

    @Override
    public void onWeatherAlertItemLongClick(WeatherAlert alert) {
        EditDeleteAlertDialog dialog = new EditDeleteAlertDialog(this, alert);
        dialog.getDialog().show();
    }

    @Override
    public void onWeatherAlertNewAlertClick() {
        StartActivityHandler.startCreateWeatherAlertActivity(this, mAccount, null);
    }

    @Override
    public void deleteAlert(WeatherAlert alert) {
        deleteAlert = alert;

        if(deleteAlert != null) {
            ConfirmAlertDialog deleteDialog = new ConfirmAlertDialog(this, Delete_WEATHER_ALERT_TITLE,
                    DELETE_WEATHER_ALERT_MSG + deleteAlert.getName() + "?");
            deleteDialog.getDialog().show();
        }
    }

    @Override
    public void editAlert(WeatherAlert alert) {
        StartActivityHandler.startEditWeatherAlertActivity(this, alert.getId());
    }

    @Override
    public void confirmOk() {
        if(deleteAlert != null) {
            AlertDatabaseManager.getInstance().deleteWeatherAlert(deleteAlert.getId());
            getContent();
        }
        deleteAlert = null;
    }

    @Override
    public void cancelClick() {

    }
}