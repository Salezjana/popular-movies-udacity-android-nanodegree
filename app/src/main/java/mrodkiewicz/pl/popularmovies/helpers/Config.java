package mrodkiewicz.pl.popularmovies.helpers;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import mrodkiewicz.pl.popularmovies.BuildConfig;

import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_ID;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_ID;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_OVERVIEW;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_POSTERPATH;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_RELEASEDATE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_REVIEWS;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_TITLE;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_VIDEOS;
import static mrodkiewicz.pl.popularmovies.helpers.Config.MovieEntry.KEY_MOVIE_VOTEAVERAGE;

/**
 * Created by Mikolaj Rodkiewicz on 21.02.2018.
 */

public class Config {
    public static String API_KEY = BuildConfig.MovieDbApiKey;

    public static String API_URL = "https://api.themoviedb.org/3/";
    public static String API_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static String API_IMAGE_SIZE_W154 = "/w154/";
    public static String API_IMAGE_SIZE_W185 = "/w185/";
    public static String API_IMAGE_SIZE_W324 = "/w324/";
    public static String PREFERENCES_KEY = "PopularMoviesKey";
    public static String PREFERENCES_SORTING_POSITION = "PopularMoviesKeySorting";
    public static String RECYCLEVIEW_POSITION_KEY = "RECYCLEVIEW_POSITION_KEY";
    public static String RECYCLEVIEW_LIST_KEY = "RECYCLEVIEW_LIST_KEY";
    public static String RECYCLEVIEW_PAGE_KEY = "RECYCLEVIEW_PAGE_KEY";

    public static int DATABASE_VERSION = 1;
    public static String DATABASE_NAME = "movies";
    public static String TABLE_MOVIE = "favourites";

    public static String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_MOVIE;

    public static final String CONTENT_AUTHORITY = "pl.mrodkiewicz.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class MovieEntry implements BaseColumns {
        public static String DATABASE_NAME = "movies";

        public static String KEY_ID = "_ID";
        public static String KEY_MOVIE_ID = "MOVIE_ID";
        public static String KEY_MOVIE_TITLE = "TITLE";
        public static String KEY_MOVIE_OVERVIEW = "OVERVIEW";
        public static String KEY_MOVIE_VOTEAVERAGE = "VOTEAVERAGE ";
        public static String KEY_MOVIE_POSTERPATH = "POSTERPATH";
        public static String KEY_MOVIE_RELEASEDATE = "RELEASEDATE";
        public static String KEY_MOVIE_VIDEOS = "VIDEOS";
        public static String KEY_MOVIE_REVIEWS = "REVIEWS";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(DATABASE_NAME).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + DATABASE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + DATABASE_NAME;

        public static Uri buildFlavorsUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static String DATABASE_CREATE = "CREATE TABLE " + Config.TABLE_MOVIE + "("
            + KEY_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + KEY_MOVIE_ID + " INTEGER NOT NULL,"
            + KEY_MOVIE_TITLE + " TEXT NOT NULL,"
            + KEY_MOVIE_OVERVIEW + " TEXT,"
            + KEY_MOVIE_VOTEAVERAGE + " REAL,"
            + KEY_MOVIE_POSTERPATH + " TEXT,"
            + KEY_MOVIE_RELEASEDATE + " TEXT,"
            + KEY_MOVIE_VIDEOS + " TEXT,"
            + KEY_MOVIE_REVIEWS + " TEXT,"
            + "UNIQUE (" + KEY_MOVIE_ID + ") ON CONFLICT REPLACE)";


}
