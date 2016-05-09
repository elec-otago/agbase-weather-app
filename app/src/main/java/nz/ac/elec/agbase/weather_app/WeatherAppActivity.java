package nz.ac.elec.agbase.weather_app;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

/**
 * Created by tm on 3/05/16.
 */
public class WeatherAppActivity extends AppCompatActivity {

    private final String TAG = "Activity";

    private void setDeviceOrientation() {
        DisplayMetrics metrics = new DisplayMetrics();
        float width = 0, density = 0;

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = getResources().getDisplayMetrics().density;
        width = metrics.widthPixels / density;

        if(width < 600) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeatherApp)getApplicationContext()).onActivityStart();
        setDeviceOrientation();
    }


    @Override
    protected void onDestroy() {
        super.onStop();
        super.onDestroy();
        ((WeatherApp)getApplicationContext()).onActivityEnd();
    }
}
