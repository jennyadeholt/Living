package com.jd.living.model;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by jennynilsson on 2014-04-21.
 */

public class Result {

    public int totalCount = 0;
    public int count = 0;

    public Result() {}

    public List<Listing> getListings() {
        return new ArrayList<Listing>();
    }
}
