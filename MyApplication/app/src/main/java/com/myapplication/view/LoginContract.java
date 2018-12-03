package com.myapplication.view;

import android.content.Intent;
import com.myapplication.LoginActivity;

public interface LoginContract {

    interface LoginView {
        void showProgress();

        void hideProgress();

        void showAlert(String msg);

        void showAlertWithExit(String msg);

        void showToast(String msg);

        void onResponseFailure(Throwable throwable);

        void onLoginSuccess();

        void onLoginFailure(String message);
    }

    interface Intractor {
        void checkLoginStatus(LoginActivity loginView);

        void createGoogleClient(LoginActivity loginView);

        void signIn(LoginActivity loginView);

        void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data);

        void onDestroy();
    }
}
