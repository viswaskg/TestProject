package com.myapplication;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.myapplication.adapters.MediaAdapter;
import com.myapplication.callbacks.MediaSelectedCallBack;
import com.myapplication.database.models.MediaList;
import com.myapplication.view.LandingContract;
import com.myapplication.presenter.LandingPresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class LandingActivity extends BaseActivity implements MediaSelectedCallBack, LandingContract.LandingView {

    RecyclerView recyclerView;
    MediaAdapter mAdapter;
    List<MediaList> mediaLists = new ArrayList<>();

    LandingPresenterImpl presenter;

    @Override
    public void doInitialize() {
        super.doInitialize();

        setContentView(R.layout.activity_landing);

        presenter = new LandingPresenterImpl(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // UI references.
        TextView mUserName = (TextView) findViewById(R.id.tv_user);
        mUserName.setText("Welcome " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new MediaAdapter(mediaLists, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        if (presenter.doDBDataStatus(this)) {
            presenter.doFetchDataFromDB();
        } else presenter.doFetchMediaLists(this);
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
    public void onMediaSelected(Object media) {
        Intent details = new Intent(this, DetailsActivity.class);
        details.putExtra("id", ((MediaList) media).getId());
        startActivity(details);
    }

    @Override
    public void onResponseSuccess(List<MediaList> response) {
        mediaLists.clear();
        mediaLists.addAll(response);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
