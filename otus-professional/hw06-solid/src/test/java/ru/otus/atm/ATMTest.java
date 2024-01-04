package ru.otus.atm;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.banknote.Banknote;
import ru.otus.banknote.BanknoteFactory;
import ru.otus.exceptions.NotEnoughMoneyException;
import ru.otus.exceptions.UnsupportedBanknoteDenominationException;

import java.util.List;
import java.util.Map;
import java.util.Set;

class ATMTest {
    private BanknoteFactory banknoteFactory;
    private Set<Banknote> supportedBanknotes;
    private List<Banknote> initialBanknotes;
    private ATM atm;

    private Map<Integer, List<Banknote>> enoughMoneyTestCases;
    private List<Integer> notEnoughMoneyTestCases;

    @BeforeEach
    void setUp() {
        banknoteFactory = new BanknoteFactory();
        supportedBanknotes = Set.of(
                banknoteFactory.oneHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.oneThousand(),
                banknoteFactory.fiveThousand()
        );

        initialBanknotes = List.of(
                banknoteFactory.oneHundred(),
                banknoteFactory.oneHundred(),
                banknoteFactory.oneHundred(),
                banknoteFactory.oneHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.oneThousand(),
                banknoteFactory.oneThousand(),
                banknoteFactory.oneThousand(),
                banknoteFactory.fiveThousand(),
                banknoteFactory.fiveThousand()
        );

        atm = new BasicATM(supportedBanknotes);

        notEnoughMoneyTestCases = List.of(
                15100,
                20000
        );

        enoughMoneyTestCases = Map.of(
                300, List.of(
                        banknoteFactory.twoHundred(),
                        banknoteFactory.oneHundred()
                ),
                800, List.of(
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.oneHundred()
                ),
                1700, List.of(
                        banknoteFactory.oneThousand(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.twoHundred()
                ),
                4000, List.of(
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.fiveHundred()
                ),
                4900, List.of(
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.oneHundred(),
                        banknoteFactory.oneHundred(),
                        banknoteFactory.oneHundred()
                ),
                9900, List.of(
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.oneHundred(),
                        banknoteFactory.oneHundred(),
                        banknoteFactory.oneHundred()
                ),
                10000, List.of(
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.fiveThousand()),
                11000, List.of(
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.oneThousand()),
                11800, List.of(
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.fiveThousand(),
                        banknoteFactory.oneThousand(),
                        banknoteFactory.fiveHundred(),
                        banknoteFactory.twoHundred(),
                        banknoteFactory.oneHundred()));
    }

    @Test
    void addBanknoteWithValidDenomination() {
        atm.addBanknote(banknoteFactory.oneHundred());
        atm.addBanknote(banknoteFactory.twoHundred());
        atm.addBanknote(banknoteFactory.fiveHundred());
        atm.addBanknote(banknoteFactory.oneThousand());
        atm.addBanknote(banknoteFactory.fiveThousand());

        int expectedBalance = 100 + 200 + 500 + 1000 + 5000;
        int currentBalance = atm.getBalance();

        Assertions.assertEquals(expectedBalance, currentBalance);
    }

    @Test
    void addBanknoteWithInvalidDenomination() {
        Assertions.assertThrows(UnsupportedBanknoteDenominationException.class,
                () -> atm.addBanknote(banknoteFactory.tenThousand()));

        int expectedBalance = 0;
        int currentBalance = atm.getBalance();

        Assertions.assertEquals(expectedBalance, currentBalance);
    }

    @Test
    void addBanknotesWithValidDenomination() {
        List<Banknote> banknotes = List.of(
                banknoteFactory.oneHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.oneThousand(),
                banknoteFactory.fiveThousand());

        atm.addBanknotes(banknotes);

        int expectedBalance = 100 + 200 + 500 + 1000 + 5000;
        int currentBalance = atm.getBalance();

        Assertions.assertEquals(expectedBalance, currentBalance);
    }

    @Test
    void addBanknotesWithInvalidDenomination() {
        List<Banknote> banknotes = List.of(
                banknoteFactory.oneHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.oneThousand(),
                banknoteFactory.fiveThousand(),
                banknoteFactory.tenThousand());

        Assertions.assertThrows(UnsupportedBanknoteDenominationException.class,
                () -> atm.addBanknotes(banknotes));

        int expectedBalance = 0;
        int currentBalance = atm.getBalance();

        Assertions.assertEquals(expectedBalance, currentBalance);
    }

    @Test
    void getAmountWithEnoughMoney() {
        for (Map.Entry<Integer, List<Banknote>> testCase : enoughMoneyTestCases.entrySet()) {
            atm = new BasicATM(supportedBanknotes);
            atm.addBanknotes(initialBanknotes);

            int requestedAmount = testCase.getKey();
            List<Banknote> expectedBanknotes = testCase.getValue();
            List<Banknote> actualBankNotes = atm.getAmount(requestedAmount);

            Assertions.assertEquals(expectedBanknotes, actualBankNotes);
        }
    }

    @Test
    void getAmountWithNotEnoughMoney() {
        for (Integer requestedAmount : notEnoughMoneyTestCases) {
            atm = new BasicATM(supportedBanknotes);
            atm.addBanknotes(initialBanknotes);

            Assertions.assertThrows(NotEnoughMoneyException.class, () -> atm.getAmount(requestedAmount));
        }
    }

    @Test
    void getBalance() {
        List<Banknote> banknotes = List.of(
                banknoteFactory.oneHundred(),
                banknoteFactory.oneHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.twoHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.fiveHundred(),
                banknoteFactory.oneThousand(),
                banknoteFactory.oneThousand(),
                banknoteFactory.fiveThousand(),
                banknoteFactory.fiveThousand());

        atm.addBanknotes(banknotes);

        int expectedBalance = 100 * 2 + 200 * 2 + 500 * 2 + 1000 * 2 + 5000 * 2;
        int currentBalance = atm.getBalance();

        Assertions.assertEquals(expectedBalance, currentBalance);
    }
}