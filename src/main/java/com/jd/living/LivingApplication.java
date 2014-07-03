package com.jd.living;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;
import org.androidannotations.annotations.OrmLiteDao;

import android.app.Application;

import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.SearchDatabase;
import com.jd.living.database.ormlite.OrmLiteDatabaseHelper;
import com.jd.living.model.ormlite.SearchHistory;


@EApplication
public class LivingApplication extends Application {

    @Bean
    DatabaseHelper database;

    public DatabaseHelper getDatabase() {
        return database;
    }
}
