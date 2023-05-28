package ru.otus.andrk.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
        var ret = value;
        return ret;
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
