package nz.ac.elec.agbase.weather_app.agbase_sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by tm on 19/04/16.
 */
public class SyncAdapterService extends Service {

    private static WeatherSyncAdapter mSyncAdapter = null;
    private static final Object lock = new Object();

    @Override
    public void onCreate() {

        synchronized(lock) {
            if(mSyncAdapter == null) {
                mSyncAdapter = new WeatherSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
