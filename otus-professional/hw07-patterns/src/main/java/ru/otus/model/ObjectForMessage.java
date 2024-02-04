package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage copy() {
        ObjectForMessage objectForMessageCopy = new ObjectForMessage();
        if (this.data != null) {
            objectForMessageCopy.setData(new ArrayList<>(this.data));
        }

        return objectForMessageCopy;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{" +
                "data=" + data +
                '}';
    }
}
