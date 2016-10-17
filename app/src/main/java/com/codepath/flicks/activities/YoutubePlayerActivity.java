package com.codepath.flicks.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rpraveen on 10/16/16.
 */
public class YoutubePlayerActivity extends YouTubeBaseActivity {

  @BindView(R.id.player) YouTubePlayerView videoPlayer;
  @Nullable @BindView(R.id.movieTitle) TextView tvMovieTitle;
  @Nullable @BindView(R.id.movieRating) RatingBar rbMovieRating;
  @Nullable @BindView(R.id.releaseDate) TextView tvReleaseDate;
  @Nullable @BindView(R.id.movieSynopsis) TextView tvSynopsis;
  @Nullable @BindView(R.id.movieBackDrop) ImageView ivMovieImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_youtube_player);
    ButterKnife.bind(this);
    videoPlayer = (YouTubePlayerView) findViewById(R.id.player);

    Movie movie = (Movie )Parcels.unwrap(getIntent().getParcelableExtra("movie"));
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
      tvMovieTitle.setText(movie.getTitle());
      rbMovieRating.setRating((float) movie.getRating());
      tvReleaseDate.setText(movie.getReleaseDate());
      tvSynopsis.setText(movie.getOverview());
      Picasso.with(this)
      .load(movie.getPosterImage())
      .transform(new RoundedCornersTransformation(10, 10))
      .placeholder(R.drawable.placeholder)
      .error(R.drawable.placeholder)
      .into(ivMovieImage);
    }
    fetchMovieTrailer(movie);
  }

  private void loadYoutubePlayer(final String videoKey) {
    videoPlayer.initialize("AIzaSyDxLGLcKTG03XO0caeVkYDSBpSSLjoBdsw",
    new YouTubePlayer.OnInitializedListener() {
      @Override
      public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                          YouTubePlayer youTubePlayer, boolean b) {

        // do any work here to cue video, play video, etc.
        youTubePlayer.loadVideo(videoKey);
      }
      @Override
      public void onInitializationFailure(YouTubePlayer.Provider provider,
                                          YouTubeInitializationResult youTubeInitializationResult) {

      }
    });
  }

  public void fetchMovieTrailer(Movie movie) {
    AsyncHttpClient httpClient = new AsyncHttpClient();
    httpClient.get(
    movie.getVideoAPIURL(),
    new JsonHttpResponseHandler(){
      @Override
      public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
          JSONArray videoResults = response.getJSONArray("results");
          String videoKey = "";
          for (int i = 0; i < videoResults.length(); i++) {
            JSONObject result = videoResults.getJSONObject(i);
            if (result.getString("site").equals("YouTube")) {
              videoKey = result.getString("key");
            }
          }
          loadYoutubePlayer(videoKey);
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    }
    );
  }
}
