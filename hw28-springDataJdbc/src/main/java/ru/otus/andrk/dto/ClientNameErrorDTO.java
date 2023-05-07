package ru.otus.andrk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientNameErrorDTO {
    private String message;

    public static ClientNameErrorDTO emptyNameError(){
        return new ClientNameErrorDTO("Имя клиента должно быть заполнено");
    }
    public static ClientNameErrorDTO noNameError(){
        return new ClientNameErrorDTO();
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "message='" + message + '\'' +
                '}';
    }
}
