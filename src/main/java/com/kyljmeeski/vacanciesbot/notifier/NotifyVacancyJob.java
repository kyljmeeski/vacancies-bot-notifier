package com.kyljmeeski.vacanciesbot.notifier;

import com.kyljmeeski.rabbitmqwrapper.Producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

public class NotifyVacancyJob implements Consumer<String> {

    private final Users users;
    private final Producer producer;

    public NotifyVacancyJob(Users users, Producer producer) {
        this.users = users;
        this.producer = producer;
    }

    @Override
    public void accept(String deliveredMessage) {
        Vacancy vacancy = new Vacancy(deliveredMessage);
        for (User user : users.all()) {
            TelegramMessageMessage messageToSend = new TelegramMessageMessage(user, vacancy);
            try {
                producer.produce(messageToSend.toString());
                System.out.println(vacancy.id() + " -> " + user.chatId());
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
