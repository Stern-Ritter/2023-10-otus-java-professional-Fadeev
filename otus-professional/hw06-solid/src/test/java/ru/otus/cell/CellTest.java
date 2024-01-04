package ru.otus.cell;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.banknote.Banknote;
import ru.otus.banknote.BanknoteFactory;
import ru.otus.banknote.Denomination;
import ru.otus.exceptions.InvalidAmountException;

import java.util.List;

import static ru.otus.banknote.Denomination.ONE_HUNDRED;

class CellTest {
    private final Denomination denomination = ONE_HUNDRED;
    private Cell<Banknote> cell;
    private BanknoteFactory banknoteFactory;

    @BeforeEach
    void setUp() {
        cell = new BanknoteCell<>(denomination);
        banknoteFactory = new BanknoteFactory();
    }

    @Test
    void addBanknote() {
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());

        int expectedBanknoteCount = 3;
        int actualBanknoteCount = cell.getBanknotesCount();
        Assertions.assertEquals(expectedBanknoteCount, actualBanknoteCount);
    }

    @Test
    void getBanknote() {
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());

        Banknote expectedBanknote = banknoteFactory.oneHundred();
        Banknote actualBanknote = cell.getBanknote();
        Assertions.assertEquals(expectedBanknote, actualBanknote);

        int expectedBanknoteCount = 2;
        int actualBanknoteCount = cell.getBanknotesCount();
        Assertions.assertEquals(expectedBanknoteCount, actualBanknoteCount);
    }

    @Test
    void getBanknotesWithValidRequestedCount() {
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());

        List<Banknote> expectedBanknotes = List.of(banknoteFactory.oneHundred(), banknoteFactory.oneHundred(),
                banknoteFactory.oneHundred());
        List<Banknote> actualBanknotes = cell.getBanknotes(3);
        Assertions.assertEquals(expectedBanknotes, actualBanknotes);

        int expectedBanknoteCount = 0;
        int actualBanknoteCount = cell.getBanknotesCount();
        Assertions.assertEquals(expectedBanknoteCount, actualBanknoteCount);
    }

    @Test
    void getBanknotesWithRequestedCountLessThanMinimumCount() {
        cell.addBanknote(banknoteFactory.oneHundred());

        Assertions.assertThrows(InvalidAmountException.class, () -> cell.getBanknotes(0));
        Assertions.assertThrows(InvalidAmountException.class, () -> cell.getBanknotes(-1));
    }

    @Test
    void getBanknotesWithRequestedCountMoreThanMaximumAvailableCount() {
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());

        Assertions.assertThrows(InvalidAmountException.class, () -> cell.getBanknotes(4));
    }

    @Test
    void getDenomination() {
        Denomination expectedDenomination = denomination;
        Denomination actualDenomination = cell.getDenomination();

        Assertions.assertEquals(expectedDenomination, actualDenomination);
    }

    @Test
    void getBanknotesCount() {
        cell.addBanknote(banknoteFactory.oneHundred());
        cell.addBanknote(banknoteFactory.oneHundred());

        int expectedBanknoteCount = 2;
        int actualBanknoteCount = cell.getBanknotesCount();
        Assertions.assertEquals(expectedBanknoteCount, actualBanknoteCount);
    }
}