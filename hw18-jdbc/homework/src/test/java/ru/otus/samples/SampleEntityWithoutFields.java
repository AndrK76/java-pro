package ru.otus.samples;

import ru.otus.core.annotations.Id;

public class SampleEntityWithoutFields {
    @Id
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
