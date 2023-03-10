package ru.otus.andrk;

import ru.otus.andrk.atm.AtmImpl;
import ru.otus.andrk.domain.Atm;
import ru.otus.andrk.domain.AtmCellDefinition;
import ru.otus.andrk.rubles.Rubles;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

public class AtmDemo {

    public static void main(String[] args) {
        new AtmDemo().run();
    }

    //Пример использования, детальнее в тестах
    void run() {
        var atm = makeAtm();
        atm.putMoneyToAtm(BanknotesHandler.toMap(
                new Banknotes(Rubles.getByNominal(50), 23),
                new Banknotes(Rubles.getByNominal(200), 10),
                new Banknotes(Rubles.getByNominal(1000), 10)
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
            System.out.println(" Сумма=" + BanknotesHandler.sum(banknotes));
        } catch (Exception e) {
            System.out.println("Ошибка выдачи: " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
        }
    }

    private void showMoneyLeft(Atm atm) {
        System.out.println("Остаток в банкомате: " + atm.getMoneyLeftInAtm());
    }


    Atm makeAtm() {
        return AtmImpl.create(
                new AtmCellDefinition(Rubles.getByNominal(5), 100),
                new AtmCellDefinition(Rubles.getByNominal(10), 100),
                new AtmCellDefinition(Rubles.getByNominal(50), 100),
                new AtmCellDefinition(Rubles.getByNominal(100), 200),
                new AtmCellDefinition(Rubles.getByNominal(200), 20),
                new AtmCellDefinition(Rubles.getByNominal(500), 200),
                new AtmCellDefinition(Rubles.getByNominal(1000), 200),
                new AtmCellDefinition(Rubles.getByNominal(2000), 100),
                new AtmCellDefinition(Rubles.getByNominal(5000), 100)
        );
    }
}
