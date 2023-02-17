package ru.otus.andrk.atm;

import ru.otus.andrk.domain.*;
import ru.otus.andrk.utils.Banknotes;
import ru.otus.andrk.utils.BanknotesHandler;

import java.util.*;
import java.util.stream.Collectors;

public class AtmInstance implements Atm {
    private static final class CellState {
        private Integer count;
        private final Integer capacity;

        private CellState(Integer count, Integer capacity) {
            this.count = count;
            this.capacity = capacity;
        }
        public void setCount(int count) {
            if (count > capacity) {
                throw new AtmFullException();
            }
            this.count = count;
        }

        public Integer getCount() {
            return count;
        }

        public Integer getCapacity() {
            return capacity;
        }
    }

    private final Map<Banknote, CellState> cells;

    public AtmInstance(AtmCell... cells) {
        this.cells = Arrays.stream(cells).collect(Collectors.toMap(AtmCell::banknote, v -> new CellState(0, v.capacity())));
    }

    @Override
    public void putMoneyToAtm(Map<Banknote, Integer> banknotes) throws AtmFullException {
        for (var banknote : banknotes.keySet()) {
            if (!cells.containsKey(banknote)) {
                throw new NoCellForBanknoteException(String.format("Cell for banknote %s not present in this ATM", banknote));
            }
            var cell = cells.get(banknote);
            try {
                cell.count += banknotes.get(banknote);
            } catch (AtmFullException e) {
                throw new AtmFullException(String.format("Cell for banknote %s is full", banknote));
            }
        }
    }
    @Override
    public List<Banknote> getMoneyFromAtm(Integer sum) throws IllegalArgumentException, NoHaveBanknoteException {
        if (sum <= 0) {
            throw new IllegalArgumentException("Sum must be greeter then zero");
        }
        ArrayList<Banknotes> ret = new ArrayList<>();
        var aviableBanknotes = cells.keySet().stream()
                .filter(r -> cells.get(r).getCount() > 0)
                .sorted(Comparator.comparingInt(Banknote::nominal).reversed())
                .toList();

        tryComposeSum(sum, ret, aviableBanknotes);
        prepareComposedSum(ret);
        return BanknotesHandler.toListBanknote(ret);
    }

    @Override
    public Integer getMoneyLeftInAtm() {
        int sum = 0;
        for (var banknote : cells.keySet()) {
            sum += cells.get(banknote).getCount() * banknote.nominal();
        }
        return sum;
    }

    private void tryComposeSum(Integer sum, ArrayList<Banknotes> ret, List<Banknote> aviableBanknotes) {
        int neededSum = sum;
        for (var cell : aviableBanknotes) {
            if (cell.nominal() > neededSum) {
                continue;
            }
            var couToSend = Math.min((int) Math.floor((float) neededSum / (float) cell.nominal()), cells.get(cell).count);
            neededSum -= cell.nominal() * couToSend;

            ret.add(new Banknotes(cell, couToSend));
            if (neededSum == 0) {
                break;
            }
        }
        if (neededSum > 0) {
            throw new NoHaveBanknoteException("ATM don't have banknotes for you sum");
        }
    }

    private void prepareComposedSum(ArrayList<Banknotes> ret) {
        for (var banknotes : ret) {
            var cell = cells.get(banknotes.banknote());
            cell.setCount(cell.getCount() - banknotes.count());
        }
    }

}
