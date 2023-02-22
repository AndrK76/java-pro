package ru.otus.andrk.rubles;

import ru.otus.andrk.domain.Banknote;

public record RubleBanknote(String name, Integer nominal) implements Banknote {
    public RubleBanknote {
        if (nominal <= 0) {
            throw new IllegalArgumentException("nominal must be greatest zero");
        }
    }

    @Override
    public String toString() {
        return "RubleBanknote {" +
                "name='" + name + '\'' +
                ", nominal=" + nominal +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RubleBanknote that = (RubleBanknote) o;

        return nominal.equals(that.nominal);
    }

    @Override
    public int hashCode() {
        return nominal.hashCode();
    }
}
