package nz.ac.elec.agbase.weather_app.models;

/**
 * Created by tm on 21/04/16.
 */
public class ActiveAlert {

    private int id, weatherAlertId, alertEnd;
    private String alertStart, alertLast;

    public ActiveAlert() {
        alertEnd = 3600;
    }

    // region getters/setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeatherAlertId() {
        return weatherAlertId;
    }

    public void setWeatherAlertId(int weatherAlertId) {
        this.weatherAlertId = weatherAlertId;
    }

    public int getAlertEnd() {
        return alertEnd;
    }

    public void setAlertEnd(int alertEnd) {
        this.alertEnd = alertEnd;
    }

    public String getAlertStart() {
        return alertStart;
    }

    public void setAlertStart(String alertStart) {
        this.alertStart = alertStart;
    }

    public String getAlertLast() {
        return alertLast;
    }

    public void setAlertLast(String alertLast) {
        this.alertLast = alertLast;
    }

    // endregion
}