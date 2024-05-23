package com.kyljmeeski.vacanciesbot.notifier;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class TelegramMessageMessage {

    private final User user;
    private final Vacancy vacancy;

    public TelegramMessageMessage(User user, Vacancy vacancy) {
        this.user = user;
        this.vacancy = vacancy;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("chat_id", user.chatId());
        jsonObject.add("vacancy", vacancy.json());
        return new Gson().toJson(jsonObject);
    }
}
