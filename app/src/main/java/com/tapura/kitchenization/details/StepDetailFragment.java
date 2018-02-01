package com.tapura.kitchenization.details;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.tapura.kitchenization.R;
import com.tapura.kitchenization.model.StepsItem;

import org.parceler.Parcels;

/**
 * A fragment representing a single Step detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment {

    private static final String SIS_PLAYER_POSITION = "current_position";
    private static final String SIS_PLAYER_WINDOW = "current_window";
    private static final String SIS_PLAYER_PLAYING = "is_playing";
    public static final String ARG_ITEM = "item";

    private StepsItem mItem;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mIsPlaying;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Parcels.unwrap(getArguments().getParcelable(ARG_ITEM));

            /*Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }*/
        }

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(SIS_PLAYER_POSITION)) {
                mPlaybackPosition = savedInstanceState.getLong(SIS_PLAYER_POSITION);
            }

            if (savedInstanceState.containsKey(SIS_PLAYER_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(SIS_PLAYER_WINDOW);
            }

            if (savedInstanceState.containsKey(SIS_PLAYER_PLAYING)) {
                mIsPlaying = savedInstanceState.getBoolean(SIS_PLAYER_PLAYING);
            }
        }

    }

    private void initializePlayer() {
        if (mPlayer == null && mPlayerView != null) {
            // Create the player
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mIsPlaying);

            mPlayer.prepare(buildMediaSource(Uri.parse(mItem.getVideoURL())), mPlaybackPosition > 0, false);

            mPlayerView.setPlayer(mPlayer);

        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mIsPlaying = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_detail, container, false);


        if (mItem != null) {
            // Show step description
            ((TextView) rootView.findViewById(R.id.step_detail)).setText(mItem.getDescription());

            //Prepare player
            SimpleExoPlayerView playerView = rootView.findViewById(R.id.player);
            ImageView imageView = rootView.findViewById(R.id.thumbnail);
            if (!TextUtils.isEmpty(mItem.getVideoURL())) {
                mPlayerView = playerView;
                playerView.setVisibility(View.VISIBLE);
                initializePlayer();
            }
            if (!TextUtils.isEmpty(mItem.getThumbnailURL())) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(getContext())
                        .load(Uri.parse(mItem.getThumbnailURL()))
                        .into(imageView);
            }

            if (playerView.getVisibility() == View.VISIBLE
                    || imageView.getVisibility() == View.VISIBLE) {
                rootView.findViewById(R.id.media_frame).setVisibility(View.VISIBLE);
            }
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPlayer != null) {
            outState.putLong(SIS_PLAYER_POSITION, mPlayer.getCurrentPosition());
            outState.putInt(SIS_PLAYER_WINDOW, mPlayer.getCurrentWindowIndex());
            outState.putBoolean(SIS_PLAYER_PLAYING, mPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(outState);
    }

    private MediaSource buildMediaSource(Uri uri) {
        String ua = Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(ua)).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

}
