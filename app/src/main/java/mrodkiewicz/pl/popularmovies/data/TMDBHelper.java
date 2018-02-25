package mrodkiewicz.pl.popularmovies.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static mrodkiewicz.pl.popularmovies.data.TMDBContract.FavouritesEntry.TABLE_NAME;
import static mrodkiewicz.pl.popularmovies.data.TMDBContract.FavouritesEntry._ID;
import static mrodkiewicz.pl.popularmovies.data.TMDBContract.FavouritesEntry.isFAVOURITE;

/**
 * Created by pc-mikolaj on 25.02.2018.
 */

public class TMDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tmdb.db";
    private static final int DATABASE_VERSION = 1;


    public TMDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + TABLE_NAME + " ("
                + _ID + " INTEGER NOT NULL); "
                + isFAVOURITE + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
