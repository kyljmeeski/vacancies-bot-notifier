/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Amir Syrgabaev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
