package nz.ac.elec.agbase.weather_app.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.AlertSummaryGenerator;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.dialogs.AirPressureConditionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AirPressureValueDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AlertDescriptionDialog;
import nz.ac.elec.agbase.weather_app.dialogs.AlertNameDialog;
import nz.ac.elec.agbase.weather_app.dialogs.ConfirmAlertDialog;
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
 * Created by tm on 10/05/16.
 */
public class WeatherAlertFormFragment extends Fragment implements AlertNameDialog.IAlertNameDialog,
        AlertDescriptionDialog.IAlertDescriptionDialog, TempValueDialog.ITempValueDialog,
        TempConditionDialog.ITempConditionDialog, HumidityValueDialog.IHumidityValueDialog,
        HumidityConditionDialog.IHumidityConditionDialog, WindSpeedConditionDialog.IWindSpeedConditionDialog,
        WindSpeedValueDialog.IWindSpeedValueDialog, AirPressureConditionDialog.IAirPressureConditionDialog,
        AirPressureValueDialog.IAirPressureValueDialog, RainConditionDialog.IRainConditionDialog,
        RainValueDialog.IRainValueDialog/*,SnowConditionDialog.ISnowConditionDialog, SnowValueDialog.ISnowValueDialog*/ {

    private final String TAG = "WeatherAlertFormFragment";

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



    public static String[] RB_ABOVE_BELOW                       = new String[] {"above", "below"};
    public static String[] RB_SNOW_CONDITION                    = new String[] {"intensity", "snowing"};
    public static String[] RB_RAIN_CONDITION                    = new String[] {"intensity", "raining"};

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
    /*private CheckBox snowCb;
    private RelativeLayout useSnowInput, snowConditionInput, snowValueInput;
    private TextView snowConditionOutput, snowValueOutput;*/
    // endregion

    // region air pressure
    private CheckBox airPressureCb;
    private RelativeLayout useAirPressureInput, airPressureConditionInput, airPressureValueInput;
    private TextView airPressureConditionOutput, airPressureValueOutput;
    // endregion

    private Button saveBtn, cancelBtn;

    private IWeatherAlertFormFragment mCallback;

    public interface IWeatherAlertFormFragment {

        void getWeatherAlert(WeatherAlert weatherAlert);
        void cancelBtnClicked();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_alert_form, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof IWeatherAlertFormFragment) {
            mCallback = (IWeatherAlertFormFragment)context;
        }
        else {
            throw new RuntimeException(context.toString() +
                    " must implement IWeatherAlertFormFragment");
        }
    }

    // region initialization
    private void init(View view) {
        initAlertNameUI(view);
        initTempUI(view);
        initWindSpeedUI(view);
        initRainUI(view);
        //initSnowUI(view);
        initHumidityUI(view);
        initAirPressureUI(view);
        initWeatherStationSpinner(view);
        initButtons(view);
        getWeatherStationList();
    }

    private void initAlertNameUI(View view) {
        // init name and description outputs
        nameOutput = (TextView)view.findViewById(R.id.create_alert_form_alert_name_desc);
        descriptionOutput = (TextView)view.findViewById(R.id.create_alert_form_alert_desc_desc);

        // init name input
        nameInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_temp_name_container);
        nameInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertNameDialog();
            }
        });

        // init description input
        descriptionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_temp_desc_container);
        descriptionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDescriptionDialog();
            }
        });
    }

    private void initTempUI(View view) {
        // init condition and value outputs
        tempConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_temp_condition_desc);
        tempValueOutput = (TextView)view.findViewById(R.id.create_alert_form_temp_value_desc);

        // init use temperature condition switch
        tempCb = (CheckBox)view.findViewById(R.id.create_alert_form_temp_select_checkbox);
        useTempInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_temp_select_container);
        useTempInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempCb.setChecked(!tempCb.isChecked());
            }
        });
        // init chose temperature condition
        tempConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_temp_condition_container);
        tempConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTempConditionDialog();
            }
        });
        // init temperature value
        tempValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_temp_value_container);
        tempValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayTempValueDialog();
            }
        });
    }

    private void initHumidityUI(View view) {
        // init condition and value outputs
        humidityConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_humidity_condition_desc);
        humidityValueOutput = (TextView)view.findViewById(R.id.create_alert_form_humidity_value_desc);
        // init use humidity switch
        humidityCb = (CheckBox)view.findViewById(R.id.create_alert_form_humidity_select_checkbox);
        useHumidityInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_humidity_select_container);
        useHumidityInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                humidityCb.setChecked(!humidityCb.isChecked());
            }
        });
        // init choose humidity condition
        humidityConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_humidity_condition_container);
        humidityConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHumidityConditionDialog();
            }
        });
        // init humidity value
        humidityValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_humidity_value_container);
        humidityValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHumidityValueDialog();
            }
        });
    }

    private void initWindSpeedUI(View view) {
        // init condition and value outputs
        windSpeedConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_wind_speed_condition_desc);
        windSpeedValueOutput = (TextView)view.findViewById(R.id.create_alert_form_wind_speed_value_desc);

        // init use wind speed condition switch
        windSpeedCb = (CheckBox)view.findViewById(R.id.create_alert_form_wind_speed_select_checkbox);
        useWindSpeedInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_wind_speed_select_container);
        useWindSpeedInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                windSpeedCb.setChecked(!windSpeedCb.isChecked());
            }
        });
        // init choose wind speed condition
        windSpeedConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_wind_speed_condition_container);
        windSpeedConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWindSpeedConditionDialog();
            }
        });
        // init wind speed value
        windSpeedValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_wind_speed_value_container);
        windSpeedValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayWindSpeedValueDialog();
            }
        });
    }

    private void initAirPressureUI(View view) {
        //init condition and value outputs
        airPressureConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_air_pressure_condition_desc);
        airPressureValueOutput = (TextView)view.findViewById(R.id.create_alert_form_air_pressure_value_desc);
        // init use air pressure switch
        airPressureCb = (CheckBox)view.findViewById(R.id.create_alert_form_air_pressure_select_checkbox);
        useAirPressureInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_air_pressure_select_container);
        useAirPressureInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                airPressureCb.setChecked(!airPressureCb.isChecked());
            }
        });
        // init choose air pressure condition
        airPressureConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_air_pressure_condition_container);
        airPressureConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAirPressureConditionDialog();
            }
        });
        // init choose air pressure value
        airPressureValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_air_pressure_value_container);
        airPressureValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAirPressureValueDialog();
            }
        });
    }

    private void initRainUI(View view) {
        // init condition and value outputs
        rainConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_rain_condition_desc);
        rainValueOutput = (TextView)view.findViewById(R.id.create_alert_form_rain_value_desc);

        // init use rain switch
        rainCb = (CheckBox)view.findViewById(R.id.create_alert_form_rain_select_checkbox);
        useRainInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_rain_select_container);
        useRainInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainCb.setChecked(!rainCb.isChecked());
            }
        });
        // init choose rain condition
        rainConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_rain_condition_container);
        rainConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRainConditionDialog();
            }
        });
        // init choose rain value
        rainValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_rain_value_container);
        rainValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRainValueDialog();
            }
        });
    }

    private void initSnowUI(View view) {
        // init condition and value outputs
        /*snowConditionOutput = (TextView)view.findViewById(R.id.create_alert_form_snow_condition_desc);
        snowValueOutput = (TextView)view.findViewById(R.id.create_alert_form_snow_value_desc);

        // init use snow switch
        snowCb = (CheckBox)view.findViewById(R.id.create_alert_form_snow_select_checkbox);
        useSnowInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_snow_select_container);
        useSnowInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                snowCb.setChecked(!snowCb.isChecked());
            }
        });
        // init choose snow condition
        snowConditionInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_snow_condition_container);
        snowConditionInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySnowConditionDialog();
            }
        });
        // init choose snow value
        snowValueInput = (RelativeLayout)view.findViewById(R.id.create_alert_form_snow_value_container);
        snowValueInput.setOnClickListener(new RelativeLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySnowValueDialog();
            }
        });*/
    }

    private void initButtons(View view) {
        saveBtn = (Button)view.findViewById(R.id.alert_form_submit_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeatherAlert alert = buildWeatherAlert();
                mCallback.getWeatherAlert(alert);
            }
        });

        cancelBtn = (Button)view.findViewById(R.id.alert_form_cancel_btn);
        cancelBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.cancelBtnClicked();

            }
        });
    }

    private void initWeatherStationSpinner(View view) {
        weatherStationSpinner = (Spinner)view.findViewById(R.id.create_alert_form_activity_weather_station_dropdown);
        weatherStationList = new ArrayList<>();
        weatherStationArrayAdapter = new ArrayAdapter(this.getContext(), R.layout.alert_spinner_item, R.id.alert_spinner_item_textview, weatherStationList);
        weatherStationSpinner.setAdapter(weatherStationArrayAdapter);
    }

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

    /*private void displaySnowConditionDialog() {
        String condition = snowConditionOutput.getText().toString();
        SnowConditionDialog dialog = new SnowConditionDialog(this,
                SNOW_CONDITION_DIALOG_TITLE, RB_SNOW_CONDITION, condition);
        dialog.getDialog().show();
    }

    private void displaySnowValueDialog() {
        String snowValue = snowValueOutput.getText().toString();
        SnowValueDialog dialog = new SnowValueDialog(this, SNOW_VALUE_DIALOG_TITLE, snowValue);
        dialog.getDialog().show();
    }*/

    // endregion

    // region public functions

    public void updateWeatherStations() {
        // get the selected weather station
        Sensor selectedItem = (Sensor)weatherStationSpinner.getSelectedItem();
        // update weather station list
        getWeatherStationList();
        // restore the selected item if possible
        int selectedItemIndex = weatherStationList.indexOf(selectedItem);
        if(selectedItemIndex != -1) {
            weatherStationSpinner.setSelection(selectedItemIndex);
        }
    }

    public WeatherAlert buildWeatherAlert() {
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

        /*if(snowCb.isChecked()) {
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
        }*/
        if(alertConditionCount == 0) { return null; }

        alert.setName(nameOutput.getText().toString());
        alert.setDescription(descriptionOutput.getText().toString());
        String deviceGuid = weatherStationList.get(weatherStationSpinner.getSelectedItemPosition()).guid;
        alert.setDeviceGuid(deviceGuid);

        return alert;
    }

    public void setWeatherStation(Sensor weatherStation) {
        int position = weatherStationList.indexOf(weatherStation);
        weatherStationSpinner.setSelection(position);
    }

    public void setName(String name) {
        if(name != null) {
            nameOutput.setText(name);
        }
    }

    public void setDescription(String description) {
        if(description != null) {
            descriptionOutput.setText(description);
        }
    }

    public void setTemperatureCheckbox(boolean checked) {
        tempCb.setChecked(checked);
    }

    public void setTemperatureCondition(String condition) {
        if(condition != null) {
            tempConditionOutput.setText(condition);
        }
    }

    public void setTemperatureValue(double value) {
        tempValueOutput.setText(String.valueOf(value));
    }

    public void setHumidityCheckbox(boolean checked) {
        humidityCb.setChecked(checked);
    }

    public void setHumidityCondition(String condition) {
        if(condition != null) {
            humidityConditionOutput.setText(condition);
        }
    }

    public void setHumidityValue(double value) {
        humidityValueOutput.setText(String.valueOf(value));
    }

    public void setWindSpeedCheckbox(boolean checked) {
        windSpeedCb.setChecked(checked);
    }

    public void setWindSpeedCondition(String condition) {
        if(condition != null) {
            windSpeedConditionOutput.setText(condition);
        }
    }

    public void setWindSpeedValue(double value) {
        windSpeedValueOutput.setText(String.valueOf(value));
    }

    public void setAirPressureCheckbox(boolean checked) {
        airPressureCb.setChecked(checked);
    }

    public void setAirPressureCondition(String condition){
        if(condition != null) {
            airPressureConditionOutput.setText(condition);
        }
    }

    public void setAirPressureValue(double value) {
        airPressureValueOutput.setText(String.valueOf(value));
    }

    public void setRainCheckbox(boolean checked) {
        rainCb.setChecked(checked);
    }

    public void setRainCondition(String condition) {
        if(condition != null) {
            rainConditionOutput.setText(condition);
        }
    }

    public void setRainValue(double value) {
        rainValueOutput.setText(String.valueOf(value));
    }

    /*public void setSnowCheckbox(boolean checked) {
        snowCb.setChecked(checked);
    }

    public void setSnowCondition(String condition) {
        if(condition != null) {
            snowConditionOutput.setText(condition);
        }
    }

    public void setSnowValue(double value) {
        snowValueOutput.setText(String.valueOf(value));
    }*/


    // region interface functions
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

    /*@Override
    public void getSnowCondition(String condition) {
        snowConditionOutput.setText(condition);
    }

    @Override
    public void getSnowValue(double snowValue) {
        snowValueOutput.setText(String.valueOf(snowValue));
    }*/

    // endregion
}
