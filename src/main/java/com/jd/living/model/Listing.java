package com.jd.living.model;

import android.text.TextUtils;

import com.jd.living.util.StringUtil;

public class Listing {

    protected int booliId;
    protected double listPrice;
    protected String published;
    protected Location location;
    protected Source source;
    protected String objectType;
    protected double rooms;
    protected double floor;
    protected double rent;
    protected double livingArea;
    protected double plotArea;
    protected int constructionYear;
    protected String url;
    protected String soldDate;
    protected double soldPrice;

    public String getSoldDate() {
        return soldDate;
    }

    public String getSoldPrice() {
        return StringUtil.getCurrencyFormattedString(soldPrice);
    }

    public boolean isSold() {
        return !TextUtils.isEmpty(soldDate);
    }

    protected class Source {
        protected String name;
        protected String url;
        protected String type;
    }

    public int getBooliId() {
        return booliId;
    }

    public String getListPrice() {
        return StringUtil.getCurrencyFormattedString(listPrice);
    }

    public double getPrice() {
        return isSold() ? soldPrice : listPrice;
    }

    public String getPublished() {
        return published.substring(0, 10);
    }

    public String getAddress() {
        return location.getAddress();
    }

    public String getRent() {
        return StringUtil.getCurrencyFormattedString(rent);
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public String getArea() {
        return location.getArea();
    }

    public String getSource() {
        return source.name;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getRoomsAsString() {
        String result = String.valueOf(rooms);
        if (rooms % 1 == 0) {
            result = String.valueOf((int) rooms);
        }
        return result;
    }

    public double getRooms() {
        return rooms;
    }

    public long getFloor() {
        return Math.round(floor);
    }

    public long getLivingArea() {
        return Math.round(livingArea);
    }

    public String getPlotArea() {
        return StringUtil.getNumberFormattedString(plotArea);
    }

    public int getConstructionYear() {
        return constructionYear;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return "http://api.bcdn.se/cache/primary_"+ getBooliId()+ "_140x94.jpg";
    }

    @Override
    public String toString() {
        return getAddress();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Listing) {
            Listing listing = (Listing) o;
            return listing.getBooliId() == this.getBooliId();
        }
        return super.equals(o);
    }
}
