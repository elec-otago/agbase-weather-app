package nz.ac.elec.agbase.weather_app.services;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import nz.ac.elec.agbase.weather_app.AgBaseAccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.SyncAdapterHandler;

/**
 * UpdateService.java
 *
 * This service is responsible for making API requests for data updates
 * via the SyncAdapter class.  This service should exist only if the app
 * is running.
 *
 * Created by tm on 3/05/16.
 */
public class UpdateService extends Service {

    private final String TAG = "UpdateService";


    private final IBinder mBinder = new UpdateServiceBinder();
    protected Handler mHandler;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            performRunnable();

        }
    };

    private boolean isRunning = false;

    @Override
    public IBinder onBind(Intent intent) {
        onStart();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        onStart();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanUp();
    }

    private void onStart() {
        isRunning = true;
        mHandler = new Handler();
        mHandler.post(mRunnable);
    }

    private void cleanUp() {
        isRunning = false;
        mHandler.removeCallbacks(mRunnable); // this doesn't seem to stop it (or does it?)
    }

    private void performRunnable() {
        if(isRunning) {
            AgBaseAccountWorker accountWorker = new AgBaseAccountWorker(this, getString(R.string.account_type));
            Account account = accountWorker.getLastAccount();

            if(account != null) {
                SyncAdapterHandler handler = new SyncAdapterHandler(getString(R.string.content_authority));
                handler.performUpdate(this, account);
            }
            mHandler.postDelayed(mRunnable, 30 * 1000);
        }
    }

    public class UpdateServiceBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}