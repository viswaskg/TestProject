package com.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;
import com.myapplication.database.DatabaseHelper;
import com.myapplication.network.RetrofitClient;
import com.myapplication.network.RetrofitInterface;
import com.myapplication.view.BaseContract;

import java.io.IOException;

/**
 * A login screen that offers login via email/password.
 */
public class BaseActivity extends AppCompatActivity implements BaseContract.MainView {

    Dialog progressLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInitialize();
    }

    public void doInitialize() {
        progressLoader = new Dialog(this,
                android.R.style.Theme_Translucent);
        progressLoader.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressLoader.getWindow().setGravity(Gravity.TOP);
        progressLoader.setContentView(R.layout.dialog_loader);
    }

    @Override
    public void showProgress() {
        progressLoader.show();
    }
    @Override
    public void hideProgress() {
        progressLoader.dismiss();
    }

    @Override
    public void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getResources().getString(R.string.ok),
                null);

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void showAlertWithExit(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setPositiveButton(getResources().getString(R.string.ok),
                null);

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void onResponseFailure(Throwable throwable) {
        if (throwable instanceof IOException) {
            showAlert(getResources().getString(R.string.error_io));
        } else if (throwable instanceof NullPointerException){
            showAlert(getResources().getString(R.string.error_server));
        } else
            showAlert(getResources().getString(R.string.error_app));
    }
}
