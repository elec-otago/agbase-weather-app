package nz.ac.elec.agbase.weather_app;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * AlertSummaryGenerator.java
 *
 * Writes a summary of a weather alert.
 *
 * Created by tm on 2/05/16.
 */
public class AlertSummaryGenerator {

    public String writeAlertSummary(WeatherAlert alert) {

        String summary = "";

        if(alert.getCheckTemp()) {
            double tempValue = alert.getTempValue();

            if(alert.getCheckTempCondition() == WeatherAlert.CheckCondition.ABOVE) {
                summary += "Temperature must be above ";
            }
            else {
                summary += "Temperature must be below ";
            }
            summary += String.valueOf(tempValue) + "\u00B0c";
        }

        if(alert.getCheckWindSpeed()) {
            if(summary.length() > 0) {
                summary += "\n";
            }
            double windSpeedValue = alert.getWindSpeedValue();

            if(alert.getCheckWindSpeedCondition() == WeatherAlert.CheckCondition.ABOVE) {
                summary += "Wind speed must be above ";
            }
            else {
                summary += "Wind speed must be below ";
            }
            summary += String.valueOf(windSpeedValue) + "m/s";
        }

        if(alert.getCheckRain()) {
            if(summary.length() > 0) {
                summary += "\n";
            }

            if(alert.getCheckRainCondition() == WeatherAlert.CheckCondition.IS_TRUE) {
                summary += "Must be raining";
            }
            else {
                double rainIntensity  = alert.getRainIntensityValue();
                summary += "Rain intensity must be at least " +
                        String.valueOf(rainIntensity) + "mm/h";
            }
        }

        if(alert.getCheckSnow()) {
            if(summary.length() > 0) {
                summary += "\n";
            }

            if(alert.getCheckSnowCondition() == WeatherAlert.CheckCondition.IS_TRUE) {
                summary += "Must be snowing";
            }
            else {
                double snowIntensity = alert.getSnowIntensityValue();
                summary += "Snow intensity must be at least " +
                        String.valueOf(snowIntensity) + "mm/h";
            }
        }

        if(alert.getCheckHumidity()) {
            if(summary.length() > 0) {
                summary += "\n";
            }
            double humidityValue = alert.getHumidityValue();

            if(alert.getCheckHumidityCondition() == WeatherAlert.CheckCondition.ABOVE) {
                summary += "Humidity must be above ";
            }
            else{
                summary += "Humidity must be below ";
            }
            summary += String.valueOf(humidityValue) + "%";
        }

        if(alert.getCheckAirPressure()) {
            if(summary.length() > 0) {
                summary += "\n";
            }
            double airPressureValue = alert.getAirPressureValue();

            if(alert.getCheckAirPressureCondition() == WeatherAlert.CheckCondition.ABOVE) {
                summary += "Air pressure must be above ";
            }
            else {
                summary += "Air pressure must be below ";
            }
            summary += String.valueOf(airPressureValue) + "hPa";
        }
        return summary;
    }
}