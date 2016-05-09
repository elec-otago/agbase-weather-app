package nz.ac.elec.agbase.weather_app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorCategory;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorType;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.WeatherAppActivity;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.dialogs.AirPressureConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AirPressureValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AlertDescriptionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AlertNameDialog;
import nz.ac.elec.agbase.weather_app.dialogs.HumidityConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.HumidityValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.RainConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.RainValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.SnowConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.SnowValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.TempConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.TempValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.WindSpeedConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.WindSpeedValueDialog;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 3/05/16.
 */
public class EditWeatherAlertActivity extends WeatherAppActivity implements AlertNameDialog.IAlertNameDialog,
        AlertDescriptionDialog.IAlertDescriptionDialog, TempValueDialog.ITempValueDialog,
        TempConditionDialog.ITempConditionDialog, HumidityValueDialog.IHumidityValueDialog,
        HumidityConditionDialog.IHumidityConditionDialog, WindSpeedConditionDialog.IWindSpeedConditionDialog,
        WindSpeedValueDialog.IWindSpeedValueDialog, AirPressureConditionDialog.IAirPressureConditionDialog,
        AirPressureValueDialog.IAirPressureValueDialog, RainConditionDialog.IRainConditionDialog,
        RainValueDialog.IRainValueDialog, SnowConditionDialog.ISnowConditionDialog, SnowValueDialog.ISnowValueDialog {

    private final String TAG =  "EditWeatherAlert";

    // region constants
    private final String TOOLBAR_TITLE                          = "Update Weather Alert";
    private final String TEMP_CONDITION_DIALOG_TITLE            = "Select Temperature Condition";
    private final String ALERT_NAME_DIALOG_TITLE                = "Enter Alert Name";
    private final String ALERT_DESC_DIALOG_TITLE                = "Enter Alert Description";
    private final String TEMP_VALUE_DIALOG_TITLE                = "Enter Temperature Value";
    private final String HUMIDITY_VALUE_DIALOG_TITLE            = "Enter Humidity Value";
    private final String HUMIDITY_CONDITION_DIALOG_TITLE        = "Select Humidity Condition";
    private final String WIND_SPEED_CONDITION_DIALOG_TITLE      = "Select Wind Speed Condition";
    private final String WIND_SPEED_VALUE_DIALOG_TITLE          = "Enter Wind Speed Value";
    private final String AIR_PRESSURE_CONDITION_DIALOG_TITLE    = "Select Air Pressure Condition";
    private final String AIR_PRESSURE_VALUE_DIALOG_TITLE        = "Enter Air Pressure Value";
    private final String RAIN_CONDITION_DIALOG_TITLE            = "Select Rain Condition";
    private final String RAIN_VALUE_DIALOG_TITLE                = "Enter Rain Value";
    private final String SNOW_CONDITION_DIALOG_TITLE            = "Select Snow Condition";
    private final String SNOW_VALUE_DIALOG_TITLE                = "Select Snow Value";

    private final String EDIT_CONDITION_MSG                     = "Updated Weather Alert";
    private final String FAIL_NO_CONDITION_MSG                  = "Failed to update Weather Alert.  " +
            "A weather alert needs at least 1 trigger condition";

    private final String[] RB_ABOVE_BELOW                       = new String[] {"above", "below"};
    private final String[] RB_SNOW_CONDITION                    = new String[] {"intensity", "snowing"};
    private final String[] RB_RAIN_CONDITION                    = new String[] {"intensity", "raining"};

    public static String ARGS_WEATHER_ALERT                     = "nz.ac.elec.agbase.weather_app.activities.EditWeatherAlert.ARGS_WEATHER_ALERT";
    // endregion

    private Toolbar toolbar;

    // weather station spinner
    private Spinner weatherStationSpinner;
    private ArrayAdapter weatherStationArrayAdapter;
    private List<Sensor> weatherStationList;

    // region alert name/description
    private RelativeLayout nameInput, descriptionInput;
    private TextView nameOutput, descriptionOutput;
    // endregion

    // region temperature
    private CheckBox tempCb;
    private RelativeLayout useTempInput, tempConditionInput, tempValueInput;
    private TextView tempConditionOutput, tempValueOutput;
    // endregion

    // region humidity
    private CheckBox humidityCb;
    private RelativeLayout useHumidityInput, humidityConditionInput, humidityValueInput;
    private TextView humidityConditionOutput, humidityValueOutput;
    // endregion

    // region wind speed
    private CheckBox windSpeedCb;
    private RelativeLayout useWindSpeedInput, windSpeedConditionInput, windSpeedValueInput;
    private TextView windSpeedConditionOutput, windSpeedValueOutput;
    // endregion

    // region rain
    private CheckBox rainCb;
    private RelativeLayout useRainInput, rainConditionInput, rainValueInput;
    private TextView rainConditionOutput, rainValueOutput;
    // endregion

    // region snow
    private CheckBox snowCb;
    private RelativeLayout useSnowInput, snowConditionInput, snowValueInput;
    private TextView snowConditionOutput, snowValueOutput;
    // endregion

    // region air pressure
    private CheckBox airPressureCb;
    private RelativeLayout useAirPressureInput, airPressureConditionInput, airPressureValueInput;
    private TextView airPressureConditionOutput, airPressureValueOutput;
    // endregion

    private Button saveBtn, cancelBtn;


    private WeatherAlert mWeatherAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_alert_form);
        initToolbar();
        initAlertNameUI();
        initTempUI();
        initWindSpeedUI();
        initRainUI();
        initSnowUI();
        initHumidityUI();
        initAirPressureUI();
        initWeatherStationSpinner();
        initButtons();
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
            getWeatherStationList();
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

    private void initAlertNameUI() {
        // init name and description outputs
        nameOutput = (TextView)findViewById(R.id.create_alert_form_alert_name_desc);
        descriptionOutput = (TextView)findViewById(R.id.create_alert_form_alert_desc_desc);

        // init name input
        nameInput = (RelativeLayout)findViewById(R.id.create_alert_form_temp_name_container);
        nameInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertNameDialog();
            }
        });

        // init description input
        descriptionInput = (RelativeLayout)findViewById(R.id.create_alert_form_temp_desc_container);
        descriptionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDescriptionDialog();
            }
        });
    }

    private void initTempUI() {
        // init condition and value outputs
        tempConditionOutput = (TextView)findViewById(R.id.create_alert_form_temp_condition_desc);
        tempValueOutput = (TextView)findViewById(R.id.create_alert_form_temp_value_desc);

        // init use temperature condition switch
        tempCb = (CheckBox) findViewById(R.id.create_alert_form_temp_select_checkbox);
        useTempInput = (RelativeLayout) findViewById(R.id.create_alert_form_temp_select_container);
        useTempInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempCb.setChecked(!tempCb.isChecked());
            }
        });
        // init chose temperature condition
        tempConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_temp_condition_container);
        tempConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTempConditionDialog();
            }
        });
        // init temperature value
        tempValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_temp_value_container);
        tempValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTempValueDialog();
            }
        });
    }

    private void initHumidityUI() {
        // init condition and value outputs
        humidityConditionOutput = (TextView)findViewById(R.id.create_alert_form_humidity_condition_desc);
        humidityValueOutput = (TextView)findViewById(R.id.create_alert_form_humidity_value_desc);
        // init use humidity switch
        humidityCb = (CheckBox)findViewById(R.id.create_alert_form_humidity_select_checkbox);
        useHumidityInput = (RelativeLayout)findViewById(R.id.create_alert_form_humidity_select_container);
        useHumidityInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                humidityCb.setChecked(!humidityCb.isChecked());
            }
        });
        // init choose humidity condition
        humidityConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_humidity_condition_container);
        humidityConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHumidityConditionDialog();
            }
        });
        // init humidity value
        humidityValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_humidity_value_container);
        humidityValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHumidityValueDialog();
            }
        });
    }

    private void initWindSpeedUI() {
        // init condition and value outputs
        windSpeedConditionOutput = (TextView)findViewById(R.id.create_alert_form_wind_speed_condition_desc);
        windSpeedValueOutput = (TextView)findViewById(R.id.create_alert_form_wind_speed_value_desc);

        // init use wind speed condition switch
        windSpeedCb = (CheckBox)findViewById(R.id.create_alert_form_wind_speed_select_checkbox);
        useWindSpeedInput = (RelativeLayout)findViewById(R.id.create_alert_form_wind_speed_select_container);
        useWindSpeedInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                windSpeedCb.setChecked(!windSpeedCb.isChecked());
            }
        });
        // init choose wind speed condition
        windSpeedConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_wind_speed_condition_container);
        windSpeedConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWindSpeedConditionDialog();
            }
        });
        // init wind speed value
        windSpeedValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_wind_speed_value_container);
        windSpeedValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWindSpeedValueDialog();
            }
        });
    }

    private void initAirPressureUI() {
        //init condition and value outputs
        airPressureConditionOutput = (TextView)findViewById(R.id.create_alert_form_air_pressure_condition_desc);
        airPressureValueOutput = (TextView)findViewById(R.id.create_alert_form_air_pressure_value_desc);
        // init use air pressure switch
        airPressureCb = (CheckBox)findViewById(R.id.create_alert_form_air_pressure_select_checkbox);
        useAirPressureInput = (RelativeLayout)findViewById(R.id.create_alert_form_air_pressure_select_container);
        useAirPressureInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                airPressureCb.setChecked(!airPressureCb.isChecked());
            }
        });
        // init choose air pressure condition
        airPressureConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_air_pressure_condition_container);
        airPressureConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAirPressureConditionDialog();
            }
        });
        // init choose air pressure value
        airPressureValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_air_pressure_value_container);
        airPressureValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAirPressureValueDialog();
            }
        });
    }

    private void initRainUI() {
        // init condition and value outputs
        rainConditionOutput = (TextView)findViewById(R.id.create_alert_form_rain_condition_desc);
        rainValueOutput = (TextView)findViewById(R.id.create_alert_form_rain_value_desc);

        // init use rain switch
        rainCb = (CheckBox)findViewById(R.id.create_alert_form_rain_select_checkbox);
        useRainInput = (RelativeLayout) findViewById(R.id.create_alert_form_rain_select_container);
        useRainInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainCb.setChecked(!rainCb.isChecked());
            }
        });
        // init choose rain condition
        rainConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_rain_condition_container);
        rainConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRainConditionDialog();
            }
        });
        // init choose rain value
        rainValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_rain_value_container);
        rainValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRainValueDialog();
            }
        });
    }

    private void initSnowUI() {
        // init condition and value outputs
        snowConditionOutput = (TextView)findViewById(R.id.create_alert_form_snow_condition_desc);
        snowValueOutput = (TextView)findViewById(R.id.create_alert_form_snow_value_desc);

        // init use snow switch
        snowCb = (CheckBox)findViewById(R.id.create_alert_form_snow_select_checkbox);
        useSnowInput = (RelativeLayout)findViewById(R.id.create_alert_form_snow_select_container);
        useSnowInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                snowCb.setChecked(!snowCb.isChecked());
            }
        });
        // init choose snow condition
        snowConditionInput = (RelativeLayout)findViewById(R.id.create_alert_form_snow_condition_container);
        snowConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySnowConditionDialog();
            }
        });
        // init choose snow value
        snowValueInput = (RelativeLayout)findViewById(R.id.create_alert_form_snow_value_container);
        snowValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySnowValueDialog();
            }
        });
    }

    private void initButtons() {
        saveBtn = (Button)findViewById(R.id.alert_form_submit_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeatherAlert();
            }
        });

        cancelBtn = (Button)findViewById(R.id.alert_form_cancel_btn);
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initWeatherStationSpinner() {
        weatherStationSpinner = (Spinner) findViewById(R.id.create_alert_form_activity_weather_station_dropdown);
        weatherStationList = new ArrayList<Sensor>();
        weatherStationArrayAdapter = new ArrayAdapter(this, R.layout.alert_spinner_item, R.id.alert_spinner_item_textview, weatherStationList);
        weatherStationSpinner.setAdapter(weatherStationArrayAdapter);
    }
    // endregion

    // region dialog display
    private void displayAlertNameDialog() {
        String alertName = nameOutput.getText().toString();
        AlertNameDialog dialog = new AlertNameDialog(this, ALERT_NAME_DIALOG_TITLE, alertName);
        dialog.getDialog().show();
    }

    private void displayAlertDescriptionDialog() {
        String alertDescription = descriptionOutput.getText().toString();
        AlertDescriptionDialog dialog = new AlertDescriptionDialog(this, ALERT_DESC_DIALOG_TITLE, alertDescription);
        dialog.getDialog().show();
    }

    private void displayTempValueDialog() {
        String tempValue = tempValueOutput.getText().toString();
        TempValueDialog dialog = new TempValueDialog(this, TEMP_VALUE_DIALOG_TITLE, tempValue);
        dialog.getDialog().show();
    }

    private void displayTempConditionDialog() {
        String condition = tempConditionOutput.getText().toString();

        TempConditionDialog dialog = new TempConditionDialog
                (this, TEMP_CONDITION_DIALOG_TITLE, RB_ABOVE_BELOW, condition);
        dialog.getDialog().show();
    }

    private void displayHumidityConditionDialog() {
        String condition = humidityConditionOutput.getText().toString();

        HumidityConditionDialog dialog = new HumidityConditionDialog
                (this, HUMIDITY_CONDITION_DIALOG_TITLE, RB_ABOVE_BELOW, condition);
        dialog.getDialog().show();
    }

    private void displayHumidityValueDialog(){
        String humidityValue = humidityValueOutput.getText().toString();
        HumidityValueDialog dialog = new HumidityValueDialog(this, HUMIDITY_VALUE_DIALOG_TITLE, humidityValue);
        dialog.getDialog().show();
    }

    private void displayWindSpeedConditionDialog() {
        String condition = windSpeedConditionOutput.getText().toString();
        WindSpeedConditionDialog dialog = new WindSpeedConditionDialog(this,
                WIND_SPEED_CONDITION_DIALOG_TITLE, RB_ABOVE_BELOW, condition);
        dialog.getDialog().show();
    }

    private void displayWindSpeedValueDialog() {
        String windSpeedValue = windSpeedValueOutput.getText().toString();
        WindSpeedValueDialog dialog = new WindSpeedValueDialog(this, WIND_SPEED_VALUE_DIALOG_TITLE, windSpeedValue);
        dialog.getDialog().show();
    }

    private void displayAirPressureConditionDialog() {
        String condition = airPressureConditionOutput.getText().toString();
        AirPressureConditionDialog dialog = new AirPressureConditionDialog(this,
                AIR_PRESSURE_CONDITION_DIALOG_TITLE, RB_ABOVE_BELOW, condition);
        dialog.getDialog().show();
    }

    private void displayAirPressureValueDialog() {
        String airPressureValue = airPressureValueOutput.getText().toString();
        AirPressureValueDialog dialog = new AirPressureValueDialog(this, AIR_PRESSURE_VALUE_DIALOG_TITLE, airPressureValue);
        dialog.getDialog().show();
    }

    private void displayRainConditionDialog() {
        String condition = rainConditionOutput.getText().toString();
        RainConditionDialog dialog = new RainConditionDialog(this,
                RAIN_CONDITION_DIALOG_TITLE, RB_RAIN_CONDITION, condition);
        dialog.getDialog().show();
    }

    private void displayRainValueDialog() {
        String rainValue = rainValueOutput.getText().toString();
        RainValueDialog dialog = new RainValueDialog(this, RAIN_VALUE_DIALOG_TITLE, rainValue);
        dialog.getDialog().show();
    }

    private void displaySnowConditionDialog() {
        String condition = snowConditionOutput.getText().toString();
        SnowConditionDialog dialog = new SnowConditionDialog(this,
                SNOW_CONDITION_DIALOG_TITLE, RB_SNOW_CONDITION, condition);
        dialog.getDialog().show();
    }

    private void displaySnowValueDialog() {
        String snowValue = snowValueOutput.getText().toString();
        SnowValueDialog dialog = new SnowValueDialog(this, SNOW_VALUE_DIALOG_TITLE, snowValue);
        dialog.getDialog().show();
    }

    // endregion

    private void getWeatherStationList() {
        AgBaseDatabaseManager db = AgBaseDatabaseManager.getInstance();
        weatherStationList.clear();
        // add sensors
        SensorCategory weatherStation = db.readSensorCategoryWithName("Weather Station");
        List<SensorType> sensorTypes = db.readSensorTypesWithCategory(weatherStation.id);

        for(SensorType sensorType : sensorTypes) {
            List<Sensor> sensors = db.readSensorsWithType(sensorType.id);
            weatherStationList.addAll(sensors);
        }
        weatherStationArrayAdapter.notifyDataSetChanged();
    }

    private void displayAlertDetails() {
        setAlertNameDisplay();
        setAlertDescriptionDisplay();
        setTemperatureDisplay();
        setWindSpeedDisplay();
        setHumidityDisplay();
        setAirPressureDisplay();
        setRainDisplay();
        setSnowDisplay();
        setWeatherStation();
    }

    private void setAlertNameDisplay() {
        if(!TextUtils.isEmpty(mWeatherAlert.getName())) {
            nameOutput.setText(mWeatherAlert.getName());
        }
    }

    private void setAlertDescriptionDisplay() {
        if(!TextUtils.isEmpty(mWeatherAlert.getDescription())) {
            descriptionOutput.setText(mWeatherAlert.getDescription());
        }
    }

    private void setSnowDisplay() {
        if(mWeatherAlert.getCheckSnow()) {
            WeatherAlert.CheckCondition snowCondition = mWeatherAlert.getCheckSnowCondition();

            if(snowCondition == WeatherAlert.CheckCondition.IS_TRUE) {
                snowConditionOutput.setText(RB_SNOW_CONDITION[1]);
            }
            else if(snowCondition == WeatherAlert.CheckCondition.INTENSITY) {
                double snowValue = mWeatherAlert.getSnowIntensityValue();

                snowValueOutput.setText(String.valueOf(snowValue));
                snowConditionOutput.setText(RB_SNOW_CONDITION[0]);
            }
            snowCb.setChecked(true);
        } else {
            snowCb.setChecked(false);
        }
    }

    private void setRainDisplay() {
        if(mWeatherAlert.getCheckRain()) {
            WeatherAlert.CheckCondition rainCondition = mWeatherAlert.getCheckRainCondition();

            if(rainCondition == WeatherAlert.CheckCondition.IS_TRUE) {
                rainConditionOutput.setText(RB_RAIN_CONDITION[1]);
            }
            else if(rainCondition == WeatherAlert.CheckCondition.INTENSITY) {
                double rainValue = mWeatherAlert.getRainIntensityValue();

                rainValueOutput.setText(String.valueOf(rainValue));
                rainConditionOutput.setText(RB_RAIN_CONDITION[0]);
            }
            rainCb.setChecked(true);
        } else {
            rainCb.setChecked(false);
        }
    }

    private void setAirPressureDisplay() {
        if(mWeatherAlert.getCheckAirPressure()) {
            WeatherAlert.CheckCondition airPressureCondition = mWeatherAlert.getCheckAirPressureCondition();
            double airPressureValue = mWeatherAlert.getAirPressureValue();

            if(airPressureCondition == WeatherAlert.CheckCondition.ABOVE) {
                airPressureConditionOutput.setText(RB_ABOVE_BELOW[0]);
            }
            else if(airPressureCondition == WeatherAlert.CheckCondition.BELOW) {
                airPressureConditionOutput.setText(RB_ABOVE_BELOW[1]);
            }
            airPressureValueOutput.setText(String.valueOf(airPressureValue));
            airPressureCb.setChecked(true);
        }
        else {
            airPressureCb.setChecked(false);
        }
    }

    private void setHumidityDisplay() {
        if(mWeatherAlert.getCheckHumidity()) {
            WeatherAlert.CheckCondition humidityCondition = mWeatherAlert.getCheckHumidityCondition();
            double humidityValue = mWeatherAlert.getHumidityValue();

            if(humidityCondition == WeatherAlert.CheckCondition.ABOVE) {
                humidityConditionOutput.setText(RB_ABOVE_BELOW[0]);
            }
            else if(humidityCondition == WeatherAlert.CheckCondition.BELOW) {
                humidityConditionOutput.setText(RB_ABOVE_BELOW[1]);
            }
            humidityValueOutput.setText(String.valueOf(humidityValue));
            humidityCb.setChecked(true);
        }
        else {
            humidityCb.setChecked(false);
        }
    }

    private void setTemperatureDisplay() {
        if(mWeatherAlert.getCheckTemp()) {
            WeatherAlert.CheckCondition tempCondition = mWeatherAlert.getCheckTempCondition();
            double tempValue = mWeatherAlert.getTempValue();

            if(tempCondition == WeatherAlert.CheckCondition.ABOVE) {
                tempConditionOutput.setText(RB_ABOVE_BELOW[0]);
            }
            else if(tempCondition == WeatherAlert.CheckCondition.BELOW) {
                tempConditionOutput.setText(RB_ABOVE_BELOW[1]);
            }
            tempValueOutput.setText(String.valueOf(tempValue));
            tempCb.setChecked(true);
        }
        else {
            tempCb.setChecked(false);
        }
    }

    private void setWindSpeedDisplay() {
        if(mWeatherAlert.getCheckWindSpeed()) {
            WeatherAlert.CheckCondition windSpeedCondition = mWeatherAlert.getCheckWindSpeedCondition();
            double windSpeedValue = mWeatherAlert.getWindSpeedValue();

            if(windSpeedCondition == WeatherAlert.CheckCondition.ABOVE) {
                windSpeedConditionOutput.setText(RB_ABOVE_BELOW[0]);
            }
            else if(windSpeedCondition == WeatherAlert.CheckCondition.BELOW) {
                windSpeedConditionOutput.setText(RB_ABOVE_BELOW[1]);
            }
            windSpeedValueOutput.setText(String.valueOf(windSpeedValue));
            windSpeedCb.setChecked(true);
        }
        else {
            windSpeedCb.setChecked(false);
        }
    }

    private void setWeatherStation() {
        Sensor sensor = AgBaseDatabaseManager.getInstance()
                .readSensorWithGuid(mWeatherAlert.getDeviceGuid());

        if(sensor != null) {
            int position = weatherStationList.indexOf(sensor);
            weatherStationSpinner.setSelection(position);
        }
    }

    private void updateWeatherAlert() {
        int id = mWeatherAlert.getId();
        mWeatherAlert = buildWeatherAlert();
        if(mWeatherAlert != null) {
            mWeatherAlert.setId(id);
            AlertDatabaseManager.getInstance().updateWeatherAlert(mWeatherAlert);
            finish();
        }
    }
    private WeatherAlert buildWeatherAlert() {
        WeatherAlert alert = new WeatherAlert();
        int alertConditionCount = 0;

        if(tempCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckTemp(true);

            // get condition
            String tempCondition = tempConditionOutput.getText().toString();
            if(tempCondition.equals(RB_ABOVE_BELOW[0])) {
                alert.setCheckTempCondition(WeatherAlert.CheckCondition.ABOVE);
            }
            else {
                alert.setCheckTempCondition(WeatherAlert.CheckCondition.BELOW);
            }
            // get value
            double tempValue = 0;
            try { tempValue = Double.parseDouble(tempValueOutput.getText().toString()); }
            catch(NumberFormatException e) { e.printStackTrace(); }
            finally { alert.setTempValue(tempValue); }
        }

        if(humidityCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckHumidity(true);

            // get condition
            String humidityCondition = humidityConditionOutput.getText().toString();
            if(humidityCondition.equals(RB_ABOVE_BELOW[0])) {
                alert.setCheckHumidityCondition(WeatherAlert.CheckCondition.ABOVE);
            }
            else {
                alert.setCheckHumidityCondition(WeatherAlert.CheckCondition.BELOW);
            }
            // get value
            double humidityValue = 0;
            try { humidityValue = Double.parseDouble(humidityValueOutput.getText().toString()); }
            catch(NumberFormatException e) { e.printStackTrace(); }
            finally { alert.setHumidityValue(humidityValue); }

        }

        if(windSpeedCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckWindSpeed(true);

            // get condition
            String windSpeedCondition = windSpeedConditionOutput.getText().toString();
            if(windSpeedCondition.equals(RB_ABOVE_BELOW[0])) {
                alert.setCheckWindSpeedCondition(WeatherAlert.CheckCondition.ABOVE);
            }
            else {
                alert.setCheckWindSpeedCondition(WeatherAlert.CheckCondition.BELOW);
            }
            // get value
            double windSpeedValue = 0;
            try { windSpeedValue = Double.parseDouble(windSpeedValueOutput.getText().toString()); }
            catch(NumberFormatException e) { e.printStackTrace(); }
            finally { alert.setWindSpeedValue(windSpeedValue); }
        }

        if(airPressureCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckAirPressure(true);

            // get condition
            String airPressureCondition = airPressureConditionOutput.getText().toString();
            if(airPressureCondition.equals(RB_ABOVE_BELOW[0])) {
                alert.setCheckAirPressureCondition(WeatherAlert.CheckCondition.ABOVE);
            }
            else {
                alert.setCheckAirPressureCondition(WeatherAlert.CheckCondition.BELOW);
            }
            // get value
            double airPressureValue = 0;
            try { airPressureValue = Double.parseDouble(airPressureValueOutput.getText().toString()); }
            catch(NumberFormatException e) { e.printStackTrace(); }
            finally { alert.setAirPressureValue(airPressureValue); }
        }

        if(rainCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckRain(true);

            // get condition
            String rainCondition = rainConditionOutput.getText().toString();
            if(rainCondition.equals(RB_RAIN_CONDITION[0])) {
                alert.setCheckRainCondition(WeatherAlert.CheckCondition.INTENSITY);

                // get value
                double rainValue = 0;
                try{ rainValue = Double.parseDouble(rainValueOutput.getText().toString()); }
                catch(NumberFormatException e) { e.printStackTrace(); }
                finally { alert.setRainIntensityValue(rainValue); }
            }
            else {
                alert.setCheckRainCondition(WeatherAlert.CheckCondition.IS_TRUE);
            }
        }

        if(snowCb.isChecked()) {
            alertConditionCount++;
            alert.setCheckSnow(true);

            // get condition
            String snowCondition = snowConditionOutput.getText().toString();
            if(snowCondition.equals(RB_SNOW_CONDITION[0])) {
                alert.setCheckSnowCondition(WeatherAlert.CheckCondition.INTENSITY);

                // get value
                double snowValue = 0;
                try { snowValue = Double.parseDouble(snowValueOutput.getText().toString()); }
                catch(NumberFormatException e) { e.printStackTrace(); }
                finally { alert.setSnowIntensityValue(snowValue); }
            }
            else {
                alert.setCheckSnowCondition(WeatherAlert.CheckCondition.IS_TRUE);
            }
        }

        if(alertConditionCount == 0) {
            Toast.makeText(this, FAIL_NO_CONDITION_MSG, Toast.LENGTH_SHORT).show();
            return null;
        }

        alert.setName(nameOutput.getText().toString());
        alert.setDescription(descriptionOutput.getText().toString());
        String deviceGuid = weatherStationList.get(weatherStationSpinner.getSelectedItemPosition()).guid;
        alert.setDeviceGuid(deviceGuid);

        return alert;
    }

    // region broadcast receiver
    private IntentFilter intentFilter;
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(WeatherSyncAdapter.STATION_UPDATE.equals(action)) {
                // get the selected weather station
                Sensor selectedItem = (Sensor)weatherStationSpinner.getSelectedItem();
                // update weather station list
                getWeatherStationList();
                // restore selected item if possible
                int selectedItemIndex = weatherStationList.indexOf(selectedItem);
                if(selectedItemIndex != -1) {
                    weatherStationSpinner.setSelection(selectedItemIndex);
                }
            }
        }
    };
    // endregion

    // region dialog callback functions
    @Override
    public void getName(String name) {
        nameOutput.setText(name);
    }

    @Override
    public void getDescription(String description) {
        descriptionOutput.setText(description);
    }

    @Override
    public void getTempValue(double tempValue) {
        tempValueOutput.setText(String.valueOf(tempValue));
    }

    @Override
    public void getTempCondition(String condition) {
        tempConditionOutput.setText(condition);
    }

    @Override
    public void getHumidityValue(double humidityValue) {
        humidityValueOutput.setText(String.valueOf(humidityValue));
    }

    @Override
    public void getHumidityCondition(String condition) {
        humidityConditionOutput.setText(condition);
    }

    @Override
    public void getWindSpeedCondition(String condition) {
        windSpeedConditionOutput.setText(condition);
    }

    @Override
    public void getWindSpeedValue(double windSpeedValue) {
        windSpeedValueOutput.setText(String.valueOf(windSpeedValue));
    }

    @Override
    public void getAirPressureCondition(String condition) {
        airPressureConditionOutput.setText(condition);
    }

    @Override
    public void getAirPressureValue(double airPressureValue) {
        airPressureValueOutput.setText(String.valueOf(airPressureValue));
    }

    @Override
    public void getRainCondition(String condition) {
        rainConditionOutput.setText(condition);
    }

    @Override
    public void getRainValue(double rainValue) {
        rainValueOutput.setText(String.valueOf(rainValue));
    }

    @Override
    public void getSnowCondition(String condition) {
        snowConditionOutput.setText(condition);
    }

    @Override
    public void getSnowValue(double snowValue) {
        snowValueOutput.setText(String.valueOf(snowValue));
    }
    // endregion
}