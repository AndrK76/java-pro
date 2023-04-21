package ru.otus.andrk.model;

import com.google.gson.annotations.Expose;
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
@Table(name = "phone")
public class Phone implements Cloneable {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "phone_gen", sequenceName = "phone_seq",
            initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_gen")
    @Expose
    private Long id;
    @Column(name = "number")
    @Expose
    private String number;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    private Client client;


    public Phone(Long id, String number){
        this(id,number,null);
    }

    public Phone(String number) {
        this(null,number, null);
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
