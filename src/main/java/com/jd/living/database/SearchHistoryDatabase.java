package com.jd.living.database;


import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;

import com.jd.living.LivingApplication;
import com.jd.living.Search;
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
            latestSearchHistory.setTimestamp(searchHistory.getTimestamp());
            getRepository().updateSearchHistory(latestSearchHistory);
        } else {
            latestSearchHistory = searchHistory;
            getRepository().addSearchHistory(searchHistory);
        }

        for (SearchHistoryDatabaseListener listener : listeners) {
            listener.onUpdate(getRepository().getSearchHistories());
        }
    }

    public void registerSearchHistoryDatabaseListener(SearchHistoryDatabaseListener listener) {
        listeners.add(listener);
        listener.onUpdate(getRepository().getSearchHistories());
    }

    public void clearSearchHistoryDatabase() {
        getRepository().clearSearchDatabase();

        for (SearchHistoryDatabaseListener listener : listeners) {
            listener.onUpdate(getRepository().getSearchHistories());
        }
        latestSearchHistory = null;
    }

    private SearchRepository getRepository() {
        if (repository == null) {
            repository = ((LivingApplication) context.getApplicationContext()).getSearchRepository();
        }
        return repository;
    }
}
