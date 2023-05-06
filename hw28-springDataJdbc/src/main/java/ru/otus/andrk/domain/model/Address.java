package ru.otus.andrk.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor

public class Address implements Cloneable {
    @Id
    @Column("address_id")
    private Long id;

    private String street;

    public Address(String street) {
        this(null,street);
    }

    @PersistenceCreator
    public Address(Long id, String street){
        this.id=id;
        this.street = street;
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
