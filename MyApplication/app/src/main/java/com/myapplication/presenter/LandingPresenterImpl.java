package com.myapplication.presenter;

import com.myapplication.LandingActivity;
import com.myapplication.R;
import com.myapplication.database.DatabaseHelper;
import com.myapplication.database.models.MediaList;
import com.myapplication.network.RetrofitClient;
import com.myapplication.network.RetrofitInterface;
import com.myapplication.view.LandingContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class LandingPresenterImpl implements LandingContract.Intractor {

    private LandingContract.LandingView landingView;
    private DatabaseHelper db;

    public LandingPresenterImpl(LandingContract.LandingView landingView) {
        this.landingView = landingView;
    }

    @Override
    public boolean doDBDataStatus(LandingActivity activity) {
        db = new DatabaseHelper(activity);
        return db.getMediaCount() > 0;
    }

    @Override
    public void doFetchDataFromDB() {
        List<MediaList> mediaLists = new ArrayList<>();
        mediaLists.clear();
        mediaLists.addAll(db.getAllMedia());
        landingView.onResponseSuccess(mediaLists);
    }

    @Override
    public void doFetchMediaLists(final LandingActivity activity) {
        landingView.showProgress();
        Call<List<MediaList>> call = RetrofitClient.getClient().create(RetrofitInterface.class).getMediaLists();
        call.enqueue(new Callback<List<MediaList>>() {
            @Override
            public void onResponse(Call<List<MediaList>> call, Response<List<MediaList>> response) {
                landingView.hideProgress();
                if (response.code() == 200) {
                    db.addAllMedia(response.body());
                    landingView.onResponseSuccess(response.body());
                } else
                    landingView.showAlert(activity.getResources().getString(R.string.error_common));
            }

            @Override
            public void onFailure(Call<List<MediaList>> call, Throwable t) {
                // Log error here since request failed
                landingView.hideProgress();
                landingView.onResponseFailure(t);
            }
        });
    }

    @Override
    public void onDestroy() {
        landingView = null;
    }
}
