package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import nz.ac.elec.agbase.android_agbase_api.AgBaseApi;
import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.android_agbase_api.api_models.ApiWeather;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import retrofit.Call;
import retrofit.Response;

/**
 * Created by tm on 28/04/16.
 */
public class WeatherRequest extends SyncAdapterRequest<Weather[]> {

    private final String TAG = "WeatherRequest";

    public static String ARGS_PRECIP_INTENSITY  = "WeatherRequest.ARGS_PRECIP_INTENSITY";
    public static String ARGS_GUID              = "WeatherRequest.ARGS_GUID";
    public static String ARGS_START             = "WeatherRequest.ARGS_START";
    public static String ARGS_END               = "WeatherRequest.ARGS_END";
    public static String ARGS_LOWWINDSPEED      = "WeatherRequest.ARGS_LOWWINDSPEED";
    public static String ARGS_HIGHWINDSPEED     = "WeatherRequest.ARGS_HIGHWINDSPEED";
    public static String ARGS_WINDDIR           = "WeatherRequest.ARGS_WINDDIR";
    public static String ARGS_LOWTEMP           = "WeatherRequest.ARGS_LOWTEMP";
    public static String ARGS_HIGHTEMP          = "WeatherRequest.ARGS_HIGHTEMP";
    public static String ARGS_LOWHUM            = "WeatherRequest.ARGS_LOWHUM";
    public static String ARGS_HIGHHUM           = "WeatherRequest.ARGS_HIGHHUM";
    public static String ARGS_LOWAIRP           = "WeatherRequest.ARGS_LOWAIRP";
    public static String ARGS_HIGHAIRP          = "WeatherRequest.ARGS_HIGHAIRP";
    public static String ARGS_PRECIPITATIONTYPE = "WeatherRequest.ARGS_PRECIPITATIONTYPE";
    public static String ARGS_LIMIT             = "WeatherRequest.ARGS_LIMIT";
    public static String ARGS_OFFSET            = "WeatherRequest.ARGS_OFFSET";


    private Weather[] weathers;

    // todo: make this capable of doing alerts
    @Override
    public boolean performRequest(Bundle extras) {
        String guid = extras.getString(ARGS_GUID);
        String precipitationType = extras.getString(ARGS_PRECIPITATIONTYPE);
        Integer limit = (Integer)extras.getSerializable(ARGS_LIMIT);
        Integer offset = (Integer)extras.getSerializable(ARGS_OFFSET);
        String start = extras.getString(ARGS_START);
        String end = extras.getString(ARGS_END);
        Double lowWindSpeed = (Double)extras.getSerializable(ARGS_LOWWINDSPEED);
        Double highWindSpeed = (Double)extras.getSerializable(ARGS_HIGHWINDSPEED);
        Double windDir = (Double)extras.getSerializable(ARGS_WINDDIR);
        Double lowTemp = (Double)extras.getSerializable(ARGS_LOWTEMP);
        Double highTemp = (Double)extras.getSerializable(ARGS_HIGHTEMP);
        Double lowHum = (Double)extras.getSerializable(ARGS_LOWHUM);
        Double highHum = (Double)extras.getSerializable(ARGS_HIGHHUM);
        Double lowAir = (Double)extras.getSerializable(ARGS_LOWAIRP);
        Double highAir = (Double)extras.getSerializable(ARGS_HIGHAIRP);
        Double precipIntens = (Double)extras.getSerializable(ARGS_PRECIP_INTENSITY);

        if(guid == null) {
            // return true if request cannot be completed
            return true;
        }
        boolean ret = false;
        try {
            Call req = AgBaseApi.getApi().getWeatherMeasurements(guid, start,
                    end, lowWindSpeed, highWindSpeed, windDir, lowTemp, highTemp, lowHum, highHum,
                    lowAir, highAir, precipitationType, precipIntens, limit, offset);
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
