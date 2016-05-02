package nz.ac.elec.agbase.weather_app;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.preferences.AppPreferences;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceContentProvider;
import nz.ac.elec.agbase.weather_app.preferences.PreferenceHandler;

/**
 * Created by tm on 28/01/16.
 */
public class AgBaseAccountWorker extends AccountWorker {

    private final String TAG = "AgBaseAccountWorker";
    public AgBaseAccountWorker() {
        super();
    }
    public AgBaseAccountWorker(Context context, String accountType) {
        super(context, accountType);
    }


    @Override
    protected void setLastAccountPref(int accountPos, Account[] accounts) {
        PreferenceHandler.getInstance()
                .setLastLoginAccount(accounts[accountPos], accountPos);
    }
    @Override
    protected void setAccountLoggedIn(boolean loggedIn) {
        PreferenceHandler.getInstance()
                .setAccountLoggedIn(loggedIn);
    }
    @Override
    protected boolean getAccountLoggedIn() {
        return PreferenceHandler.getInstance()
                .getAccountLoggedIn();
    }
    @Override
    protected String getLastAccountName() {
        return PreferenceHandler.getInstance()
                .getLastLoginAccountName();
    }

    @Override
    protected int getLastAccountPos() {
        return PreferenceHandler.getInstance()
                .getLastLoginAccountPos();
    }
}
