package nz.ac.elec.agbase.weather_app.alert_db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tm on 21/04/16.
 */
public class AlertDatabase extends SQLiteOpenHelper{

    private final String TAG = "AlertDatabase";

    private static final String DB_NAME = "AlertDatabase";
    private static final int DB_VERSION = 1;

    private static AlertDatabase mAlertDatabase;

    public static synchronized AlertDatabase getDatabase(Context context) {
        if(mAlertDatabase == null) {
            mAlertDatabase = new AlertDatabase(context);
        }
        return mAlertDatabase;
    }

    private AlertDatabase(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);
        createTables(db);
    }

    private void dropTables(SQLiteDatabase db) {
        String dropSql = "DROP TABLE IF EXISTS " ;

        db.execSQL(dropSql + ActiveAlertsTable.TABLE_NAME);
        db.execSQL(dropSql + AlertsTable.TABLE_NAME);
    }

    private void createTables(SQLiteDatabase db) {
        AlertsTable alertsTable = new AlertsTable();
        ActiveAlertsTable activeAlertsTable = new ActiveAlertsTable();

        db.execSQL(alertsTable.createTable());
        db.execSQL(activeAlertsTable.createTable());
    }

    public class AlertsTable{

        public static final String TABLE_NAME                  = "Alerts";
        public static final String COL_ID                      = "id";
        public static final String COL_WEATHER_STATION         = "weather_station_id"; // deviceGuid
        public static final String COL_NAME                    = "name";
        public static final String COL_DESC                    = "description";
        public static final String COL_CHECK_TEMP              = "check_temperature";
        public static final String COL_CHECK_WIND_SPEED        = "check_wind_speed";
        public static final String COL_CHECK_RAIN              = "check_rain";
        public static final String COL_CHECK_HUMIDITY          = "check_humidity";
        public static final String COL_CHECK_AIR_PRESSURE      = "check_air_pressure";
        public static final String COL_TEMP_CONDITION          = "temperature_condition";
        public static final String COL_RAIN_CONDITION          = "rain_condition";
        public static final String COL_WIND_SPEED_CONDITION    = "wind_speed_condition";
        public static final String COL_HUMIDITY_CONDITION      = "humidity_condition";
        public static final String COL_AIR_PRESSURE_CONDITION  = "air_pressure_condition";
        public static final String COL_TEMP_VALUE              = "temperature_value";
        public static final String COL_WIND_SPEED_VALUE        = "wind_speed_value";
        public static final String COL_RAIN_VALUE              = "rain_value";
        public static final String COL_HUMIDITY_VALUE          = "humidity_value";
        public static final String COL_AIR_PRESSURE_VALUE      = "air_pressure_value";

        public String createTable() {
            return "CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_WEATHER_STATION + " TEXT NOT NULL, "
                    + COL_NAME + " TEXT, "
                    + COL_DESC + " TEXT, "
                    + COL_CHECK_TEMP + " INTEGER NOT NULL DEFAULT 0, "
                    + COL_CHECK_WIND_SPEED + " INTEGER NOT NULL DEFAULT 0, "
                    + COL_CHECK_RAIN + " INTEGER NOT NULL DEFAULT 0, "
                    + COL_CHECK_HUMIDITY + " INTEGER NOT NULL DEFAULT 0, "
                    + COL_CHECK_AIR_PRESSURE + " INTEGER NOT NULL DEFAULT 0, "
                    + COL_TEMP_CONDITION + " INTEGER, "
                    + COL_RAIN_CONDITION + " INTEGER, "
                    + COL_HUMIDITY_CONDITION + " INTEGER, "
                    + COL_WIND_SPEED_CONDITION + " INTEGER, "
                    + COL_AIR_PRESSURE_CONDITION + " INTEGER, "
                    + COL_TEMP_VALUE + " REAL, "
                    + COL_WIND_SPEED_VALUE + " REAL, "
                    + COL_RAIN_VALUE + " REAL, "
                    + COL_HUMIDITY_VALUE + " REAL, "
                    + COL_AIR_PRESSURE_VALUE + " REAL)";
        }
    }
    public class ActiveAlertsTable {

        public static final String TABLE_NAME = "ActiveAlerts";
        public static final String COL_ID = "id";
        public static final String COL_ALERT_ID = "alert_id";          // Weather alert id
        public static final String COL_ALERT_START = "alert_start";    // time the alert was triggered
        public static final String COL_ALERT_LAST  = "alert_last";     // time of last measurement that matched alert condition after alert start
        public static final String COL_ALERT_END   = "alert_end";      // time span that must elapse since alert last before ending alert
                                                                        // (epoch time)
        public String createTable() {
            return "CREATE TABLE " + TABLE_NAME + " ("
                    + COL_ID + " INTEGER PRIMARY KEY, "
                    + COL_ALERT_ID + " INTEGER NOT NULL, "
                    + COL_ALERT_START + " TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + COL_ALERT_LAST + " TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + COL_ALERT_END + " INTEGER NOT NULL DEFAULT 3600)"; // default is 1 hour in seconds
        }
    }
}
