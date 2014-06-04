package com.jd.living.model;


import java.util.List;


/**
 * Created by jennynilsson on 2014-04-21.
 */

public class Result {

    public int totalCount;
    public int count;
    public List<Listing> listings;

    public Result(){}

    public Result(int totalCount, int count, List<Listing> listings){
        super();
        this.totalCount = totalCount;
        this.count = count;
        this.listings = listings;
    }
}
