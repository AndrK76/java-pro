package ru.otus.crm.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "client")
public class Client implements Cloneable {

    @Id
    @SequenceGenerator(name = "client_gen", sequenceName = "client_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "client", orphanRemoval = true)
    //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client", orphanRemoval = true)
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
