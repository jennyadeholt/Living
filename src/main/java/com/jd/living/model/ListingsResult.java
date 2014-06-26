package com.jd.living.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jennynilsson on 2014-06-25.
 */
public class ListingsResult extends Result {

    private List<Listing> listings = new ArrayList<Listing>();

    public ListingsResult(int totalCount, int count, List<Listing> listings){
        super();
        this.totalCount = totalCount;
        this.count = count;
        this.listings = listings;
    }

    public List<Listing> getResult() {
        return listings;
    }
}
