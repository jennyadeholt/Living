package com.jd.living.model;

import java.util.List;

import android.text.TextUtils;

/**
 * Created by jennynilsson on 2014-06-03.
 */
public class Location {

    private List<String> namedAreas;
    private Region region;
    private Address address;
    private Position position;

    public String getAddress() {
        return address.getAddress();
    }

    public String getArea() {
        StringBuffer buffer = new StringBuffer();
        for (String area : namedAreas) {
            buffer.append(area);
            buffer.append(", ");
        }
        String area = buffer.toString();
        area = area.substring(0,  area.lastIndexOf(","));

        return area;
    }

    public double getLatitude() {
        return position.getLatitude();
    }

    public double getLongitude() {
        return position.getLongitude();
    }

    public String getRegion() {
        return region.getRegion();
    }

    private class Region {
        private String municipalityName;
        private String contryName;

        public String getRegion() {
            String address = municipalityName;
            if (!TextUtils.isEmpty(contryName)){
                address += ", " + contryName;
            }
            return address;
        }
    }

    private class Address {
        private String city;
        private String streetAddress;

        protected String getAddress() {
            String address = streetAddress;
            if (!TextUtils.isEmpty(city)){
                address += ", " + city;
            }
            return streetAddress;
        }
    }

    private class Position {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

}
