package mrodkiewicz.pl.popularmovies.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * Created by pc-mikolaj on 18.03.2018.
 */


public class TrailersResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<Trailer> results = null;

    public TrailersResponse() {
    }


    public TrailersResponse(Integer id, List<Trailer> results) {
        super();
        this.id = id;
        this.results = results;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TrailersResponse withId(Integer id) {
        this.id = id;
        return this;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

    public TrailersResponse withResults(List<Trailer> results) {
        this.results = results;
        return this;
    }

}
