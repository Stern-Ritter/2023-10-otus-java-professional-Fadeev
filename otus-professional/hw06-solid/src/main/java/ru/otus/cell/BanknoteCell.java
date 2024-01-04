package ru.otus.cell;

import ru.otus.banknote.Denomination;
import ru.otus.exceptions.InvalidAmountException;

import java.util.LinkedList;
import java.util.List;

public class BanknoteCell<T> implements Cell<T> {
    private final String INVALID_REQUESTED_BANKNOTES_COUNT_EXCEPTION_TEMPLATE = "The requested banknotes count: %d is invalid";
    private final Denomination denomination;
    private LinkedList<T> banknotes;

    public BanknoteCell(Denomination denomination) {
        this.denomination = denomination;
        this.banknotes = new LinkedList<>();
    }

    @Override
    public void addBanknote(T banknote) {
        banknotes.add(banknote);
    }

    @Override
    public T getBanknote() {
        return banknotes.pollLast();
    }

    @Override
    public List<T> getBanknotes(int requestedCount) {
        validateRequestedBanknotesCount(requestedCount);
        List<T> requestedBanknotes = new LinkedList<>(banknotes.subList(0, requestedCount));
        banknotes = new LinkedList<>(banknotes.subList(requestedCount, banknotes.size()));
        return requestedBanknotes;
    }

    @Override
    public Denomination getDenomination() {
        return denomination;
    }

    @Override
    public int getBanknotesCount() {
        return banknotes.size();
    }

    private void validateRequestedBanknotesCount(int requestedCount) {
        boolean lessThanMinimumCount = requestedCount <= 0;
        boolean moreThanMaximumAvailableCount = requestedCount > getBanknotesCount();

        if (lessThanMinimumCount || moreThanMaximumAvailableCount) {
            throw new InvalidAmountException(String.format(INVALID_REQUESTED_BANKNOTES_COUNT_EXCEPTION_TEMPLATE, requestedCount));
        }
    }
}
