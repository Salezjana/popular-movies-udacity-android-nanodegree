package mrodkiewicz.pl.popularmovies.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by pc-mikolaj on 24.02.2018.
 * Saving,reading favourites films
 */

public class Favourites{
    private ArrayList<Integer> favouritesIDMovies;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Favourites(Context context) {
        favouritesIDMovies = new ArrayList<Integer>();
        Timber.d("Favourites");
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
        if(j == -1){
            Timber.d("isOnList "+i+"FALSE");
            return false;
        }else{
            Timber.d("isOnList "+i+" TRUE");
            return true;
        }

    }

    public void LogFavouritesMovies(){
        Timber.d("Favourites" + favouritesIDMovies.toString());
    }
    public ArrayList<Integer> getFavouritesMovies(){
     return favouritesIDMovies;
    }
}
