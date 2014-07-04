package com.jd.living;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EApplication;

import android.app.Application;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jd.living.database.DatabaseHelper;
import com.jd.living.database.ormlite.FavoriteRepository;
import com.jd.living.database.ormlite.OrmLiteDatabaseHelper;
import com.jd.living.database.ormlite.SearchRepository;


@EApplication
public class LivingApplication extends Application {

    @Bean
    DatabaseHelper database;

    private OrmLiteDatabaseHelper ormLiteDatabaseHelper;
    private SearchRepository searchRepository;
    private FavoriteRepository favoriteRepository;

    public OrmLiteDatabaseHelper getOrmLiteDatabaseHelper() {
        if (ormLiteDatabaseHelper == null) {
            ormLiteDatabaseHelper = OpenHelperManager.getHelper(getApplicationContext(), OrmLiteDatabaseHelper.class);
        }
        return ormLiteDatabaseHelper;
    }


    public SearchRepository getSearchRepository() {
        if (searchRepository == null) {
            searchRepository = new SearchRepository(getOrmLiteDatabaseHelper());
        }
        return searchRepository;
    }

    public FavoriteRepository getFavoriteRepository() {
        if (favoriteRepository == null) {
            favoriteRepository = new FavoriteRepository(getOrmLiteDatabaseHelper());
        }
        return favoriteRepository;
    }

    public DatabaseHelper getDatabase() {
        return database;
    }
}
