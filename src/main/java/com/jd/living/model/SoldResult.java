package com.jd.living.model;

import java.util.ArrayList;
import java.util.List;

public class SoldResult extends Result {

    public List<Listing> sold = new ArrayList<Listing>();

    public SoldResult(){}

    public SoldResult(int totalCount, int count, List<Listing> sold){
        super();
        this.totalCount = totalCount;
        this.count = count;
        this.sold = sold;
    }

    @Override
    public List<Listing> getResult() {
        return sold;
    }
}