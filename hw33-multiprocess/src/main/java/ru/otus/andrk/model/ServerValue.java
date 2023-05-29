package ru.otus.andrk.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerValue {
    private int value;
    private boolean applied;

    private final Lock lock = new ReentrantLock();

    public ServerValue(int value) {
        lock.lock();
        this.value = value;
        applied = false;
        lock.unlock();
    }

    private ServerValue(int value, boolean applied) {
        lock.lock();
        this.value = value;
        this.applied = applied;
        lock.unlock();
    }

    public boolean isApplied() {
        return applied;
    }

    public void apply(int value) {
        lock.lock();
        this.applied = (value == this.value);
        lock.unlock();
    }

    public int value() {
        return value;
    }
    public ServerValue getValue() {
        lock.lock();
        var ret = new ServerValue(this.value(), this.isApplied());
        lock.unlock();
        return ret;
    }


    public void setValue(int value) {
        lock.lock();
        this.value = value;
        applied = false;
        lock.unlock();
    }

}
