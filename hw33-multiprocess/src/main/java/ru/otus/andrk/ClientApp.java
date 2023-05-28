package ru.otus.andrk;

import ru.otus.andrk.client.ClientGrpc;

public class ClientApp {

    public static void main(String[] args) {

        new ClientGrpc().run();
    }
}
