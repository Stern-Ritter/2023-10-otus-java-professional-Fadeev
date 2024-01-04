package ru.otus.banknote;

import java.util.Objects;

public class Banknote {
    private final Denomination denomination;

    public Banknote(Denomination denomination) {
        this.denomination = denomination;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination;
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }

    @Override
    public String toString() {
        return "Banknote{" +
                "denomination=" + denomination +
                '}';
    }
}
