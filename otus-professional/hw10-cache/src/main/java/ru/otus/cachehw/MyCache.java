package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
    private final Map<K, V> cache;
    private final Set<HwListener<K, V>> listeners;

    public MyCache() {
        cache = new WeakHashMap<>();
        listeners = new HashSet<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, Action.ADD.name());
    }

    @Override
    public void remove(K key) {
        V value = cache.remove(key);
        notifyListeners(key, value, Action.REMOVE.name());
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyListeners(key, value, Action.GET.name());
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (HwListener<K, V> listener : listeners) {
            try {
                listener.notify(key, value, action);
            } catch (Exception ex) {
                logger.error("Notify error: listener: {} key = {}, value = {}, action = {}", listener, key, value, action);
            }
        }
    }
}
