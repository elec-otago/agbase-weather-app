package nz.ac.elec.agbase.weather_app.agbase_sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests.WeatherRequest;

/**
 * SyncAdapterHandler.java
 *
 * This class is responsible for sending sync requests to the
 * sync adapter.
 *
 * Created by tm on 19/04/16.
 */
public class SyncAdapterHandler {

    private final String TAG = "SyncAdapterHandler";

    private String mResolverAuthority;


    public SyncAdapterHandler(String resolverAuthority) {
        mResolverAuthority = resolverAuthority;
    }

    public void performInitSync(Context context, Account account) {
        Bundle b = new Bundle();
        AccountManager manager = AccountManager.get(context);

        b.putBoolean(context.getString(R.string.ARGS_DB_INIT), true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(account, mResolverAuthority, b);
        ContentResolver.setSyncAutomatically(account, mResolverAuthority, true);
    }

    public void performUpdate(Context context, Account account) {
        Bundle b = new Bundle();
        AccountManager manager = AccountManager.get(context);

        b.putBoolean(context.getString(R.string.ARGS_DB_UPDATE), true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, mResolverAuthority, b);
        ContentResolver.setSyncAutomatically(account, mResolverAuthority, true);
    }

    public void getLastWeatherMeasurement(Context context, Account account, String guid) {

        Bundle b = new Bundle();

        b.putBoolean(WeatherSyncAdapter.ARGS_GET_WEATHER, true);
        b.putString(WeatherRequest.ARGS_DEVICE, guid);
        b.putSerializable(WeatherRequest.ARGS_LIMIT, 1);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, mResolverAuthority, b);
        ContentResolver.setSyncAutomatically(account, mResolverAuthority, true);
    }

    public void checkWeatherAlert(Context context, Account account, int weatherAlertId) {

        Bundle b = new Bundle();
        b.putBoolean(WeatherSyncAdapter.ARGS_CHECK_ALERT, true);
        b.putInt(WeatherSyncAdapter.ARGS_WEATHER_ALERT, weatherAlertId);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, mResolverAuthority, b);
        ContentResolver.setSyncAutomatically(account, mResolverAuthority, true);
    }
}
