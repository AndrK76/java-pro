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
public class Phone implements Cloneable {
    @Expose
    private Long id;
    @Expose
    private String number;

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
