package ru.otus.andrk.domain;

import java.util.List;
import java.util.Map;

public interface Atm {
    void putMoneyToAtm(Map<Banknote, Integer> banknotes) throws AtmFullException;

    List<Banknote> getMoneyFromAtm(Integer sum) throws IllegalArgumentException, NoHaveBanknoteException;

    Integer getMoneyLeftInAtm();

}
