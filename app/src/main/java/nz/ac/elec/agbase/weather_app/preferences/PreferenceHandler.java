package nz.ac.elec.agbase.weather_app.preferences;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import nz.ac.elec.agbase.android_agbase_login.AccountWorker;
import nz.ac.elec.agbase.weather_app.R;
import nz.ac.elec.agbase.weather_app.preferences.AppPreferences;

/**
 * SharedPreferenceHandler.java
 *
 * The purpose of this class is to handle saving and retrieving
 * shared preference values in the condition score app.
 *
 * Created by Tim Miller, 25/11/2015.
 */
public class PreferenceHandler {

    private final String TAG = "SharedPreferenceHandler";

    public static final int INT_NOT_FOUND       = Integer.MIN_VALUE;
    public static final double DOUBLE_NOT_FOUND = Double.MIN_VALUE;

    private static PreferenceHandler instance;
    private static Context mContext;

    private PreferenceHandler(Context context) {
        mContext = context;
    }

    public static void initialize(Context context) {
        if(instance == null) {
            instance = new PreferenceHandler(context);
        }
    }

    public static synchronized PreferenceHandler getInstance() {
        return instance;
    }

    // region init preference

    /**
     * Sets the account parameter's perform initialization preference.
     */
    public void setPerformInitPreference(Account account, boolean initPref) {

        AppPreferences prefs = getPrefs();
        prefs.storeBoolean(account.name + mContext.getString(R.string.PREFS_DO_INIT), initPref);
    }

    /**
     * Gets the account parameter's perform initialization preference.
     */
    public boolean getPerformInitPreference(Account account) {

        AppPreferences prefs = getPrefs();
        return prefs.getBoolean(account.name + mContext.getString(R.string.PREFS_DO_INIT), false);
    }

    /**
     * Sets the account's initialization complete preference.
     */
    public void setInitCompletePreference(Account account, boolean isComplete) {

        AppPreferences prefs = getPrefs();
        prefs.storeBoolean(account.name + mContext.getString(R.string.PREFS_SYNC_DONE), isComplete);
    }

    /**
     * Gets the account's initialization complete preference.
     */
    public boolean getInitCompletePreference(Account account) {

        AppPreferences prefs = getPrefs();
        return prefs.getBoolean(account.name + mContext.getString(R.string.PREFS_SYNC_DONE), false);
    }

    // endregion

    // region logged in account

    /**
     * Sets the last logged in account by storing the account's array position.
     */
    public void setLastLoginAccount(Account account, int accountPos) {
        AppPreferences prefs = getPrefs();
        String accountNamePref = mContext.getString(R.string.PREFS_LAST_ACC_NAME);
        String accountPosPref = mContext.getString(R.string.PREFS_LAST_ACC_POS);

        if(account != null) {
            prefs.storeString(accountNamePref, account.name);
        }
        else { prefs.storeString(accountNamePref, null); }
        prefs.storeInt(accountPosPref, accountPos);
    }

    /**
     * Gets the last logged in account's array position.
     */
    public int getLastLoginAccountPos() {
        AppPreferences prefs = getPrefs();
        String accountPosPref = mContext.getString(R.string.PREFS_LAST_ACC_POS);
        return prefs.getInt(accountPosPref, -1);
    }

    /**
     * Gets the name of the last logged in account.
     */
    public String getLastLoginAccountName() {
        AppPreferences prefs = getPrefs();
        String accountNamePref = mContext.getString(R.string.PREFS_LAST_ACC_NAME);
        return prefs.getString(accountNamePref, null);
    }

    /**
     * Sets a preference that indicates if the last account preference
     * wishes to stay logged in.
     */
    public void setAccountLoggedIn(boolean isLoggedIn) {
        AppPreferences prefs = getPrefs();
        String accountLoginPref = mContext.getString(R.string.PREFS_LAST_ACC_LOGIN);
        prefs.storeBoolean(accountLoginPref, isLoggedIn);
    }

    public boolean getAccountLoggedIn() {
        AppPreferences prefs = getPrefs();
        String accountLoginPref = mContext.getString(R.string.PREFS_LAST_ACC_LOGIN);
        return prefs.getBoolean(accountLoginPref, false);
    }
    // endregion

    // region farm preference

    /**
     * Sets the account parameter's preferred farm id.
     */
    public void setFarmPreference(Account account, int farmId) {

        AppPreferences prefs = getPrefs();
        prefs.storeInt(account.name + mContext.getString(R.string.PREFS_FARM), farmId);
    }

    /**
     * Gets the account parameter's preferred farm id.
     */
    public int getFarmPreference(Account account) {

        AppPreferences prefs = getPrefs();
        return prefs.getInt(account.name + mContext.getString(R.string.PREFS_FARM), INT_NOT_FOUND);
    }
    // endregion

    // region herd preference

    /**
     * Sets the account parameter's preferred herd id.
     */
    public void setHerdPreference(Account account, int herdId) {

        AppPreferences prefs = getPrefs();
        prefs.storeInt(account.name + mContext.getString(R.string.PREFS_HERD), herdId);
    }

    /**
     * Gets the account parameter's preferred herd id.
     */
    public int getHerdPreference(Account account) {

        AppPreferences prefs = getPrefs();
        return prefs.getInt(account.name + mContext.getString(R.string.PREFS_HERD), INT_NOT_FOUND);
    }
    // endregion

    // region algorithm preference
    /**
     * Sets the account parameter's preferred algorithm id.
     */
    public void setAlgorithmPreference(Account account, int algorithmId) {

        AppPreferences prefs = getPrefs();
        prefs.storeInt(account.name + mContext.getString(R.string.PREFS_ALGO), algorithmId);

    }

    /**
     * Gets the account parameter's preferred algorithm id.
     */
    public int getAlgorithmPreference(Account account) {

        AppPreferences prefs = getPrefs();
        return prefs.getInt(account.name + mContext.getString(R.string.PREFS_ALGO), INT_NOT_FOUND);
    }
    // endregion

    private AppPreferences getPrefs() {
        return new AppPreferences(mContext);
    }
}