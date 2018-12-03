package com.myapplication.presenter;

import com.myapplication.DetailsActivity;
import com.myapplication.LandingActivity;
import com.myapplication.R;
import com.myapplication.database.DatabaseHelper;
import com.myapplication.database.models.MediaList;
import com.myapplication.network.RetrofitClient;
import com.myapplication.network.RetrofitInterface;
import com.myapplication.view.DetailsContract;
import com.myapplication.view.LandingContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class DetailsPresenterImpl implements DetailsContract.Intractor {

    private DetailsContract.DetailsView detailsView;
    private DatabaseHelper db;

    public DetailsPresenterImpl(DetailsContract.DetailsView detailsView) {
        this.detailsView = detailsView;
    }

    @Override
    public boolean doDBDataStatus(DetailsActivity activity) {
        db = new DatabaseHelper(activity);
        return db.getMediaCount() > 0;
    }

    @Override
    public void doFetchDetails(int id) {
        detailsView.onDetailsFetched(db.geMediabyId(id));
    }

    @Override
    public void doFetchOtherMediasFromDB(MediaList mediaList) {
        List<MediaList> mediaLists = new ArrayList<>();
        mediaLists.clear();
        mediaLists.addAll(db.getAllOtherMedia(mediaList));
        detailsView.onResponseSuccess(mediaLists);
    }

    @Override
    public void doFetchDataFromDB() {
        List<MediaList> mediaLists = new ArrayList<>();
        mediaLists.clear();
        mediaLists.addAll(db.getAllMedia());
        detailsView.onResponseSuccess(mediaLists);
    }

    @Override
    public void doUpdatePlayBack(int id, int window, long position) {
        db.doUpdatePlayBack(id, window, position);
    }

    @Override
    public void doRemovePlayBack(int id) {
        db.removePlayBack(id);
    }

    @Override
    public void doFetchMediaLists(final DetailsActivity activity) {
        detailsView.showProgress();
        Call<List<MediaList>> call = RetrofitClient.getClient().create(RetrofitInterface.class).getMediaLists();
        call.enqueue(new Callback<List<MediaList>>() {
            @Override
            public void onResponse(Call<List<MediaList>> call, Response<List<MediaList>> response) {
                detailsView.hideProgress();
                if (response.code() == 200) {
                    db.addAllMedia(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MediaList>> call, Throwable t) {
                // Log error here since request failed
                detailsView.hideProgress();
                detailsView.onResponseFailure(t);
            }
        });
    }

    @Override
    public void doFetchPlayBack(MediaList mediaList) {
        detailsView.initializePlayer(mediaList, db.getMediaDetails(mediaList.getId()));
    }

    @Override
    public void onDestroy() {
        detailsView = null;
    }
}
