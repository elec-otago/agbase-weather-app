package nz.ac.elec.agbase.weather_app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;

import nz.ac.elec.agbase.android_agbase_db.AgBaseDatabaseManager;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;
import nz.ac.elec.agbase.weather_app.services.UpdateService;
import nz.ac.elec.agbase.weather_app.services.UpdateService.UpdateServiceBinder;
import nz.ac.elec.agbase.weather_app.services.WeatherAlertService;

/**
 * Created by tm on 18/04/16.
 */
public class WeatherApp extends Application {

    private final String TAG = "WeatherApp";
    private int activitiesOpen = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceHandler.initialize(this);
        AgBaseDatabaseManager.initialize(this);
        AlertDatabaseManager.initialize(this);
    }

    // this is called when an activity that inherits from WeatherAppActivity is created
    public void onActivityStart() {
        activitiesOpen++;
        if(activitiesOpen == 1) {
            Log.d(TAG, "app 'onStart'");
            // start weather alert service
            if(AlertDatabaseManager.getInstance().getAlertCount() > 0) {
                Intent weatherServiceIntent = new Intent(this, WeatherAlertService.class);
                startService(weatherServiceIntent);
            }
            // start update service
            Intent updateServiceIntent = new Intent(this, UpdateService.class);
            startService(updateServiceIntent);
        }
    }

    // this is called when an activity that inherits from WeatherAppActivity is destroyed
    public void onActivityEnd() {
        activitiesOpen--;
        if(activitiesOpen < 1) {
            Log.d(TAG, "app 'onEnd'");
            Intent intent = new Intent(this, UpdateService.class);
            stopService(intent);
        }
    }


}