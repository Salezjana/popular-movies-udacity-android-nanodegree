package mrodkiewicz.pl.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by pc-mikolaj on 20.02.2018.
 */

public class PopularMovies{
    private  OkHttpClient client;
    public static Retrofit retrofit;
    public static int TIMEOUT_SECONDS = 20;

    public PopularMovies() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public Retrofit getClient(Context context) {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(context.getResources().getString(R.string.themoviedb_api_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
