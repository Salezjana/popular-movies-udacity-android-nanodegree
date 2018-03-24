package mrodkiewicz.pl.popularmovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mrodkiewicz.pl.popularmovies.helpers.Config;

/**
 * Created by pc-mikolaj on 24.03.2018.
 */

public class FavouritesMoviesDatebaseHandler extends SQLiteOpenHelper {
    private Context context;

    public FavouritesMoviesDatebaseHandler(Context context) {
        super(context, Config.DATABASE_NAME, null, Config.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Config.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Config.DATABASE_DROP);
        onCreate(db);
    }


}
