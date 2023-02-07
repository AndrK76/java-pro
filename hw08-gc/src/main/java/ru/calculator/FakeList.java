package ru.calculator;

public class FakeList<T> {

    private int _size = 0;

    public boolean add(T e) {
        _size++;
        return true;
    }

    public int size() {
        return _size;
    }

    public void clear() {
        _size = 0;
    }

}
