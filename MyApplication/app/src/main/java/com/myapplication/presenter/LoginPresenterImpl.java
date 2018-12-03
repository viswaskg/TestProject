package com.myapplication.presenter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.myapplication.LoginActivity;
import com.myapplication.R;
import com.myapplication.database.DatabaseHelper;
import com.myapplication.view.LoginContract;

import static android.support.constraint.Constraints.TAG;

public class LoginPresenterImpl implements LoginContract.Intractor {

    private static final int RC_SIGN_IN_G = 2018;
    private LoginContract.LoginView mLoginView;
    // Google client to communicate with Google
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseHelper db;

    public LoginPresenterImpl(LoginContract.LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    public void checkLoginStatus(LoginActivity loginView) {
        db = new DatabaseHelper(loginView);
        if (db.getLoginStatus()) {
            loginView.onLoginSuccess();
        }
    }

    @Override
    public void createGoogleClient(LoginActivity loginView) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(loginView.getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
//        mLoginView.specifyGoogleSignIn(gso);
        mGoogleSignInClient = GoogleSignIn.getClient(loginView, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        FirebaseApp.initializeApp(loginView);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(LoginActivity loginView) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        loginView.startActivityForResult(signInIntent, RC_SIGN_IN_G);
    }

    @Override
    public void onActivityResult(LoginActivity loginView, int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_G) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(loginView, account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(loginView.getLocalClassName(), "Google sign in failed", e);
                // [START_EXCLUDE]
//                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(final LoginActivity loginView, final GoogleSignInAccount acct) {
        loginView.showProgress();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(loginView.getLocalClassName(), "signInWithCredential:success");
                            mLoginView.showToast(loginView.getResources().getString(R.string.success_login));
                            db.doLogin(acct.getId(), acct.getDisplayName());
                            mLoginView.onLoginSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            mLoginView.showToast(loginView.getResources().getString(R.string.error_login));
                        }

                        // [START_EXCLUDE]
                        loginView.hideProgress();
                        // [END_EXCLUDE]
                    }
                });
    }

    @Override
    public void onDestroy() {
        mLoginView = null;
    }
}