package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 10/15/16.
 */
public class Movie {

  private String title;
  private String posterImage;
  private String backDropImage;
  private String overview;
  private String releaseDate;
  private double rating;

  public String getTitle() {
    return title;
  }

  public String getPosterImage() {
    return "https://image.tmdb.org/t/p/w342"+posterImage;
  }

  public String getBackDropImage() {
    if (!backDropImage.equals("null")) {
      return "https://image.tmdb.org/t/p/w780" + backDropImage;
    }
    return "https://image.tmdb.org/t/p/w780" + posterImage;
  }

  public String getOverview() {
    return overview;
  }

  public String getReleaseDate() {
    return releaseDate;
  }

  public double getRating() {
    return rating;
  }

  @Override
  public String toString() {
    return title;
  }

  public Movie(JSONObject movieRecord) throws JSONException {
    this.title = movieRecord.getString("title");
    this.posterImage = movieRecord.getString("poster_path");
    this.backDropImage = movieRecord.getString("backdrop_path");
    this.overview = movieRecord.getString("overview");
    this.releaseDate = movieRecord.getString("release_date");
    this.rating = movieRecord.getDouble("vote_average");
  }


  public static List<Movie> fromJSONObject(JSONObject movieJSON) {
    ArrayList<Movie> movies = new ArrayList<>();

    try {
      JSONArray movieRecords = movieJSON.getJSONArray("results");
      for (int i = 0; i < movieRecords.length(); i++) {
        movies.add(new Movie(movieRecords.getJSONObject(i)));
      }
    } catch(JSONException e) {
      e.printStackTrace();
    }

    return movies;
  }
}
