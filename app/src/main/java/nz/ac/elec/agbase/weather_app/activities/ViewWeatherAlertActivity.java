package nz.ac.elec.agbase.weather_app.activities;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import nz.ac.elec.agbase.weather_app.WeatherAppActivity;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.StartActivityHandler;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.create_alert_dialogs.ConfirmAlertDialog;
import nz.ac.elec.agbase.weather_app.fragments.AlertDisplayFragment;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 28/04/16.
 */
public class ViewWeatherAlertActivity extends WeatherAppActivity
            implements AlertDisplayFragment.IAlertDisplayFragment,
            ConfirmAlertDialog.IConfirmAlertDialog {

    private final String TAG = "ViewAlertActivity";
    private final String TOOLBAR_TITLE = "Weather Alert";

    private final String Delete_WEATHER_ALERT_TITLE = "Delete Weather Alert";
    private final String DELETE_WEATHER_ALERT_MSG = "Are you sure you want to delete ";

    public static String ARGS_WEATHER_ALERT = "nz.ac.elec.agbase.weather_app.activities.ViewWeatherAlertActivity.ARGS_WEATHER_ALERT";

    private Toolbar toolbar;
    private TextView mAlertName;

    private WeatherAlert mWeatherAlert;

    private AlertDisplayFragment alertDisplayFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generic_activity_layout);

        initToolbar();
        initTextView();
        initContentFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();

        int weatherAlertId = getIntent().getIntExtra(ARGS_WEATHER_ALERT, -1);
        if(weatherAlertId == -1) {
            finish();
        }
        else {
            mWeatherAlert = AlertDatabaseManager.getInstance().readWeatherAlert(weatherAlertId);

            if(mWeatherAlert!= null) {
                if (mWeatherAlert.getName() != null) {
                    mAlertName.setText(mWeatherAlert.getName());
                }
                alertDisplayFragment.setWeatherAlert(mWeatherAlert);
            }
            else {
                finish();
            }
        }
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

    private void initContentFragment() {
        alertDisplayFragment = new AlertDisplayFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, alertDisplayFragment);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }
    private void initTextView() {
        mAlertName = (TextView)findViewById(R.id.title_textView);
    }
    // endregion

    private void displayDeleteWeatherAlertDialog() {
        ConfirmAlertDialog deleteDialog = new ConfirmAlertDialog(this, Delete_WEATHER_ALERT_TITLE,
                DELETE_WEATHER_ALERT_MSG + mAlertName.getText() + "?");
        deleteDialog.getDialog().show();
    }

    private void deleteWeatherAlert() {
        AlertDatabaseManager.getInstance().deleteWeatherAlert(mWeatherAlert.getId());
        finish();
    }

    private void displayEditWeatherAlert(WeatherAlert alert) {
        StartActivityHandler.startEditWeatherAlertActivity(this, alert.getId());
    }

    // region button click listeners
    @Override
    public void onClickEditButton(WeatherAlert alert) {
        displayEditWeatherAlert(alert);
    }

    @Override
    public void onClickDeleteButton(WeatherAlert alert) {
        displayDeleteWeatherAlertDialog();
    }
    // endregion

    // delete weather alert confirmed listener
    @Override
    public void confirmOk() {
        deleteWeatherAlert();
    }
}