package ru.otus.andrk.services;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserAuthServiceImpl implements UserAuthService {

    private final Map<String,String> users;

    public InMemoryUserAuthServiceImpl() {
        users = new HashMap<>();
        users.put("user","password");
    }


    @Override
    public boolean authenticate(String login, String password) {
        return users.containsKey(login) && users.get(login).equals(password);
    }


}
