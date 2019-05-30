package com.initflow.marking.base.models;

import java.util.Set;

public class SearchRequest {

    private Set<Long> ids;
//    private String user_name;

    public Set<Long> getIds() {
        return ids;
    }

    public void setIds(Set<Long> ids) {
        this.ids = ids;
    }

//    public String getUser_name() {
//        return user_name;
//    }
//
//    public void setUser_name(String user_name) {
//        this.user_name = user_name;
//    }
}
