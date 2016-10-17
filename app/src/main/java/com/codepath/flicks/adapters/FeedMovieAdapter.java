package com.codepath.flicks.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.R;
import com.codepath.flicks.activities.YoutubePlayerActivity;
import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by rpraveen on 10/15/16.
 */
public class FeedMovieAdapter extends ArrayAdapter<Movie> {

  static class ViewHolder {
    @BindView(R.id.movieTitle) TextView tvMovieTitle;
    @BindView(R.id.movieRating) RatingBar rbMovieRating;
    @BindView(R.id.releaseDate) TextView tvReleaseDate;
    @Nullable @BindView(R.id.movieSynopsis) TextView tvSynopsis;
    @BindView(R.id.movieBackDrop) ImageView ivMovieImage;
    public ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }

  public FeedMovieAdapter(Context context, List<Movie> movies) {
    super(context, R.layout.item_movie_feed, movies);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    final Movie movie = getItem(position);

    ViewHolder viewHolder;
    if (convertView == null) {
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.item_movie_feed, parent, false);
      viewHolder = new ViewHolder(convertView);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.ivMovieImage.setImageResource(0);
    String imageURL = movie.getBackDropImage();

    if (
    getContext().getResources().getConfiguration().orientation ==
    Configuration.ORIENTATION_LANDSCAPE
    ) {
      imageURL = movie.getPosterImage();
      viewHolder.tvSynopsis.setText(movie.getOverview());
      Picasso.with(getContext())
        .load(imageURL)
        .transform(new RoundedCornersTransformation(10, 10))
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(viewHolder.ivMovieImage);
    } else {
      Picasso.with(getContext())
        .load(imageURL)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(viewHolder.ivMovieImage);
    }

    viewHolder.ivMovieImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(getContext(), YoutubePlayerActivity.class);
        intent.putExtra("movie", Parcels.wrap(movie));
        getContext().startActivity(intent);
      }
    });

    viewHolder.tvMovieTitle.setText(movie.getTitle());
    viewHolder.tvReleaseDate.setText(movie.getReleaseDate());
    viewHolder.rbMovieRating.setRating((float)movie.getRating());
    return convertView;
  }
}
