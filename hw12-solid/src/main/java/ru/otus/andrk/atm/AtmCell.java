package ru.otus.andrk.atm;

import ru.otus.andrk.domain.Banknote;

public record AtmCell(Banknote banknote, Integer capacity) {
}
