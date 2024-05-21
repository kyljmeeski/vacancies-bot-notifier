package com.kyljmeeski.vacanciesbot.notifier;

import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        try {
            Connection connection = factory.newConnection();
            Queues queues = new Queues(connection);
            queues.declare(
                    "vacancies-to-notify", false, false, false, null
            );
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
