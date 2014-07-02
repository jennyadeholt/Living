package com.jd.living.model;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by jennynilsson on 2014-04-21.
 */

public class Result {

    public int totalCount = 0;
    public int count = 0;

    public SearchParams searchParams;

    public class SearchParams {
        public String minLivingArea = "";
        public String areaId = "";
        public String minListPrice = "";
        public String maxListPrice = "";
    }

    public Result() {}

    public List<Listing> getResult() {
        return new ArrayList<Listing>();
    }
}
