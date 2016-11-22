package nz.ac.elec.agbase.weather_app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Weather;
import nz.ac.elec.agbase.weather_app.R;

/**
 * Created by tm on 18/04/16.
 */
public class WeatherDisplayFragment extends Fragment {

    private final String TAG = "WeatherDisplayFragment";

    private TextView tempOutput, dateOutput, timeOutput, windSpeedOutput, windDirOutput, humidityOutput,
        airPressureOutput, precipitationOutput, windGustOutput;

    private OnFragmentInteractionListener mListener;
    public interface OnFragmentInteractionListener {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.display_weather_fragment, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener)context;
        }
        else {
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Clears the weather measurement display
     */
    public void clear() {

        tempOutput.setText("");
        windDirOutput.setText("");
        windGustOutput.setText("");
        windSpeedOutput.setText("");
        humidityOutput.setText("");
        airPressureOutput.setText("");
        precipitationOutput.setText("");
        dateOutput.setText("");
        timeOutput.setText("");
    }

    public void displayWeatherMeasurement(Weather weather) {
        // display temperature
        if(weather.temperature != null) {
            String tempStr = String.valueOf((int) Math.round(weather.temperature));
            tempOutput.setText(tempStr + "\u00B0c");
        }

        // display time
        String dateStr = "";
        String timeStr = "";
        try {
            SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            parseFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = parseFormat.parse(weather.time);
            SimpleDateFormat dateDisplayFormat = new SimpleDateFormat("MMMM d");
            dateStr = dateDisplayFormat.format(date);
            SimpleDateFormat timeDisplayFormat = new SimpleDateFormat("h:mma");
            timeStr = timeDisplayFormat.format(date);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        dateOutput.setText(dateStr);
        timeOutput.setText(timeStr);

        // display wind speed
        if(weather.windSpeed != null) {
            windSpeedOutput.setText(String.valueOf(weather.windSpeed) + "m/s");
        }
        else {
            windSpeedOutput.setText("0m/s");
        }

        // display wind gust
        if(weather.windGust != null) {
            windGustOutput.setText(String.valueOf(weather.windGust) + "m/s");
        } else {
            windGustOutput.setText("0m/s");
        }

        // display wind direction
        if(weather.windDir != null) {
            windDirOutput.setText(convertWindDirection(weather.windDir));
        } else {
            windDirOutput.setText("");
        }

        // display humidity
        if(weather.humidity != null) {
            humidityOutput.setText(String.valueOf(weather.humidity) + "%");
        } else {
            humidityOutput.setText("");
        }

        // display air pressure
        if(weather.barometricPressure != null) {
            airPressureOutput.setText(String.valueOf(weather.barometricPressure) + "hPa");
        } else {
            airPressureOutput.setText("");
        }

        // display precipitation
        if(weather.rain1Minute != null && weather.rain1Minute > 0) {
            precipitationOutput.setText("Rain");
        } else {
            precipitationOutput.setText("");
        }
    }

    private void init(View view) {

        //tempOutput,
        tempOutput = (TextView)view.findViewById(R.id.display_weather_fragment_temp_output);
        // dateOutput,
        dateOutput = (TextView)view.findViewById(R.id.display_weather_fragment_date_output);
        timeOutput = (TextView)view.findViewById(R.id.display_weather_fragment_time_output);

        // windSpeedOutput,
        windSpeedOutput = (TextView)view.findViewById(R.id.display_weather_fragment_wind_speed_output);
        // windDirOutput,
        windDirOutput = (TextView)view.findViewById(R.id.display_weather_fragment_wind_direction_output);
        // windGustOutput,
        windGustOutput = (TextView)view.findViewById(R.id.display_weather_fragment_wind_gust_output);
        // humidityOutput,
        humidityOutput = (TextView)view.findViewById(R.id.display_weather_fragment_humidity_output);
        //airPressureOutput,
        airPressureOutput = (TextView)view.findViewById(R.id.display_weather_fragment_air_pressure_output);
        // precipitationOutput;
        precipitationOutput = (TextView)view.findViewById(R.id.display_weather_fragment_precipitation_output);
    }

    private String convertWindDirection(double windDir) {

        if(windDir < 11.25 || windDir >= 348.75) {
            return "N";
        }
        if(windDir < 33.75) {
            return "NNE";
        }
        if(windDir < 56.25) {
            return "NE";
        }
        if(windDir < 78.75) {
            return "ENE";
        }
        if(windDir < 101.25) {
            return "E";
        }
        if(windDir < 123.75) {
            return "ESE";
        }
        if(windDir < 146.25) {
            return "SE";
        }
        if(windDir < 168.75) {
            return "SSE";
        }
        if(windDir < 191.25) {
            return "S";
        }
        if(windDir < 213.75) {
            return "SSW";
        }
        if(windDir < 236.25) {
            return "SW";
        }
        if(windDir < 258.75) {
            return "WSW";
        }
        if(windDir < 281.25) {
            return "W";
        }
        if(windDir < 303.75) {
            return "WNW";
        }
        if(windDir < 326.25) {
            return "NW";
        }
        if(windDir < 348.75) {
            return "NNW";
        }
        return null;
    }
}
