package com.myapplication.view;

public interface BaseContract {

    interface MainView {
        void showProgress();
        void hideProgress();
        void showAlert(String msg);
        void showAlertWithExit(String msg);
        void showToast(String msg);
        void onResponseFailure(Throwable throwable);
    }
}
