package com.kyljmeeski.vacanciesbot.notifier;

import com.kyljmeeski.rabbitmqwrapper.Consumer;
import com.kyljmeeski.rabbitmqwrapper.PlainConsumer;
import com.kyljmeeski.rabbitmqwrapper.Queues;
import com.kyljmeeski.rabbitmqwrapper.RabbitQueue;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
            RabbitQueue queue = queues.declare(
                    "vacancies-to-notify", false, false, false, null
            );
            Consumer consumer = new PlainConsumer(connection, queue, new NotifyVacancyJob());
            consumer.startConsuming();
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

}
