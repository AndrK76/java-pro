package ru.otus.andrk;

class SequenceStorage {
    private int sequencePosition = 1;
    private int direction = 1;

    public int get() {
        int ret = sequencePosition;
        setNext();
        return ret;
    }

    private void setNext() {
        sequencePosition += direction;
        if (sequencePosition >= 10 || sequencePosition <= 1) {
            direction *= -1;
        }
    }
}
