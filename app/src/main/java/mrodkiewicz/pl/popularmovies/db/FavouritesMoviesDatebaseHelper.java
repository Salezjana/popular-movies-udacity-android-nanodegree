package mrodkiewicz.pl.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mrodkiewicz.pl.popularmovies.helpers.Config;
import mrodkiewicz.pl.popularmovies.model.Movie;

import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_ID;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_OVERVIEW;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_POSTERPATH;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_RELEASEDATE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_TITLE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_VOTEAVERAGE;

/**
 * Created by pc-mikolaj on 24.03.2018.
 */

public class FavouritesMoviesDatebaseHelper extends SQLiteOpenHelper {
    private Context context;

    public FavouritesMoviesDatebaseHelper(Context context) {
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
        values.put(KEY_MOVIE_ID, movie.getId());
        values.put(KEY_MOVIE_TITLE, movie.getTitle());
        values.put(KEY_MOVIE_OVERVIEW, movie.getOverview());
        values.put(KEY_MOVIE_VOTEAVERAGE, movie.getVoteAverage());
        values.put(KEY_MOVIE_POSTERPATH, movie.getPosterPath());
        values.put(KEY_MOVIE_RELEASEDATE, movie.getReleaseDate());
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
        sqLiteDatabase.delete(Config.TABLE_MOVIE, KEY_MOVIE_ID + "=" + movie.getId(), null);
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
