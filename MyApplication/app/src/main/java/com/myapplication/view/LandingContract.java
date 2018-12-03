package com.myapplication.view;

import android.content.Intent;
import com.myapplication.LandingActivity;
import com.myapplication.LoginActivity;
import com.myapplication.database.models.MediaList;

import java.util.List;

public interface LandingContract {

    interface LandingView {
        void showProgress();

        void hideProgress();

        void showAlert(String msg);

        void showAlertWithExit(String msg);

        void showToast(String msg);

        void onResponseFailure(Throwable throwable);

        void onResponseSuccess(List<MediaList> mediaLists);
    }

    interface Intractor {
        boolean doDBDataStatus(LandingActivity landingActivity);

        void doFetchDataFromDB();

        void doFetchMediaLists(LandingActivity activity);

        void onDestroy();
    }
}
