package mrodkiewicz.pl.popularmovies.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by pc-mikolaj on 24.02.2018.
 */

public class Favourites{
    private ArrayList<Integer> favouritesIDMovies;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Favourites(Context context) {
        favouritesIDMovies = new ArrayList<Integer>();
        sharedPreferences = context.getSharedPreferences("PopularMoviesApp",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    public void addFavouritesMovies(Integer movieID){
        favouritesIDMovies.add(movieID);
        editor.putString("favourites_movies_string",favouritesIDMovies.toString());
        editor.commit();
    }

    public boolean isOnList(Integer i){
        int j = favouritesIDMovies.indexOf(i);
        if (j== -1){
            Timber.d("FALSE");
        }else{
            Timber.d("TRUE");
        }
        return false;
    }

    public void LogFavouritesMovies(){
        Timber.d(favouritesIDMovies.toString());
    }
    public ArrayList<Integer> getFavouritesMovies(){
     return null;
    }
}
