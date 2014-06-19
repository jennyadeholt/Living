package com.jd.living.model;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by jennynilsson on 2014-04-21.
 */

public class Result {

    public int totalCount = 0;
    public int count = 0;
    public List<Listing> listings = new ArrayList<Listing>();

    public Result(){}

    public Result(int totalCount, int count, List<Listing> listings){
        super();
        this.totalCount = totalCount;
        this.count = count;
        this.listings = listings;
    }
}
