package mrodkiewicz.pl.popularmovies.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

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
        Timber.d("Favourites");
        sharedPreferences = context.getSharedPreferences("PopularMoviesApp",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        favouritesIDMovies = new ArrayList<Integer>();

        String defaultValue = "favourites_movies_string";
        String savedValue = sharedPreferences.getString("favourites_movies_string", defaultValue);
        Timber.d("Favourites savedvalue: " + savedValue);
        String savedValueForTable = savedValue.replace("[","");
        savedValueForTable = savedValueForTable.replace("]","");
        Timber.d("Favourites savedvalue savedValueForTable: " + savedValueForTable);
        String[] strings = savedValueForTable.split(",");
        for(String w:strings){
            Timber.d("Favourites savedvalue strings[]: " + w);
        }
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

}
