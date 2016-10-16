package com.codepath.flicks.network;

import com.codepath.flicks.models.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by rpraveen on 10/15/16.
 */
public class HttpNetworkUtils {

  public static List<Movie> getMoviesFromAPI(String URL) {


    JsonResponseHandler responseHandlerImpl = new JsonResponseHandler() ;


    return responseHandlerImpl.getMovieList();
  }
}

class JsonResponseHandler extends JsonHttpResponseHandler
{

  List<Movie> movieList;

  public List<Movie> getMovieList() {
    return movieList;
  }

  @Override
  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
    movieList = Movie.fromJSONObject(response);
  }

  @Override
  public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
    super.onFailure(statusCode, headers, throwable, errorResponse);
  }
}