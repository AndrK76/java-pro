package ru.otus.andrk;

import ru.otus.andrk.atm.AtmCell;
import ru.otus.andrk.atm.AtmInstance;
import ru.otus.andrk.domain.Atm;
import ru.otus.andrk.rubles.Rubles;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

public class AtmDemo {

    public static void main(String[] args) {
        new AtmDemo().run();
    }

    void run() {
        var rubles = Rubles.getInstance();
        var atm = makeAtm();
        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(rubles.getByNominal(50), 23),
                new Banknotes(rubles.getByNominal(200), 10),
                new Banknotes(rubles.getByNominal(1000), 10)
        ));
        showMoneyLeft(atm);

        var debit = 3200;
        tryGetMoney(atm, debit);
        showMoneyLeft(atm);

        debit = 3201;
        tryGetMoney(atm, debit);
        showMoneyLeft(atm);

        debit = 10000;
        tryGetMoney(atm, debit);
        showMoneyLeft(atm);

        debit = 950;
        tryGetMoney(atm, debit);
        showMoneyLeft(atm);

    }

    private void tryGetMoney(Atm atm, int debit) {
        System.out.println("\nЗапрос на выдачу: " + debit);
        try {
            var banknotes = atm.getMoneyFromAtm(debit);
            System.out.println("Выдано: ");
            BanknotesHandler.groupByBanknote(banknotes).forEach(x -> System.out.println("\t" + x));
        } catch (Exception e) {
            System.out.println("Ошибка выдачи: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
        }
    }

    private void showMoneyLeft(Atm atm){
        System.out.println("Сумма в банкомате: " + atm.getMoneyLeftInAtm());
    }


    Atm makeAtm() {
        var rubles = Rubles.getInstance();
        var atm = new AtmInstance(
                new AtmCell(rubles.getByNominal(5), 100),
                new AtmCell(rubles.getByNominal(10), 100),
                new AtmCell(rubles.getByNominal(50), 100),
                new AtmCell(rubles.getByNominal(100), 200),
                new AtmCell(rubles.getByNominal(200), 20),
                new AtmCell(rubles.getByNominal(500), 200),
                new AtmCell(rubles.getByNominal(1000), 200),
                new AtmCell(rubles.getByNominal(2000), 100),
                new AtmCell(rubles.getByNominal(5000), 100)
        );
        return atm;
    }
}
