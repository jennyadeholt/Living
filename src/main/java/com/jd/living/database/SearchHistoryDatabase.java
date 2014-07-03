package com.jd.living.database;


import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jd.living.Search;
import com.jd.living.database.ormlite.OrmLiteDatabaseHelper;
import com.jd.living.database.ormlite.SearchRepository;
import com.jd.living.model.ormlite.SearchHistory;

@EBean(scope = EBean.Scope.Singleton)
public class SearchHistoryDatabase implements SearchDatabase.SearchHistoryListener {

    @RootContext
    Context context;

    @Bean
    DatabaseHelper databaseHelper;

    public interface SearchHistoryDatabaseListener {
        public void onUpdate(List<SearchHistory> searchHistories);
    }

    private SearchHistory latestSearchHistory;
    private SearchRepository repository;
    private OrmLiteDatabaseHelper ormLiteDatabaseHelper;

    private List<SearchHistoryDatabaseListener> listeners = new ArrayList<SearchHistoryDatabaseListener>();

    @AfterInject
    protected void init() {
        latestSearchHistory = getRepository().getLatestSearchHistory();
        databaseHelper.getSearchDatabase().setSearchHistoryListener(this);
    }

    @Override
    public void onNewSearch(Search search) {

        SearchHistory searchHistory = new SearchHistory(search);

        if (searchHistory.equals(latestSearchHistory)) {
            Log.d("history", "equals");
            latestSearchHistory.setTimestamp(searchHistory.getTimestamp());
        } else {
            Log.d("history", "!equals");
            latestSearchHistory = searchHistory;
        }

        getRepository().addSearchHistory(latestSearchHistory);

        for (SearchHistoryDatabaseListener listener : listeners) {
            listener.onUpdate(getRepository().getSearchHistories());
        }
    }

    public void registerSearchHistoryDatabaseListener(SearchHistoryDatabaseListener listener) {
        listeners.add(listener);
        listener.onUpdate(getRepository().getSearchHistories());
    }

    private OrmLiteDatabaseHelper getHelper() {
        if (ormLiteDatabaseHelper == null) {
            ormLiteDatabaseHelper = OpenHelperManager.getHelper(context, OrmLiteDatabaseHelper.class);
        }
        return ormLiteDatabaseHelper;
    }

    private SearchRepository getRepository() {
        if (repository == null) {
            repository = new SearchRepository(getHelper());
        }
        return repository;
    }
}
