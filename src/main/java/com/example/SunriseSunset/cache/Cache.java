package com.example.SunriseSunset.cache;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**A simple in-memory cache implementation using a HashMap.*/

@Component
public class Cache {

    private final Map<String, Object> cache;
    /**Constructs a new Cache instance with an empty HashMap.*/

    public Cache() {
        this.cache = new HashMap<>();
    }
    /**Retrieves a value from the cache by key.*/

    public Object get(String key) {
        return cache.get(key);
    }

    /**Stores a value in the cache with the specified key.*/
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    /**Checks if the cache contains a specific key.*/
    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    /**Clears all entries in the cache.*/
    public void clear() {
        cache.clear();
    }

    /**Returns the number of entries in the cache.*/
    public int size() {
        return cache.size();
    }
}