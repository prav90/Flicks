package com.codepath.flicks.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.codepath.flicks.R;
import com.codepath.flicks.adapters.FeedMovieAdapter;
import com.codepath.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MoviesActivity extends AppCompatActivity {

  @BindView(R.id.lvMovies) ListView lvMovies;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
  FeedMovieAdapter feedMoviesAdapter;
  ArrayList<Movie> movies;

  private String currentAPIEndPoint;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_movies);
    ButterKnife.bind(this);
    movies = new ArrayList<>();
    feedMoviesAdapter = new FeedMovieAdapter(this, movies);
    currentAPIEndPoint = getString(R.string.now_playing_url);
    lvMovies.setAdapter(feedMoviesAdapter);
    // Setup refresh listener which triggers new data loading
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        if (currentAPIEndPoint.equals(getString(R.string.now_playing_url))) {
          currentAPIEndPoint = getString(R.string.popular_movies_url);
          getSupportActionBar().setTitle(R.string.flicks_popular_title);
        } else {
          currentAPIEndPoint = getString(R.string.now_playing_url);
          getSupportActionBar().setTitle(R.string.flicks_now_playing_title);
        }
        fetchMovieFeed();
      }
    });
    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(
      android.R.color.darker_gray
    );

    fetchMovieFeed();
  }

  public void fetchMovieFeed() {
    AsyncHttpClient httpClient = new AsyncHttpClient();
    httpClient.get(
      currentAPIEndPoint,
      new JsonHttpResponseHandler(){
        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
          super.onFailure(statusCode, headers, throwable, errorResponse);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
          feedMoviesAdapter.clear();
          movies.clear();
          movies.addAll(Movie.fromJSONObject(response));
          feedMoviesAdapter.notifyDataSetChanged();
          swipeContainer.setRefreshing(false);
        }
      }
    );
  }

}
