package ru.otus.andrk;

import ru.otus.andrk.atm.AtmCell;
import ru.otus.andrk.atm.AtmInstance;
import ru.otus.andrk.domain.Atm;
import ru.otus.andrk.rubles.Rubles;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

public class AtmDemo {
    static Rubles rubles = Rubles.getInstance();

    public static void main(String[] args) {
        var atm = makeAtm();
        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(rubles.getByNominal(50), 23),
                new Banknotes(rubles.getByNominal(1000), 10)
        ));
        System.out.println(atm.getMoneyLeftInAtm());
        var banknotes = atm.getMoneyFromAtm(3200);
        System.out.println(atm.getMoneyLeftInAtm());
    }

    static Atm makeAtm() {
        var atm = new AtmInstance(
                new AtmCell(rubles.getByNominal(5), 100),
                new AtmCell(rubles.getByNominal(10), 100),
                new AtmCell(rubles.getByNominal(50), 100),
                new AtmCell(rubles.getByNominal(100), 200),
                new AtmCell(rubles.getByNominal(500), 200),
                new AtmCell(rubles.getByNominal(1000), 200),
                new AtmCell(rubles.getByNominal(2000), 100),
                new AtmCell(rubles.getByNominal(5000), 100)
        );
        return atm;
    }
}
