package ru.otus.andrk.example;

import java.util.List;

public interface DBService {
    User saveUser(String firstName, String lastName);
    List<User> findAllUsers();
}
