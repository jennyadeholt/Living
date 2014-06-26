package com.jd.living;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import android.app.Application;

import com.jd.living.server.ListingsDatabase;


@EApplication
public class LivingApplication extends Application {

    @Bean
    ListingsDatabase database;


    public ListingsDatabase getDatabase() {
        return database;
    }
}
