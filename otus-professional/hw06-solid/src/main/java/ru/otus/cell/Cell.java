package ru.otus.cell;

import ru.otus.banknote.Denomination;

import java.util.List;

public interface Cell<T> {
    void addBanknote(T banknote);

    T getBanknote();

    List<T> getBanknotes(int count);

    Denomination getDenomination();

    int getBanknotesCount();
}
