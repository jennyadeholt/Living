package com.jd.living.database.ormlite;


import java.util.List;

import com.jd.living.model.ormlite.Favorite;

public class FavoriteRepository {

    private final OrmLiteDatabaseHelper helper;

    public FavoriteRepository(OrmLiteDatabaseHelper helper) {
        this.helper = helper;
    }

    public List<Favorite> getFavorites() {
        return helper.getFavoriteDatabase().queryForAll();
    }

    public boolean isFavorite(int id) {
        return helper.getFavoriteDatabase().queryForId(id) != null;
    }

    public void addFavorite(Favorite favorite) {
        helper.getFavoriteDatabase().createIfNotExists(favorite);
    }

    public void updateFavorite(Favorite searchHistory) {
        helper.getFavoriteDatabase().update(searchHistory);
    }

    public void deleteFavorite(int id) {
        helper.getFavoriteDatabase().deleteById(id);
    }

    public void clearFavoriteDatabase() {
        helper.getFavoriteDatabase().delete(getFavorites());
    }
}