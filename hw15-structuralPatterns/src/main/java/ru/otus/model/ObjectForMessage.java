package ru.otus.model;

import ru.otus.model.homework.CloneableObject;

import java.util.List;

public class ObjectForMessage  implements CloneableObject<ObjectForMessage> {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    //

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }

    @Override
    public ObjectForMessage getClone() {
        var ret = new ObjectForMessage();
        ret.setData(this.data.stream().toList());
        return ret;
    }
}
