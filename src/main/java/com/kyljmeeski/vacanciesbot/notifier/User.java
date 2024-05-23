package com.kyljmeeski.vacanciesbot.notifier;

public class User {

    private final String chatId;

    public User(String chatId) {
        this.chatId = chatId;
    }

    public String chatId() {
        return chatId;
    }

}
