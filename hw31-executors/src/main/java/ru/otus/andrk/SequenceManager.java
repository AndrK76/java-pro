package ru.otus.andrk;

public class SequenceManager {

    private final SequenceStorage[] sequences;
    private int currentIndex;

    public SequenceManager(SequenceStorage... sequences) {
        this.sequences = sequences;
        if (sequences == null || sequences.length < 2) {
            throw new IllegalArgumentException("Необходимо указать не менее 2 источников последовательностей");
        }
        currentIndex = 0;
    }

    public SequenceStorage getActiveStorage() {
        return sequences[currentIndex];
    }

    public void setNextStorage() {
        currentIndex++;
        if (currentIndex == sequences.length) {
            currentIndex = 0;
        }
    }

    public SequenceStorage[] getSequences() {
        return sequences;
    }
}
