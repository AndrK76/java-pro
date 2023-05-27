package ru.otus.andrk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.andrk.example.SampleServer;

public class ServerGrpc {

    public static void main(String[] args) {
        new SampleServer().run();
    }
}