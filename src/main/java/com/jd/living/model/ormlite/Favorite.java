package com.jd.living.model.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "favorites")
public class Favorite {

    @DatabaseField(id = true)
    private int id;

    @SuppressWarnings("unused")
    private Favorite() {}

    public Favorite(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
