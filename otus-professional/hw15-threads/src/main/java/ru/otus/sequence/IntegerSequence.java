package ru.otus.sequence;

public class IntegerSequence implements Sequence<Integer> {
    private Integer currentValue = 1;
    private boolean increase = true;

    @Override
    public Integer get() {
        Integer value = currentValue;
        generateNextValue();
        return value;
    }

    private void generateNextValue() {
        if (increase) {
            currentValue += 1;
            checkUpperLimit();
        } else {
            currentValue -= 1;
            checkLowerLimit();
        }
    }

    private void checkUpperLimit() {
        if (currentValue >= 10) {
            increase = false;
        }
    }

    private void checkLowerLimit() {
        if (currentValue <= 1) {
            increase = true;
        }
    }
}
