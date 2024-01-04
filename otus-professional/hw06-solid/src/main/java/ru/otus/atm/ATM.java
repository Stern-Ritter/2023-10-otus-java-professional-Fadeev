package ru.otus.atm;

import ru.otus.banknote.Banknote;

import java.util.Collection;
import java.util.List;

public interface ATM {
    void addBanknote(Banknote banknote);

    void addBanknotes(Collection<Banknote> banknotes);

    List<Banknote> getAmount(int requestedAmount);

    int getBalance();
}
