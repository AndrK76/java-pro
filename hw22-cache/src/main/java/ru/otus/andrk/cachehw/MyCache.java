package ru.otus.andrk.cachehw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    private static final Logger log = LoggerFactory.getLogger(HwCache.class);

    private final Map<K, V> store = new WeakHashMap<>();

    //так сделал только для проверки side-эффекта
    private final Set<WeakReference<HwListener<K,V>>> listeners = new HashSet<>();

    private boolean showLog = false;

    @Override
    public void put(K key, V value) {
        store.put(key, value);
        notify(key, value, "put");
    }

    @Override
    public void remove(K key) {
        if (store.containsKey(key)) {
            store.remove(key);
            notify(key, null, "remove");
        } else {
            store.remove(key);
            notify(key, null, "no remove");
        }
    }

    @Override
    public V get(K key) {
        if (store.containsKey(key)) {
            V value = store.get(key);
            notify(key, value, "get");
            return value;
        } else {
            notify(key, null, "no get");
            return null;
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        log.trace("addListener ({})", listener);
        addListener(new WeakReference<>(listener));
    }

    @Override
    public void addListener(WeakReference<HwListener<K, V>> listenerRef) {
        showLog = true;
        log.trace("addListener by ref ({})", listenerRef);
        listeners.add(listenerRef);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        log.trace("removeListener ({})", listener);
        var listenerRef = listeners.stream()
                        .filter(r->r.get()==listener).findFirst();
        listenerRef.ifPresent(this::removeListener);
    }

    @Override
    public void removeListener(WeakReference<HwListener<K, V>> listenerRef) {
        log.trace("removeListener by ref ({})", listenerRef);
        listeners.remove(listenerRef);
    }

    private void notify(K key, V value, String action) {
        var liveListeners = listeners.stream()
                .map(Reference::get)
                .filter(Objects::nonNull).toList();
        if (showLog){ //Не захламляем вывод в упражнении с DB Service
            log.trace("cache size={}, listeners count={}", store.size(), liveListeners.size());
            log.debug("notify ({},{},{})", key, value, action);
        }
        liveListeners.forEach(listener -> listener.notify(key, value, action));
    }
}
