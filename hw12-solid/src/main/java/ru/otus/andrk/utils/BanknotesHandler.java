package ru.otus.andrk.utils;

import ru.otus.andrk.domain.Banknote;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BanknotesHandler {
    public static Map<Banknote, Integer> toMap(Banknotes... banknotes) {
        return Arrays.stream(banknotes).collect(Collectors.toMap(Banknotes::banknote, Banknotes::count));
    }

    public static List<Banknote> toListBanknote(List<Banknotes> banknotes) {
        var ret = new ArrayList<Banknote>();
        for (var banknotePart : banknotes) {
            for (int i = 0; i < banknotePart.count(); i++) {
                ret.add(banknotePart.banknote());
            }
        }
        return ret;
    }

}
