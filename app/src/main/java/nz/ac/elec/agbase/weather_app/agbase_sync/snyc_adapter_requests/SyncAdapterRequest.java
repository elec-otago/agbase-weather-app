package nz.ac.elec.agbase.weather_app.agbase_sync.snyc_adapter_requests;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

/**
 * SyncAdapterRequest.java
 *
 * Base class used to perform API calls with the WeatherSyncAdapter
 * class.
 *
 * Created by tm on 28/04/16.
 */
public abstract class SyncAdapterRequest<T> {

    public abstract boolean performRequest(Bundle extras);

    public abstract T getRequestData();
}
