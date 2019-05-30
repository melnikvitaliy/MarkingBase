package com.initflow.marking.base.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapListValueUtil {

    private MapListValueUtil(){}

    public static <T,K> void put(Map<T, List<K>> map, T key, K value){
        List<K> valueByKey = map.get(key);
        if(valueByKey == null){
            List<K> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        } else {
            valueByKey.add(value);
        }
    }
}
