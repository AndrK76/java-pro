package ru.otus.crm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@ToString
@Table(name = "address")
public class Address implements Cloneable {
    @Id
    @SequenceGenerator(name = "address_gen", sequenceName = "address_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_gen")
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
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
