package com.jd.living.activity;

import java.util.List;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.jd.living.R;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchDatabase;
import com.jd.living.model.Listing;
import com.jd.living.model.Result;


@EActivity
public class SplashScreenActivity extends Activity implements DatabaseHelper.DatabaseListener {

    @Bean
    DatabaseHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        database.addDatabaseListener(this);
        database.launchSearch();
    }

    @UiThread
    @Override
    public void onUpdate(List<Listing> result) {
        database.removeDatabaseListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity_.class));
                finish();
            }
        }, 200);
    }

    @Override
    public void onSearchStarted() {

    }

    @Override
    public void onDetailsRequested(int booliId) {

    }
}
