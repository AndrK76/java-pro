package ru.otus.andrk.domain;

public interface AtmCell {

    Integer capacity();

    Integer count();

    void tryAdd(Integer count);

    void add(Integer count);

    void tryGet(Integer count);

    void get(Integer count);
}
