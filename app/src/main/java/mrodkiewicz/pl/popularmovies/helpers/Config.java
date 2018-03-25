package mrodkiewicz.pl.popularmovies.helpers;


import mrodkiewicz.pl.popularmovies.BuildConfig;

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
    public static String INTEGER = "INTEGER";
    public static String TEXT = "TEXT";

    public static String KEY_ID = "_ID";
    public static String KEY_MOVIE_ID = "MOVIE_ID";
    public static String KEY_MOVIE_ADULT = "ADULT";
    public static String KEY_MOVIE_BACKDROPPATH = "BACKDROPPATH";
    public static String KEY_MOVIE_BUDGET = "BUDGET";
    public static String KEY_MOVIE_HOMEPAGE = "HOMEPAGE";
    public static String KEY_MOVIE_IMDBID = "IMDBID";
    public static String KEY_MOVIE_ORIGINALLANGUAGE = "ORIGINALLANGUAGE";
    public static String KEY_MOVIE_ORIGINALTITLEEMAIL = "ORIGINALTITLEEMAIL";
    public static String KEY_MOVIE_OVERVIEW = "OVERVIEW";
    public static String KEY_MOVIE_POPULARITY = "POPULARITY";
    public static String KEY_MOVIE_POSTERPATH = "POSTERPATH";
    public static String KEY_MOVIE_RELEASEDATE = "RELEASEDATE";
    public static String KEY_MOVIE_REVENUE = "REVENUE";
    public static String KEY_MOVIE_RUNTIME = "RUNTIME";
    public static String KEY_MOVIE_STATUS = "STATUS";
    public static String KEY_MOVIE_TAGLINE = "TAGLINE";
    public static String KEY_MOVIE_TITLE = "TITLE";
    public static String KEY_MOVIE_VIDEOS = "VIDEOS";
    public static String KEY_MOVIE_REVIEWS = "REVIEWS";
    public static String KEY_MOVIE_VOTEAVERAGE = "VOTEAVERAGE ";
    public static String KEY_MOVIE_VOTECOUNT = "VOTECOUNT";
    public static String KEY_MOVIE_FAVOURITE = "VOTECOUNT";

    public static String DATABASE_CREATE = "CREATE TABLE " + Config.TABLE_MOVIE + "("
            + Config.KEY_ID + " INTEGER NOT NULL PRIMARY KEY,"
            + Config.KEY_MOVIE_ID + " INTEGER NOT NULL,"
            + Config.KEY_MOVIE_TITLE + " TEXT NOT NULL,"
            + Config.KEY_MOVIE_OVERVIEW + " TEXT,"
            + Config.KEY_MOVIE_VOTEAVERAGE + " REAL,"
            + Config.KEY_MOVIE_POSTERPATH + " TEXT,"
            + Config.KEY_MOVIE_RELEASEDATE + " TEXT,"
            + Config.KEY_MOVIE_VIDEOS + " TEXT,"
            + Config.KEY_MOVIE_REVIEWS + " TEXT,"
            + Config.KEY_MOVIE_FAVOURITE + " INTEGER NOT NULL DEFAULT 0,"
            + "UNIQUE (" + Config.KEY_MOVIE_ID + ") ON CONFLICT REPLACE)";

    public static String DATABASE_DROP = "DROP TABLE IF EXISTS " + TABLE_MOVIE;

}
