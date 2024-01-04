package ru.otus.banknote;

import static ru.otus.banknote.Denomination.FIVE_HUNDRED;
import static ru.otus.banknote.Denomination.FIVE_THOUSAND;
import static ru.otus.banknote.Denomination.ONE_HUNDRED;
import static ru.otus.banknote.Denomination.ONE_THOUSAND;
import static ru.otus.banknote.Denomination.TEN_THOUSAND;
import static ru.otus.banknote.Denomination.TWO_HUNDRED;

public class BanknoteFactory {
    public Banknote oneHundred() {
        return new Banknote(ONE_HUNDRED);
    }

    public Banknote twoHundred() {
        return new Banknote(TWO_HUNDRED);
    }

    public Banknote fiveHundred() {
        return new Banknote(FIVE_HUNDRED);
    }

    public Banknote oneThousand() {
        return new Banknote(ONE_THOUSAND);
    }

    public Banknote fiveThousand() {
        return new Banknote(FIVE_THOUSAND);
    }

    public Banknote tenThousand() {
        return new Banknote(TEN_THOUSAND);
    }
}
