package ru.otus.andrk.server;

public interface SimpleWebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
