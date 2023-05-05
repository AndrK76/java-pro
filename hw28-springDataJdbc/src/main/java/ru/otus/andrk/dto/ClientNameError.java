package ru.otus.andrk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientNameError {
    private String message;

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "message='" + message + '\'' +
                '}';
    }
}
