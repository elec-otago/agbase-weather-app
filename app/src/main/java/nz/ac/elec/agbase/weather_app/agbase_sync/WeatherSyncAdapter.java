package nz.ac.elec.agbase.weather_app.agbase_sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorCategory;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.SensorType;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiAuth;
import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests.LoginRequest;
import nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests.SensorCategoryRequest;
import nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests.SensorRequest;
import nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests.WeatherRequest;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.models.ActiveAlert;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;

/**
 * Created by tm on 18/04/16.
 */
public class WeatherSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG                  = "SyncAdapter";
    public static final int TOKEN_LIFE              = 55;
    public static final String SYNC_FINISHED        = "nz.ac.elec.agbase.weather_app.intent.sync_finished";
    public static final String WEATHER_UPDATE       = "nz.ac.elec.agbase.weather_app.intent.weather_update";
    public static final String WEATHER_ALERT        = "nz.ac.elec.agbase.weather_app.intent.weather_alert";
    public static final String ARGS_WEATHER_ALERT   = "nz.ac.elec.agbase.weather_app.intent.ARGS_WEATHER_ALERT";
    public static final String ARGS_GET_WEATHER     = "nz.ac.elec.agbase.weather_app.intent.ARGS_GET_WEATHER";
    public static final String ARGS_CHECK_ALERT     = "nz.ac.elec.agbase.weather_app.intent.ARGS_CHECK_ALERT";

    public static final String ARGS_END_ALERT       = "nz.ac.elec.agbase.weather_app.intent.ARGS_END_ALERT";
    public static final String ARGS_END_ALERT_NAME  = "nz.ac.elec.agbase.weather_app.intent.ARGS_END_ALERT_NAME";

    ContentResolver mContentResolver;

    public WeatherSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(TAG, "performing sync...");

        boolean didComplete = false;
        Context context = getContext();
        AccountManager accountManager = AccountManager.get(context);

        setupApiToken(accountManager, account);

        // check if initial sync
        if(extras.getBoolean(context.getString(R.string.ARGS_DB_INIT), false)) {
            // confirm by checking preferences
            PreferenceHandler prefs = PreferenceHandler.getInstance();
            if(prefs.getInitCompletePreference(account)) {
                didComplete = true;
            }
            // check that there all other syncs have completed
            else if(getPendingSyncs(accountManager, account) == 0) {
                didComplete = initLocalDb(accountManager, account);
            }
        }
        // perform request
        else if(extras.getBoolean(ARGS_GET_WEATHER, false)) {

            WeatherRequest weatherRequest = new WeatherRequest();
            didComplete = weatherRequest.performRequest(extras);

            if(didComplete) {
                Weather[] weathers = weatherRequest.getRequestData();

                if(weathers.length > 0) {
                    sendWeather(weathers[0]);
                }
            }
            didComplete = true;
        }
        else if(extras.getBoolean(ARGS_CHECK_ALERT, false)) {
            int weatherAlertId = extras.getInt(ARGS_WEATHER_ALERT, -1);
            if(weatherAlertId != -1) {
                // do check

                if(checkWeatherAlert(weatherAlertId)) {
                    sendWeatherAlert(weatherAlertId);
                }
            }
            didComplete = true;
        }

        if(didComplete) {
            decrementPendingSyncs(accountManager, account);
        }
        else {
            syncResult.stats.numIoExceptions++;
        }
    }
    // region token setup
    /**
     * Retrieves the API auth token from the account.  If the token has expired,
     * retrieves a new token from the Agbase server
     */
    private void setupApiToken(AccountManager accountManager, Account account) {
        // Get the account's auth token.
        String accountToken = accountManager.peekAuthToken(
                account, AccountWorker.ARGS_AGBASE_TOKEN);

        boolean tokenExpired = true;

        try {
            // Get the time that that token was issued.
            String tokenIssueTimestamp = accountManager.getUserData
                    (account, AccountWorker.ARGS_TOKEN_RECEIVE_TIME);
            SimpleDateFormat format = new SimpleDateFormat
                    (getContext().getString(R.string.TIMESTAMP_FORMAT), Locale.getDefault());
            Date date = format.parse(tokenIssueTimestamp);
            long tokenIssueTime = date.getTime();
            tokenExpired = AgBaseApi.isTokenExpired(tokenIssueTime, TOKEN_LIFE);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Update the auth token if it has expired.
        if(tokenExpired) {
            getNewToken(accountManager, account);
            accountToken = accountManager.peekAuthToken(account, AccountWorker.ARGS_AGBASE_TOKEN);
        }

        // Set the auth token to the account token if they are different.
        String authToken = AgBaseApi.getAuthToken();
        if (TextUtils.isEmpty(authToken) || accountToken.compareTo(authToken) != 0) {
            AgBaseApi.clearAuthToken();
            AgBaseApi.setAuthToken(accountToken);
        }
    }

    /**
     * Gets and sets a new auth token for mAccount.
     */
    private void getNewToken(AccountManager accountManager, Account account) {
        LoginRequest loginRequest = new LoginRequest();

        Bundle extras = new Bundle();
        extras.putString(LoginRequest.ARGS_EMAIL, account.name);
        extras.putString(LoginRequest.ARGS_PASSWORD, accountManager.getPassword(account));

        if(loginRequest.performRequest(extras)) {

            ApiAuth.LoginResponse response = loginRequest.getRequestData();

            String accountType = getContext().getString(R.string.account_type);
            String tokenType = AccountWorker.ARGS_AGBASE_TOKEN;
            String oldToken = accountManager.peekAuthToken(account, tokenType);
            String newToken = response.token;

            SimpleDateFormat format = new SimpleDateFormat
                    (getContext().getResources().getString(R.string.TIMESTAMP_FORMAT), Locale.getDefault());
            Date date = new Date();
            String authTokenIssued = format.format(date);

            // Set auth token for account
            accountManager.setUserData(account, AccountWorker.ARGS_TOKEN_RECEIVE_TIME, authTokenIssued);
            accountManager.invalidateAuthToken(accountType, oldToken);
            accountManager.setAuthToken(account, tokenType, newToken);

            // Set auth token for agbase server.
            AgBaseApi.setAuthToken(newToken);
        }
    }
    // endregion

    // region pending sync requests
    /**
     * Finds and returns the number of sync requests that have been
     * made by the account given in the account parameter
     */
    private int getPendingSyncs(AccountManager accountManager, Account account) {
        int pendingSyncs = 0;
        String syncsArg =  getContext().getString(R.string.ARGS_PENDING_SYNCS);
        String pendingSyncsStr = accountManager.getUserData(account, syncsArg);
        try {
            pendingSyncs = Integer.parseInt(pendingSyncsStr);
        }
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        return pendingSyncs;
    }

    /**
     * Decrements the number of sync requests for the account given
     * in the account parameter.  This function should always be called
     * when a sync is successful.
     */
    private void decrementPendingSyncs(AccountManager accountManager, Account account) {
        int pendingSyncs = getPendingSyncs(accountManager, account);

        if(pendingSyncs > 0) {
            pendingSyncs--;
            String syncsArg =  getContext().getString(R.string.ARGS_PENDING_SYNCS);
            accountManager.setUserData(account, syncsArg, String.valueOf(pendingSyncs));
        }
    }
    // endregion

    // region finish init db
    private void finalizeInitDb(Account account) {
        PreferenceHandler.getInstance().setPerformInitPreference(account, false);
        PreferenceHandler.getInstance().setInitCompletePreference(account, true);

        sendSyncFinishedRequest();
    }

    private void sendSyncFinishedRequest() {
        Intent i = new Intent();
        i.setAction(SYNC_FINISHED);
        i.putExtra(getContext().getString(R.string.ARGS_DB_INIT), true);
        getContext().sendBroadcast(i);
    }
    // endregion

    /**
     * Sends a broadcast with the last weather measurement
     */
    private void sendWeather(Weather weather) {
        Intent i = new Intent();
        i.setAction(WEATHER_UPDATE);
        i.putExtra(ARGS_GET_WEATHER, weather);
        getContext().sendBroadcast(i);
    }

    /**
     * Sends a broadcast to indicate a weather alert with
     * an id equal to the id parameter was found
     */
    private void sendWeatherAlert(int weatherAlertId) {
        Intent i = new Intent();
        i.setAction(WEATHER_ALERT);
        i.putExtra(ARGS_WEATHER_ALERT, weatherAlertId);
        getContext().sendBroadcast(i);
    }

    /**
     * Sends a broadcast to indicate that an active alert has ended
     */
    private void sendWeatherAlertEnd(ActiveAlert activeAlert) {
        WeatherAlert alert = AlertDatabaseManager.getInstance().readWeatherAlert(activeAlert.getWeatherAlertId());
        Intent i = new Intent();
        i.setAction(ARGS_END_ALERT);
        i.putExtra(ARGS_END_ALERT_NAME, alert.getName());

        getContext().sendBroadcast(i);
    }


    private boolean initLocalDb(AccountManager accountManager, Account account) {
        // get Weather Station category and types
        boolean result = getWeatherStationTypes();
        // get all sensors that have a sensor type that belongs to the weather station category
        if(result) {
            result = getWeatherStationSensors();
        }
        if(result) {
            finalizeInitDb(account);
        }
        return result;
    }

    private boolean getWeatherStationTypes() {

        Bundle extras = new Bundle();
        extras.putString(SensorCategoryRequest.ARGS_INCLUDE, "sensorTypes");
        extras.putString(SensorCategoryRequest.ARGS_NAME, "Weather Station");
        SensorCategoryRequest sensorCategoryRequest = new SensorCategoryRequest();
        // perform api request
        if(sensorCategoryRequest.performRequest(extras)) {
            // check that only one sensor category was returned (weather station)
            SensorCategory[] sensorCategories = sensorCategoryRequest.getRequestData();
            if(sensorCategories.length == 1) {
                // save weather station sensor category to db
                AgBaseDatabaseManager db = AgBaseDatabaseManager.getInstance();
                int result = db.createSensorCategory(sensorCategories[0]);
                // save sensor types of category weather station if weather station category was saved
                if(result != -1) {

                    SensorType[] sensorTypes = sensorCategories[0].sensorTypes;
                    List<SensorType> sensorTypeList = Arrays.asList(sensorTypes);
                    db.createSensorTypes(sensorTypeList);
                }
            }
            return true;
        }
        return false;
    }

    private boolean getWeatherStationSensors() {

        AgBaseDatabaseManager db = AgBaseDatabaseManager.getInstance();

        // get weather station sensor category
        SensorCategory weatherStationCategory = db.readSensorCategoryWithName("Weather Station");
        if(weatherStationCategory == null) {
            return false;
        }

        // get sensor types that belong to weather station category
        List<SensorType> weatherStationTypes = db.readSensorTypesWithCategory(weatherStationCategory.id);
        if(weatherStationTypes == null) {
            return false;
        }

        // put ids of weather station category types into
        // a string to perform api request
        String sensorTypes = "";
        for(SensorType sensorType : weatherStationTypes) {
            if(sensorTypes.length() > 0) {
                sensorTypes = sensorTypes + ",";
            }
            sensorTypes = sensorTypes + String.valueOf(sensorType.id);
        }
        Bundle extras = new Bundle();
        extras.putString(SensorRequest.ARGS_TYPE, sensorTypes);
        SensorRequest request = new SensorRequest();

        if(request.performRequest(extras)) {
            List<Sensor> sensors = Arrays.asList(request.getRequestData());

            return db.createSensors(sensors);
        }
        return false;
    }

    /**
     * Checks for a weather measurement that meets the conditions defined by
     * the weather alert with an id equal to the id parameter is found.
     */
    private boolean checkWeatherAlert(int weatherAlertId) {
        // get weather alert from database.
        AlertDatabaseManager db = AlertDatabaseManager.getInstance();
        WeatherAlert alert = db.readWeatherAlert(weatherAlertId);
        // return false if weather alert is not found
        if(alert == null) {
            return false;
        }

        Double lowWindSpeed = null, highWindSpeed = null,
                lowTemperature = null, highTemperature = null, lowHumidity = null,
                highHumidity = null, lowAirPressure = null, highAirPressure = null;

        String guid = alert.getDeviceGuid();

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = new Date();
        startDate.setTime(startDate.getTime() - (5 * 60 * 1000)); // start is 5 minutes before now

        String start = timeStampFormat.format(startDate);
        String precipitationType = null;
        String end = null;
        int limit = 1;

        if(alert.getCheckTemp()) {
            double tempValue = alert.getTempValue();

            if(alert.getCheckTempCondition() == WeatherAlert.CheckCondition.ABOVE) {
                lowTemperature = tempValue;
            }
            else {
                highTemperature = tempValue;
            }
        }
        if(alert.getCheckWindSpeed()) {
            double windSpeedValue = alert.getWindSpeedValue();

            if (alert.getCheckWindSpeedCondition() == WeatherAlert.CheckCondition.ABOVE) {
                lowWindSpeed = windSpeedValue;
            }
            else {
                highWindSpeed = windSpeedValue;
            }
        }
        if(alert.getCheckHumidity()) {
            double humidityValue = alert.getHumidityValue();

            if(alert.getCheckHumidityCondition() == WeatherAlert.CheckCondition.ABOVE) {
                lowHumidity = humidityValue;
            }
            else {
                highHumidity = humidityValue;
            }
        }
        if(alert.getCheckAirPressure()) {
            double airPressureValue = alert.getAirPressureValue();

            if(alert.getCheckAirPressureCondition() == WeatherAlert.CheckCondition.ABOVE) {
                lowAirPressure = airPressureValue;
            }
            else {
                highAirPressure = airPressureValue;
            }
        }
        if(alert.getCheckRain()) {
            precipitationType = "liquid";
            if(alert.getCheckRainCondition() == WeatherAlert.CheckCondition.INTENSITY) {
                double rainIntensity = alert.getRainIntensityValue();
            }
        }
        if(alert.getCheckSnow()) {
            precipitationType = "solid";
            if(alert.getCheckSnowCondition() == WeatherAlert.CheckCondition.INTENSITY) {
                double snowIntensity = alert.getSnowIntensityValue();
            }
        }

        Bundle extras = new Bundle();
        extras.putString(WeatherRequest.ARGS_GUID, guid);
        extras.putString(WeatherRequest.ARGS_PRECIPITATIONTYPE, precipitationType);
        extras.putString(WeatherRequest.ARGS_START, start);
        extras.putString(WeatherRequest.ARGS_END, end);
        extras.putSerializable(WeatherRequest.ARGS_LOWWINDSPEED, lowWindSpeed);
        extras.putSerializable(WeatherRequest.ARGS_HIGHWINDSPEED, highWindSpeed);
        extras.putSerializable(WeatherRequest.ARGS_LOWTEMP, lowTemperature);
        extras.putSerializable(WeatherRequest.ARGS_HIGHTEMP, highTemperature);
        extras.putSerializable(WeatherRequest.ARGS_LOWHUM, lowHumidity);
        extras.putSerializable(WeatherRequest.ARGS_HIGHHUM, highHumidity);
        extras.putSerializable(WeatherRequest.ARGS_LOWAIRP, lowAirPressure);
        extras.putSerializable(WeatherRequest.ARGS_HIGHAIRP, highAirPressure);
        extras.putSerializable(WeatherRequest.ARGS_LIMIT, limit);

        WeatherRequest weatherRequest = new WeatherRequest();
        // perform request
        if(weatherRequest.performRequest(extras)) {

            // return true if any measurements were returned.
            if(weatherRequest.getRequestData().length > 0) {

                return true;
            }

            // if weather alert is active, check if it enough time has passed to
            // allow it to be deleted.
            ActiveAlert activeAlert = db.readActiveAlertForWeatherAlert(weatherAlertId);
            if(activeAlert != null) {

                try {
                    Date lastAlertDate = timeStampFormat.parse(activeAlert.getAlertLast());
                    long timeDiff = (startDate.getTime() - lastAlertDate.getTime()) / 1000;

                    if(timeDiff >= activeAlert.getAlertEnd()) {
                        sendWeatherAlertEnd(activeAlert);
                        db.deleteActiveAlert(activeAlert.getId());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
