package nz.ac.elec.agbase.weather_app;

import android.app.Application;

import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;

/**
 * Created by tm on 18/04/16.
 */
public class WeatherApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceHandler.initialize(this);
        AgBaseDatabaseManager.initialize(this);
        AlertDatabaseManager.initialize(this);
    }
}
