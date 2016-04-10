package org.singular.logDistribution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Aggregator {
    Map<String, String> logs = new ConcurrentHashMap<String, String>();

    public Aggregator(){

    }

    public void add(String host, String value) {
        logs.put(host, value);
    }
}
