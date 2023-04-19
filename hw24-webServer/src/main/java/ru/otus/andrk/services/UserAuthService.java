package ru.otus.andrk.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
