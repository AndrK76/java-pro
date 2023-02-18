package ru.otus.andrk.domain;

import java.util.Objects;

public final class AtmCellDefinition {
    private final Banknote banknote;
    private final Integer capacity;

    public AtmCellDefinition(Banknote banknote, Integer capacity) {
        this.banknote = banknote;
        this.capacity = capacity;
    }

    public Banknote banknote() {
        return banknote;
    }

    public Integer capacity() {
        return capacity;
    }
}
