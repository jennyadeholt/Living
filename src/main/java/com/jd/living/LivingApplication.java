package com.jd.living;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import android.app.Application;

import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchDatabase;


@EApplication
public class LivingApplication extends Application {

    @Bean
    DatabaseHelper database;


    public DatabaseHelper getDatabase() {
        return database;
    }
}
