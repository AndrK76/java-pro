package ru.otus.andrk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final int SLEEP_INTERVAL = 200;
    private static SequenceManager manager;

    //TODO: добавил ещё парочку потоков для красоты
    public static void main(String[] args) {
        var main = new Main();

        manager = new SequenceManager(
                new SequenceStorage()
                , new SequenceStorage()
                , new SequenceStorage()
                , new SequenceStorage()
        );

        for (int i = 0; i < manager.getSequences().length; i++) {
            var seq = manager.getSequences()[i];
            new Thread(() -> main.showSequence(seq), String.format("th%d", i + 1)).start();
        }

    }

    private synchronized void showSequence(SequenceStorage storage) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (manager.getActiveStorage() != storage) {
                    this.wait();
                }
                prettyLogCurrVal(storage);
                sleep();
                manager.setNextStorage();
                notifyAll();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void prettyLogCurrVal(SequenceStorage storage) {
        int curThreadNum = 0;
        Pattern p = Pattern.compile("\\d+");
        var matcher = p.matcher(Thread.currentThread().getName());
        if (matcher.find()) {
            curThreadNum = Integer.parseInt(matcher.group());
        }
        log.info("Value: {}", String.format("%" + curThreadNum * 4 + "s", " ") + storage.get());
    }

    private static void sleep() {
        try {
            Thread.sleep(SLEEP_INTERVAL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}