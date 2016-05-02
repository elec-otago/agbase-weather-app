package nz.ac.elec.agbase.weather_app.preferences;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * PreferenceContentProvider.java
 *
 * This class stores user preferences by saving them in a database dedicated to
 * saving key value pairs.
 *
 * Created by Tim Miller, 18/01/2016.
 */
public class PreferenceContentProvider extends ContentProvider {

    // the provider authority must match the android:authorities values declared in the manifest
    public static final String PROVIDER_AUTHORITY = "nz.ac.elec.agbase.weather_app.prefs_content_authority";

    private static final int BOOLEAN            = 1;
    private static final int BOOLEAN_ID         = 2;
    private static final int STRING             = 3;
    private static final int STRING_ID          = 4;
    private static final int INTEGER            = 5;
    private static final int INTEGER_ID         = 6;
    private static final int DOUBLE             = 7;
    private static final int DOUBLE_ID          = 8;

    public static final String ID_PATH          = "/*";
    public static final String BOOLEAN_PATH     = "boolean";
    public static final String STRING_PATH      = "string";
    public static final String INTEGER_PATH     = "integer";
    public static final String DOUBLE_PATH      = "double";

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_AUTHORITY, BOOLEAN_PATH, BOOLEAN);
        uriMatcher.addURI(PROVIDER_AUTHORITY, BOOLEAN_PATH + ID_PATH, BOOLEAN_ID);
        uriMatcher.addURI(PROVIDER_AUTHORITY, STRING_PATH, STRING);
        uriMatcher.addURI(PROVIDER_AUTHORITY, STRING_PATH + ID_PATH, STRING_ID);
        uriMatcher.addURI(PROVIDER_AUTHORITY, INTEGER_PATH, INTEGER);
        uriMatcher.addURI(PROVIDER_AUTHORITY, INTEGER_PATH + ID_PATH, INTEGER_ID);
        uriMatcher.addURI(PROVIDER_AUTHORITY, DOUBLE_PATH, DOUBLE);
        uriMatcher.addURI(PROVIDER_AUTHORITY, DOUBLE_PATH + ID_PATH, DOUBLE_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        String id;

        int uriType = uriMatcher.match(uri);
        DatabaseHelper db = new DatabaseHelper(getContext());

        switch(uriType) {
            case BOOLEAN:
                cursor = db.getBoolean(projection, selection, selectionArgs, sortOrder);
                break;
            case BOOLEAN_ID:
                id = uri.getLastPathSegment();
                cursor = db.getBoolean(new String[] {VALUE_COL}, KEY_COL + "=?", new String[] {id}, null);
                break;
            case STRING:
                cursor = db.getString(projection, selection, selectionArgs, sortOrder);
                break;
            case STRING_ID:
                id = uri.getLastPathSegment();
                cursor = db.getString(new String[] {VALUE_COL}, KEY_COL + "=?", new String[] {id}, null);
                break;
            case INTEGER:
                cursor = db.getInt(projection, selection, selectionArgs, sortOrder);
                break;
            case INTEGER_ID:
                id = uri.getLastPathSegment();
                cursor = db.getInt(new String[] {VALUE_COL}, KEY_COL + "=?", new String[] {id}, null);
                break;
            case DOUBLE:
                cursor = db.getDouble(projection, selection, selectionArgs, sortOrder);
                break;
            case DOUBLE_ID:
                id = uri.getLastPathSegment();
                cursor = db.getDouble(new String[] {VALUE_COL}, KEY_COL + "=?", new String[] {id}, null);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        DatabaseHelper db = new DatabaseHelper(getContext());

        switch(uriType) {
            case BOOLEAN:
                db.insertBoolean(values);
                break;
            case STRING:
                db.insertString(values);
                break;
            case INTEGER:
                db.insertInt(values);
                break;
            case DOUBLE:
                db.insertDouble(values);
                break;
            default:
                break;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    // region db
    private SQLiteDatabase db;
    private static final String DB_NAME     = "PrefsDB";
    private static final int DB_VERSION     = 1;
    private static final String INT_TABLE   = "IntPrefs";
    private static final String DBL_TABLE   = "DblPrefs";
    private static final String STR_TABLE   = "StrPrefs";
    private static final String BOOL_TABLE  = "BoolPrefs";
    public static final String KEY_COL     = "key";
    public static final String VALUE_COL   = "value";

    private static String createTable(String tableName, String valueType) {
        return "CREATE TABLE " + tableName + "("
                + KEY_COL + " STRING PRIMARY KEY, "
                + VALUE_COL + " " + valueType + " NOT NULL)";
    }

    private static String dropTable(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("SQL", "onCreate Call");
            db.execSQL(createTable(INT_TABLE, "INTEGER"));
            db.execSQL(createTable(DBL_TABLE, "REAL"));
            // string table can be used to store null values...
            db.execSQL("CREATE TABLE " + STR_TABLE + "("
                    + KEY_COL + " STRING PRIMARY KEY, "
                    + VALUE_COL + " TEXT)");
            db.execSQL(createTable(BOOL_TABLE, "INTEGER"));
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(dropTable(INT_TABLE));
            db.execSQL(dropTable(DBL_TABLE));
            db.execSQL(dropTable(STR_TABLE));
            db.execSQL(dropTable(BOOL_TABLE));
            onCreate(db);
        }

        //region read
        public Cursor getBoolean(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Cursor cursor;

            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(BOOL_TABLE, projection, selection, selectionArgs, null, null, null);

            return cursor;
        }

        public Cursor getString(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Cursor cursor;

            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(STR_TABLE, projection, selection, selectionArgs, null, null, null);

            return cursor;
        }

        public Cursor getInt(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Cursor cursor;

            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(INT_TABLE, projection, selection, selectionArgs, null, null, null);

            return cursor;
        }

        public Cursor getDouble(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            Cursor cursor;

            SQLiteDatabase db = getReadableDatabase();
            cursor = db.query(DBL_TABLE, projection, selection, selectionArgs, null, null, null);

            return cursor;
        }
        //endregion

        //region insert
        public void insertBoolean(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            db.insertWithOnConflict(BOOL_TABLE, KEY_COL, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        public void insertString(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            db.insertWithOnConflict(STR_TABLE, KEY_COL, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        public void insertInt(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            db.insertWithOnConflict(INT_TABLE, KEY_COL, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        public void insertDouble(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            db.insertWithOnConflict(DBL_TABLE, KEY_COL, values, SQLiteDatabase.CONFLICT_REPLACE);
            db.setTransactionSuccessful();
            db.endTransaction();
        }
        //endregion

        //region delete
        //endregion
    }

    // endregion
}