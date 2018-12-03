package com.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.myapplication.adapters.MediaAdapterInner;
import com.myapplication.callbacks.MediaSelectedCallBack;
import com.myapplication.database.models.MediaList;
import com.myapplication.database.models.MediaPlayBack;
import com.myapplication.presenter.DetailsPresenterImpl;
import com.myapplication.view.DetailsContract;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity implements DetailsContract.DetailsView, MediaSelectedCallBack, Player.EventListener {

    DetailsPresenterImpl presenter;
    TextView tvTitle, tvDescription;
    Button btnPlay;
    PlayerView playerView;
    SimpleExoPlayer player;
    RecyclerView recyclerView;
    MediaAdapterInner mAdapter;
    List<MediaList> mediaLists = new ArrayList<>();
    int currentWindow = 0;
    long playbackPosition = 0;
    boolean playWhenReady = true;
    boolean hasResetPosition = false;
    int currentId;

    @Override
    public void doInitialize() {
        super.doInitialize();

        setContentView(R.layout.activity_details);

        presenter = new DetailsPresenterImpl(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // UI references.
        btnPlay = findViewById(R.id.btn_play);
        tvTitle = findViewById(R.id.tv_title);
        tvDescription = findViewById(R.id.tv_desc);
        playerView = findViewById(R.id.video_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MediaAdapterInner(mediaLists, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (!presenter.doDBDataStatus(DetailsActivity.this))
            presenter.doFetchMediaLists(DetailsActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentId = getIntent().getIntExtra("id", 0);
        presenter.doFetchDetails(currentId);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        currentId = intent.getIntExtra("id", 0);
        presenter.doFetchDetails(currentId);
    }

    @Override
    public void onDetailsFetched(final MediaList mediaList) {
        tvTitle.setText(mediaList.getTitle());
        tvDescription.setText(mediaList.getDescription());
        presenter.doFetchOtherMediasFromDB(mediaList);
        if (btnPlay.getVisibility() == View.GONE)
            presenter.doFetchPlayBack(mediaList);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                presenter.doFetchPlayBack(mediaList);
            }
        });
    }

    @Override
    public void onResponseSuccess(List<MediaList> response) {
        mediaLists.clear();
        mediaLists.addAll(response);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initializePlayer(MediaList mediaList, MediaPlayBack mediaPlayBack) {
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        playerView.setPlayer(player);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(mediaPlayBack.getCurrentWindow(), mediaPlayBack.getPlaybackPosition());

        Uri uri = Uri.parse(mediaList.getUrl());
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, mediaPlayBack.getPlaybackPosition() == 0, false);
        player.addListener(this);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exo-test")).
                createMediaSource(uri);
    }

    @Override
    public void onMediaSelected(Object media) {
        releasePlayer();
        playbackPosition = 0;
        currentWindow = 0;
        playWhenReady = true;
        currentId = ((MediaList) media).getId();
        presenter.doFetchDetails(currentId);
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

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            hasResetPosition = true;
            player.release();
            player = null;
            presenter.doUpdatePlayBack(currentId, currentWindow, playbackPosition);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                !playWhenReady) {
            playerView.setKeepScreenOn(false);
        } else { // STATE_IDLE, STATE_ENDED
            // This prevents the screen from getting dim/lock
            playerView.setKeepScreenOn(true);
        }

        switch (playbackState) {
            case Player.STATE_READY:
                break;

            case Player.STATE_IDLE:
                releasePlayer();
                break;

            case Player.STATE_ENDED:
                presenter.doRemovePlayBack(currentId);
                currentId = mediaLists.get(currentId - 1 % mediaLists.size()).getId();
                presenter.doFetchDetails(currentId);
                break;
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    protected void onDestroy() {
        releasePlayer();
        presenter.onDestroy();
        super.onDestroy();
    }
}
