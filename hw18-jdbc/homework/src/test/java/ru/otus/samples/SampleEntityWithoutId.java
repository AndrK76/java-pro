package ru.otus.samples;

import ru.otus.core.annotations.Id;

public class SampleEntityWithoutId {
    private long id;
    private String field1;
    private String field2;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }
}
