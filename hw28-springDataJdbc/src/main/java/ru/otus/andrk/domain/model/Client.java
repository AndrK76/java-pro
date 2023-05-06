package ru.otus.andrk.domain.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Client implements Cloneable {

    @Id
    @Column("client_id")
    private Long id;

    private String name;


    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;


    public Client(String name) {
        this(null, name);
    }

    public Client(Long id, String name) {
        this(id, name, null, null);
    }

    public Client(String name, Address address, Set<Phone> phones) {
        this(null, name, address, phones);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        // Если this.phones = phones то при передаче null поймаем
        // A collection with cascade="all-delete-orphan" was no longer referenced by the owning entity instance
        this.phones = phones == null ? new HashSet<>() : phones;
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
