package ru.otus.andrk.atm;

import ru.otus.andrk.domain.*;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

import java.util.*;
import java.util.stream.Collectors;

public class AtmImpl implements Atm {

    private final Map<Banknote, AtmCellImpl> cells;

    public static Atm create(AtmCellDefinition... cells) {
        return new AtmImpl(cells);
    }

    private AtmImpl(AtmCellDefinition... cells) {
        this.cells = Arrays.stream(cells).collect(Collectors.toMap(AtmCellDefinition::banknote, v -> new AtmCellImpl(v.capacity())));
    }

    @Override
    public void putMoneyToAtm(Map<Banknote, Integer> banknotes) throws AtmFullException {
        ArrayList<Banknotes> moneyForPut = checkBanknotesAndPrepareForPut(banknotes);
        for (var banknotesForPut : moneyForPut) {
            cells.get(banknotesForPut.banknote()).add(banknotesForPut.count());
        }
    }

    @Override
    public List<Banknote> getMoneyFromAtm(Integer sum) throws IllegalArgumentException, NoHaveBanknoteException {
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum must be greeter then zero");
        }
        var aviableBanknotes = cells.keySet().stream()
                .filter(r -> cells.get(r).count() > 0)
                .sorted(Comparator.comparingInt(Banknote::nominal).reversed())
                .toList();

        var preparedBanknotes = tryComposeSum(sum, aviableBanknotes);
        giveComposedSum(preparedBanknotes);
        return BanknotesHandler.toListBanknote(preparedBanknotes);
    }

    @Override
    public Integer getMoneyLeftInAtm() {
        int sum = 0;
        for (var banknote : cells.keySet()) {
            sum += cells.get(banknote).count() * banknote.nominal();
        }
        return sum;
    }

    private ArrayList<Banknotes> checkBanknotesAndPrepareForPut(Map<Banknote, Integer> banknotes) {
        ArrayList<Banknotes> moneyForPut = new ArrayList<>();
        for (var banknote : banknotes.keySet()) {
            if (!cells.containsKey(banknote)) {
                throw new NoCellForBanknoteException(String.format("Cell for banknote %s not present in this ATM", banknote));
            }
            try {
                cells.get(banknote).tryAdd(banknotes.get(banknote));
            } catch (CellFullException e) {
                throw new AtmFullException(String.format("Cell for banknote %s is full", banknote));
            }
            moneyForPut.add(new Banknotes(banknote, banknotes.get(banknote)));
        }
        return moneyForPut;
    }

    private ArrayList<Banknotes> tryComposeSum(Integer sum, List<Banknote> aviableBanknotes) {
        var ret = new ArrayList<Banknotes>();
        int neededSum = sum;
        for (var cell : aviableBanknotes) {
            if (cell.nominal() > neededSum) {
                continue;
            }
            var couToSend = Math.min((int) Math.floor((float) neededSum / (float) cell.nominal()), cells.get(cell).count());
            neededSum -= cell.nominal() * couToSend;

            ret.add(new Banknotes(cell, couToSend));
            if (neededSum == 0) {
                break;
            }
        }
        if (neededSum > 0) {
            throw new NoHaveBanknoteException("ATM don't have banknotes for you sum");
        }
        return ret;
    }

    private void giveComposedSum(ArrayList<Banknotes> ret) {
        for (var banknotes : ret) {
            var cell = cells.get(banknotes.banknote());
            cell.get(banknotes.count());
        }
    }

}
