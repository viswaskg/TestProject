package com.myapplication;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.myapplication.view.LoginContract;
import com.myapplication.presenter.LoginPresenterImpl;


public class LoginActivity extends BaseActivity implements LoginContract.LoginView {

    private LoginPresenterImpl loginPresenter;
    // UI references.


    @Override
    public void doInitialize() {
        super.doInitialize();

        setContentView(R.layout.activity_login);

        loginPresenter = new LoginPresenterImpl(LoginActivity.this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loginPresenter.checkLoginStatus(this);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                loginPresenter.createGoogleClient(LoginActivity.this);
                loginPresenter.signIn(LoginActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loginPresenter.onActivityResult(LoginActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, LandingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginFailure(String message) {

    }

    @Override
    protected void onDestroy() {
        loginPresenter.onDestroy();
        super.onDestroy();
    }
}
