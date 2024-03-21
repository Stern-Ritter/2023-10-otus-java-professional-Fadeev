package ru.otus.model;

public class Sequence {
    private long start;
    private long end;
    private long step;

    public Sequence(long start, long end, long step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }
}
