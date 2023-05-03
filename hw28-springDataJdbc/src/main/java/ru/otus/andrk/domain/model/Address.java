package ru.otus.andrk.domain.model;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Cloneable {
    @Expose
    private Long id;

    @Expose
    private String street;

    public Address(String street) {
        this(null,street);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Address(this.street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
