package mrodkiewicz.pl.popularmovies.api;

import mrodkiewicz.pl.popularmovies.model.Movie;
import mrodkiewicz.pl.popularmovies.model.MoviesResponse;
import mrodkiewicz.pl.popularmovies.model.ReviewsResponse;
import mrodkiewicz.pl.popularmovies.model.TrailersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Mikolaj Rodkiewicz on 21.02.2018.
 */

public interface APIService {

    @GET("movie/{sort}")
    Call<MoviesResponse> getMovies(@Path("sort") String sort, @Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{id}")
    Call<Movie> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);

}
