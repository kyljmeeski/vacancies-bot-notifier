[![Java](https://img.shields.io/badge/Java-8%2B-orange)](https://www.oracle.com/java/)
![Lines of Code](https://img.shields.io/badge/lines_of_code-507-green)
![License](https://img.shields.io/badge/license-MIT-blue)

**Notifier for Vacancies Bot** s a component of the Vacancies Bot application built on a microservices architecture. It reads vacancies sent by [Saver](https://github.com/kyljmeeski/vacancies-bot-saver), reads subscribed users from PostgreSQL DB and sends message about users and vacancies to the queue "telegram-messages";

## Requirements
- Java 8 or higher
- RabbitMQ and PostgreSQL server running locally or accessible via network
- Maven for building the project

## Installation
1. Clone the repository:
```shell
git clone https://github.com/kyljmeeksi/vacancies-bot-notifier.git
```
2. Navigate to the project directory:
```shell
cd vacancies-bot-notifier 
```
3. Build the project using Maven:
```shell
mvn clean install
```

## Configuration
Modify the following parameters in Main.java to match your RabbitMQ and PostgreSQL setup:
```java
factory.setHost("localhost");
factory.setPort(5672);

String url = "jdbc:postgresql://localhost:5432/vacancies-bot";
String user = "postgres";
String password = "postgres";
```
Ensure RabbitMQ and PostgreSQL servers are configured correctly.

## Usage
1. Run the application:
```shell
java -jar vacancies-bot-notifier.jar
```

