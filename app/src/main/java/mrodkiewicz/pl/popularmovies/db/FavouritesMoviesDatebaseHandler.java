package mrodkiewicz.pl.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.model.Movie;

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

    public void addMovie(Movie movie) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Config.KEY_MOVIE_ID, movie.getId());
        values.put(Config.KEY_MOVIE_TITLE, movie.getTitle());
        values.put(Config.KEY_MOVIE_OVERVIEW, movie.getOverview());
        values.put(Config.KEY_MOVIE_VOTEAVERAGE, movie.getVoteAverage());
        values.put(Config.KEY_MOVIE_POSTERPATH, movie.getPosterPath());
        values.put(Config.KEY_MOVIE_RELEASEDATE, movie.getReleaseDate());
        sqLiteDatabase.insertOrThrow(Config.TABLE_MOVIE, null, values);
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> list = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + Config.TABLE_MOVIE;

        SQLiteDatabase db = this.getReadableDatabase();
        try {

            Cursor cursor = db.rawQuery(selectQuery, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        Movie obj = new Movie();
                        obj.setId(cursor.getInt(1));
                        obj.setTitle(cursor.getString(2));
                        obj.setOverview(cursor.getString(3));
                        obj.setPosterPath(cursor.getString(5));
                        obj.setReleaseDate(cursor.getString(6));

                        list.add(obj);
                    } while (cursor.moveToNext());
                }

            } finally {
                try {
                    cursor.close();
                } catch (Exception ignore) {
                }
            }

        } finally {
            try {
                db.close();
            } catch (Exception ignore) {
            }
        }

        return list;
    }

    public void deleteMovie(Movie movie){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(Config.TABLE_MOVIE, Config.KEY_MOVIE_ID + "=" + movie.getId(), null);
    }
    public boolean isInDatabase(int id){
        boolean isInDatabase = false;
        ArrayList<Movie> movies = getAllMovies();
        for (Movie movie: movies){
            if (id == movie.getId()){
                isInDatabase = true;
            }
        }
        return isInDatabase;
    }

}
