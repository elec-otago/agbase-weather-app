package nz.ac.elec.agbase.weather_app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * WeatherAlert.java
 *
 * Stores the details of weather conditions that will
 * trigger a weather alert.
 *
 * Created by tm on 23/03/16.
 */
public class WeatherAlert implements Parcelable {

    // defines the condition to check for when necessary
    public enum CheckCondition {
        ABOVE,
        BELOW,
        IS_TRUE,
        INTENSITY
    }
    public WeatherAlert() {}

    private int id;

    private String name, description, deviceGuid;

    public boolean checkTemp, checkWindSpeed, checkRain, checkSnow,
                    checkHumidity, checkAirPressure;

    private CheckCondition tempCondition, rainCondition, snowCondition,
                    humidityCondition, airPressureCondition, windSpeedCondition;

    private double tempValue, windSpeedValue, rainValue, snowValue, humidityValue,
                    airPressureValue;

    // region getters/setters

    // id
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // weather station id
    public String getDeviceGuid() { return deviceGuid; }
    public void setDeviceGuid(String deviceGuid) { this.deviceGuid = deviceGuid; }
    // name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    // description
    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return this.description; }
    // check temperature
    public void setCheckTemp(boolean checkTemp) { this.checkTemp = checkTemp; }
    public boolean getCheckTemp() { return checkTemp; }
    // check wind speed
    public void setCheckWindSpeed(boolean checkWindSpeed) { this.checkWindSpeed = checkWindSpeed; }
    public boolean getCheckWindSpeed() { return checkWindSpeed; }
    // check rain
    public void setCheckRain(boolean checkRain) { this.checkRain = checkRain; }
    public boolean getCheckRain() { return checkRain; }
    // check snow
    public void setCheckSnow(boolean checkSnow) { this.checkSnow = checkSnow; }
    public boolean getCheckSnow() { return checkSnow; }
    // check humidity
    public void setCheckHumidity(boolean checkHumidity) { this.checkHumidity = checkHumidity; }
    public boolean getCheckHumidity() { return checkHumidity; }
    // check air pressure
    public void setCheckAirPressure(boolean checkAirPressure) { this.checkAirPressure = checkAirPressure; }
    public boolean getCheckAirPressure() { return checkAirPressure;  }
    // temperature condition
    public void setCheckTempCondition(CheckCondition tempCondition) { this.tempCondition = tempCondition; }
    public CheckCondition getCheckTempCondition() { return tempCondition; }
    // rain condition
    public void setCheckRainCondition(CheckCondition rainCondition) { this.rainCondition = rainCondition; }
    public CheckCondition getCheckRainCondition() {  return rainCondition; }
    // snow condition
    public void setCheckSnowCondition(CheckCondition snowCondition) { this.snowCondition = snowCondition; }
    public CheckCondition getCheckSnowCondition() { return snowCondition; }
    // humidity condition
    public void setCheckHumidityCondition(CheckCondition humidityCondition) { this.humidityCondition = humidityCondition; }
    public CheckCondition getCheckHumidityCondition() { return humidityCondition; }
    // air pressure condition
    public void setCheckAirPressureCondition(CheckCondition airPressureCondition) { this.airPressureCondition = airPressureCondition; }
    public CheckCondition getCheckAirPressureCondition() { return airPressureCondition; }
    // wind speed condition
    public void setCheckWindSpeedCondition(CheckCondition windSpeedCondition) { this.windSpeedCondition = windSpeedCondition; }
    public CheckCondition getCheckWindSpeedCondition() { return windSpeedCondition; }
    // temperature value
    public void setTempValue(double tempValue) { this.tempValue = tempValue; }
    public double getTempValue() { return tempValue;}
    // wind speed value
    public void setWindSpeedValue(double windSpeedValue) { this.windSpeedValue = windSpeedValue; }
    public double getWindSpeedValue() { return windSpeedValue; }
    // rain value
    public void setRainIntensityValue(double rainValue) { this.rainValue = rainValue; }
    public double getRainIntensityValue() { return rainValue; }
    // snow value
    public void setSnowIntensityValue(double snowValue) { this.snowValue = snowValue; }
    public double getSnowIntensityValue() { return snowValue; }
    // humidity value
    public void setHumidityValue(double humidityValue) { this.humidityValue = humidityValue; }
    public double getHumidityValue() {return humidityValue;}
    // air pressure value
    public void setAirPressureValue(double airPressureValue) { this.airPressureValue = airPressureValue; }
    public double getAirPressureValue() { return airPressureValue; }

    // endregion

    // region create check functions
    public void createTempCheck(CheckCondition condition, double value) {
        checkTemp = true;
        tempCondition = condition;
        tempValue = value;
    }

    public void createHumidityCheck(CheckCondition condition, double value) {
        checkHumidity = true;
        humidityCondition = condition;
        humidityValue = value;
    }

    public void createAirPressureCheck(CheckCondition condition, double value) {
        checkAirPressure = true;
        airPressureCondition = condition;
        airPressureValue = value;
    }

    public void createRainCheck() {
        checkRain = true;
        rainCondition = CheckCondition.IS_TRUE;
    }

    public void createRainIntensityCheck(double value) {
        checkRain = true;
        rainCondition = CheckCondition.INTENSITY;
        rainValue = value;
    }

    public void createSnowCheck() {
        checkSnow = true;
        snowCondition = CheckCondition.IS_TRUE;
    }

    public void createSnowIntensityCheck(double value) {
        checkSnow = true;
        snowCondition = CheckCondition.INTENSITY;
        snowValue = value;
    }

    public void createWindSpeedCheck(CheckCondition condition, double value) {
        checkWindSpeed = true;
        windSpeedCondition = condition;
        windSpeedValue = value;
    }
    // endregion

    // region remove check functions
    public void removeTempCheck() {
        checkTemp = false;
        tempCondition = null;
        tempValue = 0;
    }

    public void removeHumidityCheck() {
        checkHumidity = false;
        humidityCondition = null;
        humidityValue = 0;
    }

    public void removeRainCheck() {
        checkRain = false;
        rainCondition = null;
        rainValue = 0;
    }

    public void removeSnowCheck() {
        checkSnow = false;
        snowCondition = null;
        snowValue = 0;
    }

    public void removeAirPressureCheck() {
        checkAirPressure = false;
        airPressureCondition = null;
        airPressureValue = 0;
    }

    public void removeWindSpeedCheck() {
        checkWindSpeed = false;
        windSpeedCondition = null;
        windSpeedValue = 0;
    }
    // endregion

    // region parcelable

    private WeatherAlert(Parcel source) {
        id = source.readInt();
        name = source.readString();
        description = source.readString();
        tempValue = source.readDouble();
        windSpeedValue = source.readDouble();
        rainValue = source.readDouble();
        snowValue = source.readDouble();
        humidityValue = source.readDouble();
        airPressureValue = source.readDouble();
        checkTemp = source.readByte() == 1;
        checkWindSpeed = source.readByte() == 1;
        checkRain = source.readByte() == 1;
        checkSnow = source.readByte() == 1;
        checkHumidity = source.readByte() == 1;
        checkAirPressure = source.readByte() == 1;
        if(checkTemp) {
            tempCondition = CheckCondition.values()[source.readInt()];
        }
        if(checkWindSpeed) {
            windSpeedCondition = CheckCondition.values()[source.readInt()];
        }
        if(checkRain) {
            rainCondition = CheckCondition.values()[source.readInt()];
        }
        if(checkSnow) {
            snowCondition = CheckCondition.values()[source.readInt()];
        }
        if(checkHumidity){
            humidityCondition = CheckCondition.values()[source.readInt()];
        }
        if (checkAirPressure) {
            airPressureCondition = CheckCondition.values()[source.readInt()];
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeDouble(tempValue);
        parcel.writeDouble(windSpeedValue);
        parcel.writeDouble(rainValue);
        parcel.writeDouble(snowValue);
        parcel.writeDouble(humidityValue);
        parcel.writeDouble(airPressureValue);
        parcel.writeByte((byte) (checkTemp ? 1 : 0));
        parcel.writeByte((byte) (checkWindSpeed ? 1 : 0));
        parcel.writeByte((byte) (checkRain ? 1 : 0));
        parcel.writeByte((byte) (checkSnow ? 1 : 0));
        parcel.writeByte((byte) (checkHumidity? 1 : 0));
        parcel.writeByte((byte) (checkAirPressure? 1 : 0));
        if(checkTemp) {
            parcel.writeInt(tempCondition.ordinal());
        }
        if(checkWindSpeed) {
            parcel.writeInt(windSpeedCondition.ordinal());
        }
        if(checkRain) {
            parcel.writeInt(rainCondition.ordinal());
        }
        if(checkSnow) {
            parcel.writeInt(snowCondition.ordinal());
        }
        if(checkHumidity){
            parcel.writeInt(humidityCondition.ordinal());
        }
        if(checkAirPressure) {
            parcel.writeInt(airPressureCondition.ordinal());
        }
    }

    public static final Creator<WeatherAlert> CREATOR
            = new Creator<WeatherAlert>() {
        @Override
        public WeatherAlert createFromParcel(Parcel source) {
            return new WeatherAlert(source);
        }

        @Override
        public WeatherAlert[] newArray(int size) {
            return new WeatherAlert[0];
        }
    };

    // endregion
}
