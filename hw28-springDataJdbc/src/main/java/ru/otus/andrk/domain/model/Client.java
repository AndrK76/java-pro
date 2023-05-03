package ru.otus.andrk.domain.model;


import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Client implements Cloneable {

    @Expose
    private Long id;

    @Expose
    private String name;

    @Expose
    private Address address;

    @Expose
    private List<Phone> phones;


    public Client(String name) {
        this(null, name);
    }

    public Client(Long id, String name) {
        this(id, name, null, null);
    }

    public Client(String name, Address address, List<Phone> phones) {
        this(null, name, address, phones);
    }

    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        // Если this.phones = phones то при передаче null поймаем
        // A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance
        this.phones = phones == null ? new ArrayList<>() : phones;
        if (this.phones != null) {
            this.phones.forEach(r -> r.setClient(this));
        }
    }

    @Override
    public Client clone() {
        return new Client(this.id, this.name, this.address, this.phones);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", phones=" + phones +
                '}';
    }
}
