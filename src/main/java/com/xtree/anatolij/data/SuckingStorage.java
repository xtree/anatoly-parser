package com.xtree.anatolij.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SuckingStorage {
    private static final Map<String,List<Long>> storage= new HashMap<>();
    public static void noteSucking(String name, long time){
        List<Long> sucks;
        if (!storage.containsKey(name)) {
            sucks = new ArrayList<>();
            storage.put(name, sucks);
        } else {
            sucks = storage.get(name);
        }
        sucks.add(time);
        storage.put(name,cleanSucks(sucks,time));

    }

    private static List<Long> cleanSucks(List<Long> sucks, long time) {
        return sucks.stream().filter(suck -> suck > time - 15*60*1000).collect(Collectors.toList());
    }

    public static boolean isSuspicious(String name){
        if (!storage.containsKey(name)){ return false;}
        List<Long> sucks = storage.get(name);
        return sucks.size() > 2;
    }
}
