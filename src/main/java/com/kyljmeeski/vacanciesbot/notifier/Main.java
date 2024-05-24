package com.kyljmeeski.vacanciesbot.notifier;

import com.kyljmeeski.rabbitmqwrapper.*;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);

        String url = "jdbc:postgresql://localhost:5432/vacancies-bot";
        String user = "postgres";
        String password = "postgres";

        try {
            java.sql.Connection PostgreSQLConnection = DriverManager.getConnection(url, user, password);
            Users users = new Users(PostgreSQLConnection);
            try {
                Connection rabbitMQConnection = factory.newConnection();
                Queues queues = new Queues(rabbitMQConnection);
                RabbitQueue queue = queues.declare(
                        "vacancies-to-notify", false, false, false, null
                );
                Exchanges exchanges = new Exchanges(rabbitMQConnection);
                RabbitExchange exchange = exchanges.declare("vacancies", "direct");
                queues.declare(
                        "telegram-messages", false, false, false, null
                ).bind(exchange, "telegram-messages");
                Producer producer = new PlainProducer(rabbitMQConnection, exchange, "telegram-messages");
                Consumer consumer = new PlainConsumer(rabbitMQConnection, queue, new NotifyVacancyJob(users, producer));
                consumer.startConsuming();
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
