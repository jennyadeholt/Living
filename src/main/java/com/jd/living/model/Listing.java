package com.jd.living.model;

public class Listing {

    private int booliId;

    private double listPrice;

    private String published;

    private Location location;

    private Source source;

    private String objectType;

    private double rooms;

    private double floor;

    private double rent;

    private double livingArea;

    private double plotArea;

    private int constructionYear;

    private String url;

    private class Source {
        protected String name;
        protected String url;
        protected String type;
    }

    public int getBooliId() {
        return booliId;
    }

    public String getListPrice() {
        return getFormattedNumber(listPrice);
    }

    public String getPublished() {
        return published;
    }

    public String getAddress() {
        return location.getAddress();
    }

    public String getRent() {
        return getFormattedNumber(rent);
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

    public Source getSource() {
        return source;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getRooms() {
        String result = String.valueOf(rooms);
        if (rooms % 1 == 0) {
            result = String.valueOf((int) rooms);
        }
        return result;
    }

    public long getFloor() {
        return Math.round(floor);
    }

    public long getLivingArea() {
        return Math.round(livingArea);
    }

    public double getPlotArea() {
        return plotArea % 1 == 0 ? Math.abs(plotArea) : plotArea;
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

    private String getFormattedNumber(double number) {
        String r = String.valueOf((int) number);

        if (r.length() > 6) {
            r = r.substring(0, r.length() - 6) + " " + r.substring(r.length() - 6);
        }

        if (r.length() > 3) {
            r = r.substring(0, r.length() - 3) + " " + r.substring(r.length() - 3);
        }

        return r;
    }
}
