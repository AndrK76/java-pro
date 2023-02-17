package ru.otus.andrk.rubles;

import ru.otus.andrk.domain.Banknote;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rubles {

    private static Rubles instance = null;
    private final Map<Integer, Banknote> banknotes;

    private Rubles() {
        banknotes = Stream.of(
                new RubleBanknote("5 рублей", 5),
                new RubleBanknote("10 рублей", 10),
                new RubleBanknote("50 рублей", 50),
                new RubleBanknote("100 рублей", 100),
                new RubleBanknote("200 рублей", 200),
                new RubleBanknote("500 рублей", 500),
                new RubleBanknote("1000 рублей", 1000),
                new RubleBanknote("2000 рублей", 2000),
                new RubleBanknote("5000 рублей", 5000)
        ).collect(Collectors.toMap(RubleBanknote::nominal, v -> v));
    }

    public static Rubles getInstance() {
        if (instance == null) {
            instance = new Rubles();
        }
        return instance;
    }

    public Iterable<? extends Banknote> getBanknotes() {
        return banknotes.values();
    }

    public Banknote getByNominal(Integer nominal) throws IllegalArgumentException {
        if (!banknotes.containsKey(nominal)) {
            throw new IllegalArgumentException(String.format("Купюры с номиналом %d не существует", nominal));
        }
        return banknotes.get(nominal);
    }

}
