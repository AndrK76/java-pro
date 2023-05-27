package ru.otus.andrk.example;

import ru.otus.andrk.protobuf.generated.UserMessage;

public class Mapper {
    public static UserMessage user2UserMessage(User user) {
        return UserMessage.newBuilder()
                .setId(user.getId())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .build();
    }

    public static User userMessage2User(UserMessage userMessage) {
        return new User(
                userMessage.getId(),
                userMessage.getFirstName(),
                userMessage.getLastName()
        );
    }
}
