package com.jd.living.database.ormlite;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.table.TableUtils;
import com.jd.living.model.ormlite.SearchHistory;


/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class OrmLiteDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "searches.db";

    private static final int DATABASE_VERSION = 2;

    private RuntimeExceptionDao<SearchHistory, Integer> authorDao = null;


    public OrmLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SearchHistory.class);
        } catch (SQLException e) {
            Log.e(OrmLiteDatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, com.j256.ormlite.support.ConnectionSource connectionSource, int i, int i2) {
        try {
            TableUtils.dropTable(connectionSource, SearchHistory.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(OrmLiteDatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<SearchHistory, Integer> getSearchHistoryDao() {
        if (authorDao == null) {
            authorDao = getRuntimeExceptionDao(SearchHistory.class);
        }
        return authorDao;
    }

    @Override
    public void close() {
        super.close();
        authorDao = null;
    }
}