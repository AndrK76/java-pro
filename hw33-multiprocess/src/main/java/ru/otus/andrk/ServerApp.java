package ru.otus.andrk;

import ru.otus.andrk.server.ServerGrpc;

public class ServerApp {

    public static void main(String[] args) {
        new ServerGrpc().run();
    }
}