package com.jd.living.model;

import java.util.List;

/**
 * Created by jennynilsson on 2014-06-25.
 */
public class Area {

    private int booliId;
    private String name;

    private List<String> types;

    private int parentBooliId;
    private String parentName;
    private List<String> parentTypes;
    private String fullName;


    public String getName() {
        return name;
    }
}
