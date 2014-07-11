package com.jd.living.model.ormlite;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.jd.living.Search;

@DatabaseTable(tableName = "searchHistory")
public class SearchHistory implements Comparable<SearchHistory> {

    @DatabaseField(generatedId=true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String location;

    @DatabaseField(canBeNull = false)
    private String types;

    @DatabaseField(canBeNull = false)
    private String minRooms;

    @DatabaseField(canBeNull = false)
    private String maxRooms;

    @DatabaseField(canBeNull = false)
    private String minAmount;

    @DatabaseField(canBeNull = false)
    private String maxAmount;

    @DatabaseField(canBeNull = false)
    private String maxRent;

    @DatabaseField(canBeNull = false)
    private String production;

    @DatabaseField(canBeNull = false)
    private boolean isSold;

    @DatabaseField(canBeNull = false)
    private long timestamp;

    @SuppressWarnings("unused")
    private SearchHistory() {
    }

    public SearchHistory(Search search) {
        this.location = search.getLocation();
        this.types = search.getTypes();
        this.minRooms = search.getMinRooms();
        this.maxRooms = search.getMaxRooms(false);
        this.minAmount = search.getMinAmount(false);
        this.maxAmount = search.getMaxAmount(false);
        this.production = search.getProduction();
        this.isSold = search.fetchSoldObjects();
        this.maxRent = search.getMaxRent(false);
        this.timestamp = search.getTimestamp();
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getTypes() {
        return types;
    }

    public String getMinRooms() {
        return minRooms;
    }

    public String getMaxRooms() {
        return maxRooms;
    }

    public String getMinAmount() {
        return minAmount;
    }

    public String getMaxAmount() {
        return maxAmount;
    }

    public String getProduction() {
        return production;
    }

    public boolean isSold() {
        return isSold;
    }

    public long getTimestamp() { return timestamp; };

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SearchHistory) {
            SearchHistory searchHistory = (SearchHistory) o;
            return searchHistory.getLocation().equals(this.getLocation()) &&
                    searchHistory.getMinAmount().equals(this.getMinAmount()) &&
                    searchHistory.getMaxAmount().equals(this.getMaxAmount()) &&
                    searchHistory.getMaxRooms().equals(this.getMaxRooms()) &&
                    searchHistory.getMinRooms().equals(this.getMinRooms()) &&
                    searchHistory.getTypes().equals(this.getTypes()) && //TODO fixes if types are in different order
                    searchHistory.isSold() == this.isSold() &&
                    searchHistory.getProduction().equals(this.getProduction());
        }
        return super.equals(o);
    }

    @Override
    public int compareTo(SearchHistory another) {
        return (timestamp > another.getTimestamp()) ? -1 : 1;
    }
}
