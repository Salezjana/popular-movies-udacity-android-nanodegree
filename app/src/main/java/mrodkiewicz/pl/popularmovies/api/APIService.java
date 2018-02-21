package mrodkiewicz.pl.popularmovies.api;

import mrodkiewicz.pl.popularmovies.model.Movie;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pc-mikolaj on 21.02.2018.
 */

public interface APIService {
    @GET("/movie/{id}")
    Call<Movie> getMovie(@Query("id") int id);


}
