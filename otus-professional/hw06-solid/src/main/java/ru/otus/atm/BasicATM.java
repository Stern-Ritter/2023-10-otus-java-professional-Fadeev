package ru.otus.atm;

import ru.otus.banknote.Banknote;
import ru.otus.banknote.Denomination;
import ru.otus.cell.BanknoteCell;
import ru.otus.cell.Cell;
import ru.otus.exceptions.InvalidAmountException;
import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.exceptions.UnsupportedBanknoteDenominationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

public class BasicATM implements ATM {
    private final String UNSUPPORTED_BANKNOTE_EXCEPTION_TEMPLATE = "The banknote with the denomination: %d is not supported by the ATM";
    private final String INVALID_REQUESTED_AMOUNT_EXCEPTION_TEMPLATE = "The requested amount: %d is invalid";
    private final String NOT_ENOUGH_MONEY_EXCEPTION_TEMPLATE = "There is not enough money in the ATM to dispense the requested amount: %d";
    private final NavigableMap<Integer, Cell<Banknote>> cells;

    public BasicATM(Set<Banknote> supportedBanknotes) {
        this.cells = new TreeMap<>(Comparator.reverseOrder());
        for (Banknote banknote : supportedBanknotes) {
            Denomination denomination = banknote.getDenomination();
            Cell<Banknote> cell = new BanknoteCell<>(denomination);
            cells.put(denomination.getValue(), cell);
        }
    }

    @Override
    public void addBanknote(Banknote banknote) {
        validateBanknoteDenomination(banknote);
        addBanknoteToCell(banknote);
    }

    @Override
    public void addBanknotes(Collection<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            validateBanknoteDenomination(banknote);
        }
        for (Banknote banknote : banknotes) {
            addBanknoteToCell(banknote);
        }
    }

    @Override
    public List<Banknote> getAmount(int requestedAmount) {
        validateRequestedAmount(requestedAmount);
        checkEnoughMoney(requestedAmount);
        return getBanknotes(requestedAmount);
    }

    @Override
    public int getBalance() {
        return cells.values()
                .stream()
                .mapToInt((cell) -> cell.getDenomination().getValue() * cell.getBanknotesCount())
                .reduce(0, Integer::sum);
    }

    private void addBanknoteToCell(Banknote banknote) {
        int denomination = banknote.getDenomination().getValue();
        Cell<Banknote> cell = cells.get(denomination);
        cell.addBanknote(banknote);
    }

    private void validateRequestedAmount(int requestedAmount) {
        boolean lessThanMinimumBanknoteDenomination = cells.lastEntry() == null || requestedAmount < cells.lastEntry().getKey();

        if (lessThanMinimumBanknoteDenomination) {
            throw new InvalidAmountException(String.format(INVALID_REQUESTED_AMOUNT_EXCEPTION_TEMPLATE, requestedAmount));
        }
    }

    private void validateBanknoteDenomination(Banknote banknote) {
        int denomination = banknote.getDenomination().getValue();
        if (!cells.containsKey(denomination)) {
            throw new UnsupportedBanknoteDenominationException(String.format(UNSUPPORTED_BANKNOTE_EXCEPTION_TEMPLATE, denomination));
        }
    }

    private List<Banknote> getBanknotes(int requestedAmount) {
        int remainingAmount = requestedAmount;
        List<Banknote> requestedBanknotes = new ArrayList<>();
        for (Cell<Banknote> cell : cells.values()) {
            if (remainingAmount == 0) {
                break;
            }

            int maximumBanknotesCount = cell.getBanknotesCount();
            int denomination = cell.getDenomination().getValue();
            int requestedBanknotesCount = remainingAmount / denomination;
            int receivedBanknotesCount = Math.min(maximumBanknotesCount, requestedBanknotesCount);
            if (receivedBanknotesCount == 0) {
                continue;
            }
            remainingAmount -= denomination * receivedBanknotesCount;
            requestedBanknotes.addAll(cell.getBanknotes(receivedBanknotesCount));
        }
        return requestedBanknotes;
    }

    private void checkEnoughMoney(int requestedAmount) {
        int remainingAmount = requestedAmount;
        for (Cell<Banknote> cell : cells.values()) {
            if (remainingAmount == 0) {
                break;
            }

            int maximumBanknotesCount = cell.getBanknotesCount();
            int denomination = cell.getDenomination().getValue();
            int requestedBanknotesCount = remainingAmount / denomination;
            int receivedBanknotesCount = Math.min(maximumBanknotesCount, requestedBanknotesCount);
            if (receivedBanknotesCount == 0) {
                continue;
            }
            remainingAmount -= denomination * receivedBanknotesCount;
        }

        if (remainingAmount != 0) {
            throw new NotEnoughMoneyException(NOT_ENOUGH_MONEY_EXCEPTION_TEMPLATE);
        }
    }
}
