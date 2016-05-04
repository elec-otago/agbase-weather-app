package nz.ac.elec.agbase.weather_app.services;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import nz.ac.elec.agbase.weather_app.AgBaseAccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.activities.EditWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.activities.ViewWeatherAlertActivity;
import nz.ac.elec.agbase.weather_app.agbase_sync.SyncAdapterHandler;
import nz.ac.elec.agbase.weather_app.agbase_sync.WeatherSyncAdapter;
import nz.ac.elec.agbase.weather_app.alert_db.AlertDatabaseManager;
import nz.ac.elec.agbase.weather_app.models.ActiveAlert;
import nz.ac.elec.agbase.weather_app.models.WeatherAlert;

/**
 * Created by tm on 20/04/16.
 */
public class WeatherAlertService extends Service {

    private final String TAG = "WeatherAlertService";

    private Handler weatherAlertHandler;
    private IBinder binder;
    private Context mContext;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        init();
        mContext = getApplicationContext();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void init() {
        // init broadcast receiver
        intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherSyncAdapter.WEATHER_ALERT);
        registerReceiver(broadcastReceiver, intentFilter);
        // init timer
        weatherAlertHandler = new Handler();
        weatherAlertHandler.postDelayed(requestRunnable, (10 * 1000));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class WeatherAlertBinder extends Binder {
        public WeatherAlertService getService() {
            return WeatherAlertService.this;
        }
    }

    private final Runnable requestRunnable = new Runnable() {

        @Override
        public void run() {
            performRunnable();
        }
    };

    private void performRunnable() {
        AlertDatabaseManager db = AlertDatabaseManager.getInstance();
        List<WeatherAlert> weatherAlertList = db.readWeatherAlerts();

        if(weatherAlertList.size() < 1) {
            WeatherAlertService.this.stopSelf();
        }
        else {

            SyncAdapterHandler syncAdapterHandler = new SyncAdapterHandler(getString(R.string.content_authority));
            AgBaseAccountWorker worker = new AgBaseAccountWorker(this, getString(R.string.account_type));
            Account account = worker.getLastAccount();

            if(account != null) {

                for (WeatherAlert alert : weatherAlertList) {
                    if (alert != null) {
                        syncAdapterHandler.checkWeatherAlert(this, account, alert.getId());
                    }
                }
                weatherAlertHandler.postDelayed(requestRunnable, 120 * 1000);
            }
        }
    }

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if(WeatherSyncAdapter.WEATHER_ALERT.equals(action)) {
                int weatherAlertId = intent.getIntExtra(WeatherSyncAdapter.ARGS_WEATHER_ALERT, -1);

                if(weatherAlertId != -1) {
                    AlertDatabaseManager db = AlertDatabaseManager.getInstance();
                    WeatherAlert alert = db.readWeatherAlert(weatherAlertId);
                    onWeatherAlertReceive(alert);
                }
            }
        }
    };

    private void onWeatherAlertReceive(WeatherAlert alert) {

        // get active alert if exists
        AlertDatabaseManager db = AlertDatabaseManager.getInstance();
        ActiveAlert activeAlert = db.readActiveAlertForWeatherAlert(alert.getId());

        // create timestamp
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        timeStampFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDate = new Date();
        String timestamp = timeStampFormat.format(startDate);

        // if alert is not in active alert table, save it and send notification
        if(activeAlert == null) {

            activeAlert = new ActiveAlert();
            activeAlert.setWeatherAlertId(alert.getId());
            activeAlert.setAlertStart(timestamp);
            activeAlert.setAlertLast(timestamp);

            db.createActiveAlert(activeAlert);
            createNotification(alert.getName(), alert.getId());
        }
        // if alert is in active alert table, update the last timestamp
        else {
            activeAlert.setAlertLast(timestamp);
            db.updateActiveAlert(activeAlert);
        }
    }

    private void createNotification(String title, int weatherAlertId) {

        Intent intent = new Intent(this, ViewWeatherAlertActivity.class);
        intent.putExtra(ViewWeatherAlertActivity.ARGS_WEATHER_ALERT, weatherAlertId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent. FLAG_UPDATE_CURRENT );

        Notification notification  =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.app_icon)
                        .setContentIntent(pendingIntent)
                        .setContentTitle("Weather Alert!")
                        .setContentText(title)
                        .build();

           notification.defaults |= Notification.DEFAULT_VIBRATE;
           notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notification);
    }
}