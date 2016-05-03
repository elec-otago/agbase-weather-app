package nz.ac.elec.agbase.weather_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by tm on 3/05/16.
 */
public class WeatherAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((WeatherApp)getApplicationContext()).onActivityStart();
    }

    @Override
    protected void onDestroy() {
        super.onStop();
        super.onDestroy();
        ((WeatherApp)getApplicationContext()).onActivityEnd();
    }
}
