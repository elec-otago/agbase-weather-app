package nz.ac.elec.agbase.weather_app.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
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

    private final String TIMEZONE = "UTC";
    private final String DATE_PARSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private final String DATE_DISPLAY_FORMAT = "MMMM d";
    private final String TIME_DISPLAY_FORMAT = "h:mma";

    // constants used to map wind direction to a string value
    private final Double N_UPPER_BOUND = 11.25;
    private final Double N_LOWER_BOUND = 348.75;
    private final Double NNE_UPPER_BOUND = 33.75;
    private final Double NE_UPPER_BOUND = 56.25;
    private final Double ENE_UPPER_BOUND = 78.75;
    private final Double E_UPPER_BOUND = 101.25;
    private final Double ESE_UPPER_BOUND = 123.75;
    private final Double SE_UPPER_BOUND = 146.25;
    private final Double SSE_UPPER_BOUND = 168.75;
    private final Double S_UPPER_BOUND = 191.25;
    private final Double SSW_UPPER_BOUND = 213.75;
    private final Double SW_UPPER_BOUND = 236.25;
    private final Double WSW_UPPER_BOUND = 258.75;
    private final Double W_UPPER_BOUND = 281.25;
    private final Double WNW_UPPER_BOUND = 303.75;
    private final Double NW_UPPER_BOUND = 326.25;
    private final Double NNW_UPPER_BOUND = 348.75;

    private TextView tempOutput, dateOutput, timeOutput, windSpeedOutput, windDirOutput, humidityOutput,
        airPressureOutput, precipitationOutput, windGustOutput;

    private String windSpeedSuffix, humiditySuffix, airPressureSuffix, rainingText, temperatureSuffix;

    private String nValue, nneValue, neValue, eneValue, eValue, eseValue, seValue, sseValue, sValue,
            sswValue, swValue, wswValue, wValue, wnwValue, nwValue, nnwValue;

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
            tempOutput.setText(tempStr + temperatureSuffix);
        }

        // display time
        String dateStr = "";
        String timeStr = "";
        try {
            // use this if dateParseFormat doesn't work
            SimpleDateFormat parseFormat = new SimpleDateFormat(DATE_PARSE_FORMAT);
            parseFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
            Date date = parseFormat.parse(weather.time);
            SimpleDateFormat dateDisplayFormat = new SimpleDateFormat(DATE_DISPLAY_FORMAT);
            dateStr = dateDisplayFormat.format(date);
            SimpleDateFormat timeDisplayFormat = new SimpleDateFormat(TIME_DISPLAY_FORMAT);
            timeStr = timeDisplayFormat.format(date);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        dateOutput.setText(dateStr);
        timeOutput.setText(timeStr);

        // display wind speed
        if(weather.windSpeed != null) {
            windSpeedOutput.setText(String.valueOf(weather.windSpeed) + windSpeedSuffix);
        }
        else {
            windSpeedOutput.setText("0" + windSpeedSuffix);
        }

        // display wind gust
        if(weather.windGust != null) {
            windGustOutput.setText(String.valueOf(weather.windGust) + windSpeedSuffix);
        } else {
            windGustOutput.setText("0" + windSpeedSuffix);
        }

        // display wind direction
        if(weather.windDir != null) {
            windDirOutput.setText(convertWindDirection(weather.windDir));
        } else {
            windDirOutput.setText("");
        }

        // display humidity
        if(weather.humidity != null) {
            humidityOutput.setText(String.valueOf(weather.humidity) + humiditySuffix);
        } else {
            humidityOutput.setText("");
        }

        // display air pressure
        if(weather.barometricPressure != null) {
            airPressureOutput.setText(String.valueOf(weather.barometricPressure) + airPressureSuffix);
        } else {
            airPressureOutput.setText("");
        }

        // display precipitation
        if(weather.rain1Minute != null && weather.rain1Minute > 0) {
            precipitationOutput.setText(rainingText);
        } else {
            precipitationOutput.setText("");
        }
    }

    private void init(View view) {
        initTextviews(view);
        initStrings();
    }

    private void initStrings() {
        windSpeedSuffix = getResources().getString(R.string.wind_speed_suffix);
        humiditySuffix = getResources().getString(R.string.humidity_suffix);
        airPressureSuffix = getResources().getString(R.string.air_pressure_suffix);
        rainingText = getResources().getString(R.string.rain_title);
        temperatureSuffix = getResources().getString(R.string.temperature_suffix);

        nValue = getResources().getString(R.string.north_value_label);
        nneValue = getResources().getString(R.string.north_north_east_value_label);
        neValue = getResources().getString(R.string.north_east_value_label);
        eneValue = getResources().getString(R.string.east_north_east_value_label);
        eValue = getResources().getString(R.string.east_value_label);
        eseValue = getResources().getString(R.string.east_south_east_value_label);
        seValue = getResources().getString(R.string.south_east_value_label);
        sseValue = getResources().getString(R.string.south_south_east_value_label);
        sValue = getResources().getString(R.string.south_value_label);
        sswValue = getResources().getString(R.string.south_south_west_value_label);
        swValue = getResources().getString(R.string.south_west_value_label);
        wswValue = getResources().getString(R.string.west_south_west_value_label);
        wValue = getResources().getString(R.string.west_value_label);
        wnwValue = getResources().getString(R.string.west_north_west_value_label);
        nwValue = getResources().getString(R.string.north_west_value_label);
        nnwValue = getResources().getString(R.string.north_north_west_value_label);
    }

    private void initTextviews(View view) {
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

        if (windDir < N_UPPER_BOUND || windDir >= N_LOWER_BOUND) return nValue;
        if (windDir < NNE_UPPER_BOUND) return nneValue;
        if (windDir < NE_UPPER_BOUND) return neValue;
        if (windDir < ENE_UPPER_BOUND) return eneValue;
        if (windDir < E_UPPER_BOUND) return eValue;
        if (windDir < ESE_UPPER_BOUND) return eseValue;
        if (windDir < SE_UPPER_BOUND) return seValue;
        if (windDir < SSE_UPPER_BOUND) return sseValue;
        if (windDir < S_UPPER_BOUND) return sValue;
        if (windDir < SSW_UPPER_BOUND) return sswValue;
        if (windDir < SW_UPPER_BOUND) return swValue;
        if (windDir < WSW_UPPER_BOUND) return wswValue;
        if (windDir < W_UPPER_BOUND) return wValue;
        if (windDir < WNW_UPPER_BOUND) return wnwValue;
        if (windDir < NW_UPPER_BOUND) return nwValue;
        if (windDir < NNW_UPPER_BOUND) return nnwValue;
        return null;
    }
}
