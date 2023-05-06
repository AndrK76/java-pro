package ru.otus.andrk.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Phone implements Cloneable {

    @Id
    @Column("phone_id")
    private Long id;
    private String number;

    @JsonIgnore
    @Transient
    private Client client;


    public Phone(Long id, String number) {
        this(id, number, null);
    }

    public Phone(String number) {
        this(null, number, null);
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Phone(this.number);
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
