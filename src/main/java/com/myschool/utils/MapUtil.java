package com.myschool.utils;

import java.util.*;

public class MapUtil {
    public static <Long, Double extends Comparable<? super Double>> Map<Long, Double> sortByValue(Map<Long, Double> map) {
        List<Map.Entry<Long, Double>> list = new ArrayList<>(map.entrySet());
        //list.sort(Map.Entry.comparingByValue());

        Collections.sort(list, new Comparator<Map.Entry<Long, Double>>() {
            @Override
            public int compare(Map.Entry<Long, Double> entry1, Map.Entry<Long, Double> entry2) {
                return entry2.getValue().compareTo(entry1.getValue());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        Map<Long, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}