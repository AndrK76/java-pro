package ru.otus.andrk.cachehw;


import java.lang.ref.WeakReference;

public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<K, V> listener);

    //Для эксперимента с side-эффектом когда Idea предлагает упростить код
    void addListener(WeakReference<HwListener<K, V>> listenerRef);

    void removeListener(HwListener<K, V> listener);

    //Для эксперимента с side-эффектом когда Idea предлагает упростить код
    void removeListener(WeakReference<HwListener<K, V>> listenerRef);

    //Для проверок размера кэша
    int getCacheSize();
}
