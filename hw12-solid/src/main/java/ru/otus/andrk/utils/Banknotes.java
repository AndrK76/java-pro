package ru.otus.andrk.utils;

import ru.otus.andrk.domain.Banknote;

public record Banknotes (Banknote banknote, Integer count){

    @Override
    public String toString() {
        return "Banknotes{" +
                "banknote=" + banknote.name() +
                ", count=" + count +
                '}';
    }
}
