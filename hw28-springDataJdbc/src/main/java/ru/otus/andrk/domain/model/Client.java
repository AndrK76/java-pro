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
//TODO: Правильнее разделить на Client - Понятие BL и на ClientDAL и ClientDTO
//      относится так же и к Address и Phone
public class Client implements Cloneable {

    @Id
    @Column("client_id")
    private Long id;

    private String name;


    //@MappedCollection(idColumn = "client_id") //Так тоже работает
    @Column("client_id")
    private Address address;

   //TODO:(!!!) Получается для варианта с List обязательно нужна колонка сортировки?
   //@MappedCollection(idColumn = "client_id", keyColumn = "phone_id") -- в phone_id вставим 0
   //@MappedCollection(idColumn = "client_id", keyColumn = "client_id") -- упадём при Insert
   //@MappedCollection(idColumn = "client_id")  - требуется колонка client_key
   //private List<Phone> phones;
    @MappedCollection(idColumn = "client_id", keyColumn = "client_id")
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
