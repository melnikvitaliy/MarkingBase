package com.initflow.marking.base.models;

import java.util.Set;

public class SearchRequest<T> {

    private Set<T> ids;

    public Set<T> getIds() {
        return ids;
    }

    public void setIds(Set<T> ids) {
        this.ids = ids;
    }

}
