package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiWeather;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by tm on 28/04/16.
 */
public class WeatherRequest extends SyncAdapterRequest<Weather[]> {

    private final String TAG = "WeatherRequest";

    public static String ARGS_DEVICE            = "WeatherRequest.ARGS_DEVICE";
    public static String ARGS_START_DATE        = "WeatherRequest.ARGS_START_DATE";
    public static String ARGS_END_DATE          = "WeatherRequest.ARGS_END_DATE";
    public static String ARGS_LOW_WINDSPEED     = "WeatherRequest.ARGS_LOW_WINDSPEED";
    public static String ARGS_HIGH_WINDSPEED    = "WeatherRequest.ARGS_HIGH_WINDSPEED";
    public static String ARGS_LOW_WINDGUST      = "WeatherRequest.ARGS_LOW_WINDGUST";
    public static String ARGS_HIGH_WINDGUST     = "WeatherRequest.ARGS_HIGH_WINDGUST";
    public static String ARGS_WIND_DIRECTION    = "WeatherRequest.ARGS_WIND_DIRECTION";
    public static String ARGS_LOW_TEMP          = "WeatherRequest.ARGS_LOW_TEMP";
    public static String ARGS_HIGH_TEMP         = "WeatherRequest.ARGS_HIGH_TEMP";
    public static String ARGS_LOW_HUMIDITY      = "WeatherRequest.ARGS_LOW_HUMIDITY";
    public static String ARGS_HIGH_HUMIDITY     = "WeatherRequest.ARGS_HIGH_HUMIDITY";
    public static String ARGS_LOW_AIRPRESSURE   = "WeatherRequest.ARGS_LOW_AIRPRESSURE";
    public static String ARGS_HIGH_AIRPRESSURE  = "WeatherRequest.ARGS_HIGH_AIRPRESSURE";
    public static String ARGS_LOW_RAIN1MIN      = "WeatherRequest.ARGS_LOW_RAIN1MIN";
    public static String ARGS_HIGH_RAIN1MIN     = "WeatherRequest.ARGS_HIGH_RAIN1MIN";
    public static String ARGS_LOW_RAIN1HOUR     = "WeatherRequest.ARGS_LOW_RAIN1HOUR";
    public static String ARGS_HIGH_RAIN1HOUR    = "WeatherRequest.ARGS_HIGH_RAIN1HOUR";
    public static String ARGS_LOW_RAIN24HOUR    = "WeatherRequest.ARGS_LOW_RAIN24HOUR";
    public static String ARGS_HIGH_RAIN24HOUR   = "WeatherRequest.ARGS_HIGH_RAIN24HOUR";
    public static String ARGS_LIMIT             = "WeatherRequest.ARGS_LIMIT";
    public static String ARGS_OFFSET            = "WeatherRequest.ARGS_OFFSET";


    private Weather[] weathers;

    // todo: make this capable of doing alerts
    @Override
    public boolean performRequest(Bundle extras) {
        String device = extras.getString(ARGS_DEVICE);
        Integer limit = (Integer)extras.getSerializable(ARGS_LIMIT);
        Integer offset = (Integer)extras.getSerializable(ARGS_OFFSET);
        String start = extras.getString(ARGS_START_DATE);
        String end = extras.getString(ARGS_END_DATE);
        Double lowWindSpeed = (Double)extras.getSerializable(ARGS_LOW_WINDSPEED);
        Double highWindSpeed = (Double)extras.getSerializable(ARGS_HIGH_WINDSPEED);
        Double lowWindGust = (Double)extras.getSerializable(ARGS_LOW_WINDGUST);
        Double highWindGust = (Double)extras.getSerializable(ARGS_HIGH_WINDGUST);
        Double windDir = (Double)extras.getSerializable(ARGS_WIND_DIRECTION);
        Double lowTemp = (Double)extras.getSerializable(ARGS_LOW_TEMP);
        Double highTemp = (Double)extras.getSerializable(ARGS_HIGH_TEMP);
        Double lowHum = (Double)extras.getSerializable(ARGS_LOW_HUMIDITY);
        Double highHum = (Double)extras.getSerializable(ARGS_HIGH_HUMIDITY);
        Double lowAir = (Double)extras.getSerializable(ARGS_LOW_AIRPRESSURE);
        Double highAir = (Double)extras.getSerializable(ARGS_HIGH_AIRPRESSURE);
        Double lowRain1Min = (Double)extras.getSerializable(ARGS_LOW_RAIN1MIN);
        Double highRain1Min = (Double)extras.getSerializable(ARGS_HIGH_RAIN1MIN);
        Double lowRain1Hour = (Double)extras.getSerializable(ARGS_LOW_RAIN1HOUR);
        Double highRain1Hour = (Double)extras.getSerializable(ARGS_HIGH_RAIN1HOUR);
        Double lowRain24Hour = (Double)extras.getSerializable(ARGS_LOW_RAIN24HOUR);
        Double highRain24Hour = (Double)extras.getSerializable(ARGS_HIGH_RAIN24HOUR);

        if(device == null) {
            // return true if request cannot be completed
            return true;
        }
        boolean ret = false;

        try {
            Call req = AgBaseApi.getApi().getWeatherMeasurements(device, start, end, lowWindSpeed,
                    highWindSpeed, lowWindGust, highWindGust, windDir, lowTemp, highTemp, lowHum,
                    highHum, lowAir, highAir, lowRain1Min, highRain1Min, lowRain1Hour, highRain1Hour,
                    lowRain24Hour, highRain24Hour, limit, offset);
            Response<ApiWeather.GetManyResponse> res = req.execute();

            if(res.isSuccess()) {
                weathers = res.body().measurements;
                ret = true;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public Weather[] getRequestData() {
        return weathers;
    }
}
