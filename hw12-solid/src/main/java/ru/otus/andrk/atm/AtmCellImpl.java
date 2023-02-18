package ru.otus.andrk.atm;

import ru.otus.andrk.domain.*;

public final class AtmCellImpl implements AtmCell {
    private final Integer capacity;
    private Integer count;


    public AtmCellImpl(Integer capacity) {
        this.capacity = capacity;
        this.count=0;
    }

    public Integer capacity() {
        return capacity;
    }

    public Integer count() {
        return count;
    }

    public void tryAdd(Integer count) {
        checkCountArgument(count);
        if (this.count + count > this.capacity) {
            throw new CellFullException();
        }
    }

    public void add(Integer count) {
        tryAdd(count);
        this.count += count;
    }

    public void tryGet(Integer count) {
        checkCountArgument(count);
        if (this.count - count < 0) {
            throw new NoHaveBanknoteException();
        }
    }

    public void get(Integer count) {
        tryGet(count);
        this.count -= count;
    }

    private void checkCountArgument(Integer count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be greatest zero");
        }
    }

}
