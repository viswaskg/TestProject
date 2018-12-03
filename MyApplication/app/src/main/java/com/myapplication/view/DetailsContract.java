package com.myapplication.view;

import com.myapplication.DetailsActivity;
import com.myapplication.database.models.MediaList;
import com.myapplication.database.models.MediaPlayBack;

import java.util.List;

public interface DetailsContract {

    interface DetailsView {
        void showProgress();

        void hideProgress();

        void showAlert(String msg);

        void showAlertWithExit(String msg);

        void showToast(String msg);

        void initializePlayer(MediaList mediaList, MediaPlayBack mediaPlayBack);

        void onDetailsFetched(MediaList mediaList);

        void onResponseFailure(Throwable throwable);

        void onResponseSuccess(List<MediaList> mediaLists);
    }

    interface Intractor {

        void doFetchDetails(int id);

        void doFetchOtherMediasFromDB(MediaList mediaList);

        boolean doDBDataStatus(DetailsActivity landingActivity);

        void doFetchDataFromDB();

        void doFetchMediaLists(DetailsActivity activity);

        void doFetchPlayBack(MediaList mediaList);

        void doUpdatePlayBack(int id, int window, long position);

        void doRemovePlayBack(int id);

        void onDestroy();
    }
}
