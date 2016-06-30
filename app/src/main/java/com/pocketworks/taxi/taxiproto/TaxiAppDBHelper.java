package com.pocketworks.taxi.taxiproto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vasco on 30/06/2016.
 */
public class TaxiAppDBHelper extends SQLiteOpenHelper {
        // Database Info
        private static final String DATABASE_NAME = "TaxiDB";
        private static final int DATABASE_VERSION = 1;

        // Table Names
        private static final String TABLE_FAVOURITES = "favourites";

        // Favourites Table Columns
        private static final String KEY_FAVOURITE_ID = "fav_id";
        private static final String KEY_FAVOURITE_CITY = "city";
        private static final String KEY_FAVOURITE_ADDRESS = "address";
        private static final String KEY_FAVOURITE_POST_CODE = "post_code";


    private static TaxiAppDBHelper sInstance;

        public static synchronized TaxiAppDBHelper getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            if (sInstance == null) {
                sInstance = new TaxiAppDBHelper(context.getApplicationContext());
            }
            return sInstance;
        }

        /**
         * Constructor should be private to prevent direct instantiation.
         * Make a call to the static method "getInstance()" instead.
         */
        private TaxiAppDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Called when the database connection is being configured.
        // Configure database settings for things like foreign key support, write-ahead logging, etc.
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        // Called when the database is created for the FIRST time.
        // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_FAVOURITES +
                    "(" +
                    KEY_FAVOURITE_ID + " INTEGER PRIMARY KEY," + //primary key
                    KEY_FAVOURITE_ADDRESS + " INTEGER PRIMARY KEY," +
                    KEY_FAVOURITE_CITY + " TEXT," +
                    KEY_FAVOURITE_POST_CODE + " TEXT" +
                    ")";

            db.execSQL(CREATE_FAVOURITE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
                onCreate(db);
            }
        }

        public void addFavourite(Favourite favourite) {

            SQLiteDatabase db = getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(KEY_FAVOURITE_ID, favourite.id);
                values.put(KEY_FAVOURITE_CITY, favourite.city_name);
                values.put(KEY_FAVOURITE_ADDRESS, favourite.address);
                values.put(KEY_FAVOURITE_POST_CODE, favourite.post_code);

                db.insertOrThrow(TABLE_FAVOURITES, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.d("FavouriteID:"+String.valueOf(favourite.id), "Error while trying to add favourite to database");
            } finally {
                db.endTransaction();
            }
        }

        // Get all favourites in the database
        public List<Favourite> getAllFavourites() {
            List<Favourite> favourites = new ArrayList<>();

            String FAV_SELECT_QUERY =
                    String.format("SELECT * FROM %s ",
                            TABLE_FAVOURITES);

            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(FAV_SELECT_QUERY, null);
            try {
                if (cursor.moveToFirst())
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex(KEY_FAVOURITE_ID));
                        String address = cursor.getString(cursor.getColumnIndex(KEY_FAVOURITE_ADDRESS));
                        String city = cursor.getString(cursor.getColumnIndex(KEY_FAVOURITE_CITY));
                        String post_code = cursor.getString(cursor.getColumnIndex(KEY_FAVOURITE_POST_CODE));

                        Favourite newFavourite = new Favourite(id, address, city, post_code);

                        favourites.add(newFavourite);
                    } while(cursor.moveToNext());

            } catch (Exception e) {
                //TODO: find something to put in log and help to trace eventual problems
                Log.d("", "Error while trying to get All Favourites from database");
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
            return favourites;
        }

    // check if city is already stored in favourites database
    public boolean checkIfExists(int fav_id) {
        SQLiteDatabase db = getReadableDatabase();
        db.beginTransaction();

        String FAV_SELECT_QUERY =
                String.format("SELECT * FROM favourites WHERE fav_id=%d",
                        fav_id);

        Cursor cursor=db.rawQuery(FAV_SELECT_QUERY, null);

        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }


    // Delete a Favourite
    public void deleteFavourite(int fav_id) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_FAVOURITES, KEY_FAVOURITE_ID + " = ?",
                    new String[] { String.valueOf(fav_id) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("FavID:"+String.valueOf(fav_id), "Error while trying to delete favourite from database");
        } finally {
            db.endTransaction();
        }
    }



}

