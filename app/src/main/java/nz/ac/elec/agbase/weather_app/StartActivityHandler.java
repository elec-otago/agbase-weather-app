package nz.ac.elec.agbase.weather_app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import nz.ac.elec.agbase.android_agbase_api.agbase_models.Sensor;
import nz.ac.elec.agbase.android_agbase_login.LoginActivity;
import nz.ac.elec.agbase.weather_app.activities.CreateWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.activities.EditWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.activities.ViewWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.activities.ViewWeatherAlertsActivity;
import nz.ac.elec.agbase.weather_app.activities.WeatherReportActivity;

/**
 * Created by tm on 18/04/16.
 */
public class StartActivityHandler {

    public static void startLoginActivity(Context context) {
        Bundle b = new Bundle();
        b.putBoolean(context.getString(R.string.ARGS_FROM_APP), true);
        //b.putString(LoginActivity.ARGS_PACKAGE, WeatherReportActivity.class.getName());
        b.putString(LoginActivity.ARGS_PACKAGE, context.getPackageName());
        b.putString(LoginActivity.ARGS_ACCOUNT_WORKER, AgBaseAccountWorker.class.getName());

        AccountManager manager = AccountManager.get(context);
        manager.addAccount(context.getString(R.string.AGBASE_ACCOUNT),
                null, null, b, (Activity) context, null, null);
    }

    public static void startCreateWeatherAlertActivity(Context context, Account account, Sensor weatherStation) {
        Intent intent = new Intent(context, CreateWeatherAlertActivity.class);
        intent.putExtra(context.getString(R.string.ARGS_ACCOUNT), account);
        if(weatherStation != null) {
            intent.putExtra(context.getString(R.string.ARGS_SENSOR), weatherStation.id);
        }
        context.startActivity(intent);
    }

    public static void startWeatherActivity(Context context, Account account) {
        Intent intent = new Intent(context, WeatherReportActivity.class);
        intent.putExtra(context.getString(R.string.ARGS_ACCOUNT), account);

        context.startActivity(intent);
    }

    public static void startViewWeatherAlertsActivity(Context context, Account account) {
        Intent intent = new Intent(context, ViewWeatherAlertsActivity.class);
        intent.putExtra(context.getString(R.string.ARGS_ACCOUNT), account);

        context.startActivity(intent);
    }

    public static void startViewWeatherAlertActivity(Context context, int weatherAlertId) {
        Intent intent = new Intent(context, ViewWeatherAlertActivity.class);
        intent.putExtra(ViewWeatherAlertActivity.ARGS_WEATHER_ALERT, weatherAlertId);

        context.startActivity(intent);
    }

    public static void startEditWeatherAlertActivity(Context context, int weatherAlertId) {
        Log.d("foo", "starting edit weather activity");
        Intent intent = new Intent(context, EditWeatherAlertActivity.class);
        intent.putExtra(EditWeatherAlertActivity.ARGS_WEATHER_ALERT, weatherAlertId);

        context.startActivity(intent);
    }
}
