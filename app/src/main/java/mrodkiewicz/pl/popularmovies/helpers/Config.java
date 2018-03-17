package mrodkiewicz.pl.popularmovies.helpers;


import android.content.Context;

import mrodkiewicz.pl.popularmovies.BuildConfig;
import mrodkiewicz.pl.popularmovies.R;

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
    public static String PREFERENCES_KEY= "PopularMoviesKey";
    public static String PREFERENCES_SORTING_POSITION = "PopularMoviesKeySorting";
    public static String PREFERENCES_RECYCLEVIEW_POSITION = "PREFERENCES_SORTING";
    public static String PREFERENCES_RECYCLEVIEW_LIST = "PREFERENCES_LIST";
}
