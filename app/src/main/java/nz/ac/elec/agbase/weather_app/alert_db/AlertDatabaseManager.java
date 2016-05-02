package nz.ac.elec.agbase.weather_app.alert_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nz.ac.elec.agbase.weather_app.models.ActiveAlert;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 21/04/16.
 */
public class AlertDatabaseManager {

    private final String TAG = "AlertDatabaseManager";
    private final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static AlertDatabaseManager instance;
    private static SQLiteOpenHelper mDatabaseHelper;

    public static synchronized void initialize(Context context) {
        if(instance == null) {
            instance = new AlertDatabaseManager();
            mDatabaseHelper = AlertDatabase.getDatabase(context);
        }
    }

    public static synchronized AlertDatabaseManager getInstance() {
        return instance;
    }

    private SQLiteDatabase open() throws SQLException {
        return mDatabaseHelper.getWritableDatabase();
    }

    private void close() {
        mDatabaseHelper.close();
    }

    /**
     * Parses and returns a utc timestamp from the date parameter.
     */
    public String convertDateToUtcTimestamp(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(TIMESTAMP_FORMAT);
        format.setTimeZone(TimeZone.getDefault());
        return format.format(date);
    }

    // region alerts
    public int createAlert(WeatherAlert weatherAlert) {
        int ret = -1;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ContentValues contentValues = createWeatherAlertContentValues(weatherAlert);
            contentValues.remove(AlertDatabase.AlertsTable.COL_ID);

            ret = (int)db.insert(AlertDatabase.AlertsTable.TABLE_NAME, null, contentValues);

            if(ret != -1) {
                weatherAlert.setId(ret);
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }

    public List<WeatherAlert> readWeatherAlerts() {
        List<WeatherAlert> weatherAlerts = new ArrayList<>();

        try {
            SQLiteDatabase db = open();

            Cursor cursor = db.query(AlertDatabase.AlertsTable.TABLE_NAME, getWeatherAlertFields(),
                    null, null, null, null, null);

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();

                while(!cursor.isAfterLast()) {
                    WeatherAlert weatherAlert = createWeatherAlertFromCursor(cursor);
                    weatherAlerts.add(weatherAlert);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return weatherAlerts;
    }

    public WeatherAlert readWeatherAlert(int weatherAlertId) {
        WeatherAlert weatherAlert = null;

        try {
            SQLiteDatabase db = open();

            Cursor cursor = db.query(AlertDatabase.AlertsTable.TABLE_NAME, getWeatherAlertFields(),
                    AlertDatabase.AlertsTable.COL_ID + "=?", new String[]{String.valueOf(weatherAlertId)},
                    null, null, null, "1");

            if(cursor.getCount() == 1) {
                cursor.moveToFirst();
                weatherAlert = createWeatherAlertFromCursor(cursor);
            }
            cursor.close();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return weatherAlert;
    }

    public boolean updateWeatherAlert(WeatherAlert weatherAlert) {
        boolean ret = false;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ContentValues values = createWeatherAlertContentValues(weatherAlert);
            ret = db.update(AlertDatabase.AlertsTable.TABLE_NAME, values, AlertDatabase.AlertsTable.COL_ID + "=?",
                    new String[]{String.valueOf(weatherAlert.getId())}) == 1;

            if(ret) {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }

    public boolean deleteWeatherAlert(int weatherAlertId) {
        boolean ret = false;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ret = db.delete(AlertDatabase.AlertsTable.TABLE_NAME, AlertDatabase.AlertsTable.COL_ID + "=?",
                    new String[]{String.valueOf(weatherAlertId)}) == 1;

            if(ret) {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }

    /**
     * Returns a count of all alerts in database
     */
    public int getAlertCount() {
        int ret = 0;

        try {
            SQLiteDatabase db = open();

            Cursor cursor = db.query(AlertDatabase.AlertsTable.TABLE_NAME, getWeatherAlertFields(),
                    null, null, null, null, null);

            ret = cursor.getCount();
            cursor.close();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }
    // endregion

    // region active alerts
    public int createActiveAlert(ActiveAlert activeAlert) {
        int ret = -1;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ContentValues contentValues = createActiveAlertContentValues(activeAlert);
            contentValues.remove(AlertDatabase.ActiveAlertsTable.COL_ID);

            ret = (int)db.insert(AlertDatabase.ActiveAlertsTable.TABLE_NAME, null, contentValues);

            if(ret != -1) {
                activeAlert.setId(ret);
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }

    /**
     * Returns an active alert that has a weather alert id equal to the
     * weatherAlertId parameter.
     */
    public ActiveAlert readActiveAlertForWeatherAlert(int weatherAlertId) {
        ActiveAlert activeAlert = null;

        try {
            SQLiteDatabase db = open();

            Cursor cursor = db.query(AlertDatabase.ActiveAlertsTable.TABLE_NAME, getActiveAlertFields(),
                    AlertDatabase.ActiveAlertsTable.COL_ALERT_ID + "=?", new String[]{String.valueOf(weatherAlertId)},
                    null, null, null, "1");

            if(cursor.getCount() == 1) {
                cursor.moveToFirst();
                activeAlert = createActiveAlertFromCursor(cursor);
            }
            cursor.close();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return activeAlert;
    }

    public boolean updateActiveAlert(ActiveAlert activeAlert) {
        boolean ret = false;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ContentValues values = createActiveAlertContentValues(activeAlert);
            ret = db.update(AlertDatabase.ActiveAlertsTable.TABLE_NAME, values, AlertDatabase.ActiveAlertsTable.COL_ID + "=?",
                    new String[]{String.valueOf(activeAlert.getId())}) == 1;

            if(ret) {
                db.setTransactionSuccessful();
            }

            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;
    }

    public boolean deleteActiveAlert(int activeAlertId) {
        boolean ret = false;

        try {
            SQLiteDatabase db = open();
            db.beginTransaction();

            ret = db.delete(AlertDatabase.ActiveAlertsTable.TABLE_NAME, AlertDatabase.ActiveAlertsTable.COL_ID + "=?",
                    new String[]{String.valueOf(activeAlertId)}) == 1;

            if(ret) {
                db.setTransactionSuccessful();
            }
            db.endTransaction();
        }
        catch(SQLException e) { e.printStackTrace(); }
        finally { close(); }
        return ret;

    }
    // endregion

    // region helper functions

    // region weather alert
    private ContentValues createWeatherAlertContentValues(WeatherAlert weatherAlert) {
        ContentValues values = new ContentValues();

        values.put(AlertDatabase.AlertsTable.COL_ID, weatherAlert.getId());
        values.put(AlertDatabase.AlertsTable.COL_WEATHER_STATION, weatherAlert.getDeviceGuid());
        values.put(AlertDatabase.AlertsTable.COL_NAME, weatherAlert.getName());
        values.put(AlertDatabase.AlertsTable.COL_DESC, weatherAlert.getDescription());

        values.put(AlertDatabase.AlertsTable.COL_CHECK_TEMP, weatherAlert.getCheckTemp());
        if(weatherAlert.getCheckTemp()) {
            values.put(AlertDatabase.AlertsTable.COL_TEMP_CONDITION, weatherAlert.getCheckTempCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_TEMP_VALUE, weatherAlert.getTempValue());
        }

        values.put(AlertDatabase.AlertsTable.COL_CHECK_WIND_SPEED, weatherAlert.getCheckWindSpeed());
        if(weatherAlert.getCheckWindSpeed()) {
            values.put(AlertDatabase.AlertsTable.COL_WIND_SPEED_CONDITION, weatherAlert.getCheckWindSpeedCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_WIND_SPEED_VALUE, weatherAlert.getWindSpeedValue());
        }

        values.put(AlertDatabase.AlertsTable.COL_CHECK_RAIN, weatherAlert.getCheckRain());
        if(weatherAlert.getCheckRain()) {
            values.put(AlertDatabase.AlertsTable.COL_RAIN_CONDITION, weatherAlert.getCheckRainCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_RAIN_VALUE, weatherAlert.getRainIntensityValue());
        }

        values.put(AlertDatabase.AlertsTable.COL_CHECK_SNOW, weatherAlert.getCheckSnow());
        if(weatherAlert.getCheckSnow()) {
            values.put(AlertDatabase.AlertsTable.COL_SNOW_CONDITION, weatherAlert.getCheckSnowCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_SNOW_VALUE, weatherAlert.getSnowIntensityValue());
        }

        values.put(AlertDatabase.AlertsTable.COL_CHECK_HUMIDITY, weatherAlert.getCheckHumidity());
        if(weatherAlert.getCheckHumidity()) {
            values.put(AlertDatabase.AlertsTable.COL_HUMIDITY_CONDITION, weatherAlert.getCheckHumidityCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_HUMIDITY_VALUE, weatherAlert.getHumidityValue());
        }

        values.put(AlertDatabase.AlertsTable.COL_CHECK_AIR_PRESSURE, weatherAlert.getCheckAirPressure());
        if(weatherAlert.getCheckAirPressure()) {
            values.put(AlertDatabase.AlertsTable.COL_AIR_PRESSURE_CONDITION, weatherAlert.getCheckAirPressureCondition().ordinal());
            values.put(AlertDatabase.AlertsTable.COL_AIR_PRESSURE_VALUE, weatherAlert.getAirPressureValue());
        }
        return values;
    }

    private String[] getWeatherAlertFields() {
        return new String[] {
                AlertDatabase.AlertsTable.COL_ID,
                AlertDatabase.AlertsTable.COL_WEATHER_STATION,
                AlertDatabase.AlertsTable.COL_NAME,
                AlertDatabase.AlertsTable.COL_DESC,
                AlertDatabase.AlertsTable.COL_CHECK_TEMP,
                AlertDatabase.AlertsTable.COL_CHECK_WIND_SPEED,
                AlertDatabase.AlertsTable.COL_CHECK_RAIN,
                AlertDatabase.AlertsTable.COL_CHECK_SNOW,
                AlertDatabase.AlertsTable.COL_CHECK_HUMIDITY,
                AlertDatabase.AlertsTable.COL_CHECK_AIR_PRESSURE,
                AlertDatabase.AlertsTable.COL_TEMP_CONDITION,
                AlertDatabase.AlertsTable.COL_RAIN_CONDITION,
                AlertDatabase.AlertsTable.COL_SNOW_CONDITION,
                AlertDatabase.AlertsTable.COL_HUMIDITY_CONDITION,
                AlertDatabase.AlertsTable.COL_WIND_SPEED_CONDITION,
                AlertDatabase.AlertsTable.COL_AIR_PRESSURE_CONDITION,
                AlertDatabase.AlertsTable.COL_TEMP_VALUE,
                AlertDatabase.AlertsTable.COL_WIND_SPEED_VALUE,
                AlertDatabase.AlertsTable.COL_RAIN_VALUE,
                AlertDatabase.AlertsTable.COL_SNOW_VALUE,
                AlertDatabase.AlertsTable.COL_HUMIDITY_VALUE,
                AlertDatabase.AlertsTable.COL_AIR_PRESSURE_VALUE
        };
    }

    private WeatherAlert createWeatherAlertFromCursor(Cursor cursor) {
        WeatherAlert weatherAlert = new WeatherAlert();

        String[] cursorFields = getWeatherAlertFields();

        for(int i = 0; i <cursorFields.length; i++) {
            String field = cursorFields[i];

            if(field == AlertDatabase.AlertsTable.COL_ID) {
                weatherAlert.setId(cursor.getInt(i));
            }
            if(field == AlertDatabase.AlertsTable.COL_WEATHER_STATION) {
                weatherAlert.setDeviceGuid(cursor.getString(i));
            }
            else if(field == AlertDatabase.AlertsTable.COL_NAME) {
                weatherAlert.setName(cursor.getString(i));
            }
            else if(field == AlertDatabase.AlertsTable.COL_DESC) {
                weatherAlert.setDescription(cursor.getString(i));
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_TEMP) {
                weatherAlert.setCheckTemp(cursor.getInt(i) == 1);
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_WIND_SPEED) {
                weatherAlert.setCheckWindSpeed(cursor.getInt(i) == 1);
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_RAIN) {
                weatherAlert.setCheckRain(cursor.getInt(i) == 1);
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_SNOW) {
                weatherAlert.setCheckSnow(cursor.getInt(i) == 1);
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_HUMIDITY) {
                weatherAlert.setCheckHumidity(cursor.getInt(i) == 1);
            }
            else if(field == AlertDatabase.AlertsTable.COL_CHECK_AIR_PRESSURE) {
                weatherAlert.setCheckAirPressure(cursor.getInt(i) == 1);
            }
            else if(!cursor.isNull(i)) {
                if (field == AlertDatabase.AlertsTable.COL_TEMP_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckTempCondition(condition);

                }
                else if (field == AlertDatabase.AlertsTable.COL_RAIN_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckRainCondition(condition);
                }
                else if (field == AlertDatabase.AlertsTable.COL_SNOW_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckSnowCondition(condition);
                }
                else if (field == AlertDatabase.AlertsTable.COL_WIND_SPEED_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckWindSpeedCondition(condition);
                }
                else if (field == AlertDatabase.AlertsTable.COL_HUMIDITY_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckHumidityCondition(condition);
                }
                else if (field == AlertDatabase.AlertsTable.COL_AIR_PRESSURE_CONDITION) {
                    WeatherAlert.CheckCondition condition = WeatherAlert.CheckCondition.values()[cursor.getInt(i)];
                    weatherAlert.setCheckAirPressureCondition(condition);
                }
                else if (field == AlertDatabase.AlertsTable.COL_TEMP_VALUE) {
                    weatherAlert.setTempValue(cursor.getDouble(i));
                }
                else if (field == AlertDatabase.AlertsTable.COL_WIND_SPEED_VALUE) {
                    weatherAlert.setWindSpeedValue(cursor.getDouble(i));
                }
                else if (field == AlertDatabase.AlertsTable.COL_RAIN_VALUE) {
                    weatherAlert.setRainIntensityValue(cursor.getDouble(i));
                }
                else if (field == AlertDatabase.AlertsTable.COL_SNOW_VALUE) {
                    weatherAlert.setSnowIntensityValue(cursor.getDouble(i));
                }
                else if (field == AlertDatabase.AlertsTable.COL_HUMIDITY_VALUE) {
                    weatherAlert.setHumidityValue(cursor.getDouble(i));
                }
                else if (field == AlertDatabase.AlertsTable.COL_AIR_PRESSURE_VALUE) {
                    weatherAlert.setAirPressureValue(cursor.getDouble(i));
                }
            }
        }
        return weatherAlert;
    }
    // endregion

    // region active alert
    private String[] getActiveAlertFields() {
        return new String [] {
                AlertDatabase.ActiveAlertsTable.COL_ID,
                AlertDatabase.ActiveAlertsTable.COL_ALERT_ID,
                AlertDatabase.ActiveAlertsTable.COL_ALERT_START,
                AlertDatabase.ActiveAlertsTable.COL_ALERT_LAST,
                AlertDatabase.ActiveAlertsTable.COL_ALERT_END};
    }

    private ContentValues createActiveAlertContentValues(ActiveAlert activeAlert) {
        ContentValues values = new ContentValues();

        values.put(AlertDatabase.ActiveAlertsTable.COL_ID, activeAlert.getId());
        values.put(AlertDatabase.ActiveAlertsTable.COL_ALERT_ID, activeAlert.getWeatherAlertId());
        values.put(AlertDatabase.ActiveAlertsTable.COL_ALERT_START, activeAlert.getAlertStart());
        values.put(AlertDatabase.ActiveAlertsTable.COL_ALERT_LAST, activeAlert.getAlertLast());
        values.put(AlertDatabase.ActiveAlertsTable.COL_ALERT_END, activeAlert.getAlertEnd());

        return values;
    }

    private ActiveAlert createActiveAlertFromCursor(Cursor cursor) {
        ActiveAlert activeAlert = new ActiveAlert();

        String[] cursorFields = getActiveAlertFields();

        for(int i = 0; i <cursorFields.length; i++) {
            String field = cursorFields[i];

            if(field == AlertDatabase.ActiveAlertsTable.COL_ID) {
                activeAlert.setId(cursor.getInt(i));
            }
            else if(field == AlertDatabase.ActiveAlertsTable.COL_ALERT_ID) {
                activeAlert.setWeatherAlertId(cursor.getInt(i));
            }
            else if(field == AlertDatabase.ActiveAlertsTable.COL_ALERT_START) {
                activeAlert.setAlertStart(cursor.getString(i));
            }
            else if(field == AlertDatabase.ActiveAlertsTable.COL_ALERT_LAST) {
                activeAlert.setAlertLast(cursor.getString(i));
            }
            else if(field == AlertDatabase.ActiveAlertsTable.COL_ALERT_END) {
                activeAlert.setAlertEnd(cursor.getInt(i));
            }
        }
        return activeAlert;
    }
    // endregion

    // endregion
}
