package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpraveen on 10/15/16.
 */
@Parcel
public class Movie {

  String title;
  String posterImage;
  String backDropImage;
  String overview;
  String releaseDate;
  double rating;
  long ID;
  long voteCount;

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

  public String getVideoAPIURL() {
    return "https://api.themoviedb.org/3/movie/" + ID +
      "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
  }

  public String getVoteCount() {
    return "" + voteCount;
  }

  @Override
  public String toString() {
    return title;
  }

  public Movie() {
  }

  public Movie(JSONObject movieRecord) throws JSONException {
    this.title = movieRecord.getString("title");
    this.posterImage = movieRecord.getString("poster_path");
    this.backDropImage = movieRecord.getString("backdrop_path");
    this.overview = movieRecord.getString("overview");
    this.releaseDate = movieRecord.getString("release_date");
    this.rating = movieRecord.getDouble("vote_average");
    this.ID = movieRecord.getLong("id");
    this.voteCount = movieRecord.getLong("vote_count");
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
