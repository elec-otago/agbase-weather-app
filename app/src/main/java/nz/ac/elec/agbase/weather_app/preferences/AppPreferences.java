package nz.ac.elec.agbase.weather_app.preferences;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

/**
 * AppPreferences.java
 *
 * This class provides an interface for other classes to save user preferences
 * with the PreferenceContentProvider class.
 *
 * Created by Tim Miller, 18/01/2016.
 */
public class AppPreferences {

    private Context mContext;
    private final String CONTENT_PREFIX = "content://";

    public AppPreferences(final Context context) {
        mContext = context;
    }

    /**
     * Sets an integer preference.
     */
    public void storeInt(String key, int value) {
        ContentValues values = new ContentValues();
        values.put(nz.ac.elec.agbase.weather_app.preferences.PreferenceContentProvider.KEY_COL, key);
        values.put(nz.ac.elec.agbase.weather_app.preferences.PreferenceContentProvider.VALUE_COL, value);

        Uri uri =
        Uri.parse(CONTENT_PREFIX + nz.ac.elec.agbase.weather_app.preferences.PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
            nz.ac.elec.agbase.weather_app.preferences.PreferenceContentProvider.INTEGER_PATH);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            cr.insert(uri, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cr.release();
    }

    /**
     * Sets a double preference.
     */
    public void storeDouble(String key, double value) {
        ContentValues values = new ContentValues();
        values.put(PreferenceContentProvider.KEY_COL, key);
        values.put(PreferenceContentProvider.VALUE_COL, value);

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.DOUBLE_PATH);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            cr.insert(uri, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cr.release();
    }

    /**
     * Sets a string preference.
     */
    public void storeString(String key, String value) {
        ContentValues values = new ContentValues();
        values.put(PreferenceContentProvider.KEY_COL, key);
        values.put(PreferenceContentProvider.VALUE_COL, value);

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.STRING_PATH);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            cr.insert(uri, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cr.release();
    }

    /**
     * Sets a boolean preference.
     */
    public void storeBoolean(String key, boolean value) {
        ContentValues values = new ContentValues();
        int i = value == false? 0 : 1;
        values.put(PreferenceContentProvider.KEY_COL, key);
        values.put(PreferenceContentProvider.VALUE_COL, value);

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.BOOLEAN_PATH);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            cr.insert(uri, values);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cr.release();
    }

    //region get

    /**
     * Gets an integer preference.
     */
    public int getInt(String key, int defaultValue) {
        int ret = defaultValue;

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.INTEGER_PATH + "/" + key);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            Cursor cursor = cr.query(uri, null, null, null, null);
            if(cursor.getCount() == 1 && cursor.moveToFirst()) {
                ret = cursor.getInt(0);
            }
            cursor.close();
        }
        catch(RemoteException e) {
            e.printStackTrace();
        }
        cr.release();

        return ret;
    }

    /**
     * Gets a double preference.
     */
    public double getDouble(String key, double defaultValue) {
        double  ret = defaultValue;

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.DOUBLE_PATH + "/" + key);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            Cursor cursor = cr.query(uri, null, null, null, null);
            if(cursor.getCount() == 1 && cursor.moveToFirst()) {
                ret = cursor.getDouble(0);
            }
            cursor.close();
        }
        catch(RemoteException e) {
            e.printStackTrace();
        }
        cr.release();

        return ret;
    }

    /**
     * Gets a string preference.
     */
    public String getString(String key, String defaultValue) {
        String ret = defaultValue;

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.STRING_PATH + "/" + key);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            Cursor cursor = cr.query(uri, null, null, null, null);
            if(cursor.getCount() == 1 && cursor.moveToFirst()) {
                ret = cursor.getString(0);
            }
            cursor.close();
        }
        catch(RemoteException e) {
            e.printStackTrace();
        }
        cr.release();

        return ret;
    }

    /**
     * Gets a boolean preference.
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        boolean ret = defaultValue;

        Uri uri =
        Uri.parse(CONTENT_PREFIX + PreferenceContentProvider.PROVIDER_AUTHORITY + "/" +
                PreferenceContentProvider.BOOLEAN_PATH + "/" + key);

        ContentProviderClient cr =
        mContext.getContentResolver().acquireContentProviderClient(uri);

        try {
            Cursor cursor  = cr.query(uri, null, null, null, null);
            int count = cursor.getCount();
            if(cursor.getCount() == 1 && cursor.moveToFirst()) {
                int i = cursor.getInt(0);
                ret = i == 0 ? false : true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cr.release();

        return ret;
    }
    //endregion
}
