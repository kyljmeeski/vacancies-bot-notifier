package com.kyljmeeski.vacanciesbot.notifier;

import java.util.function.Consumer;

public class NotifyVacancyJob implements Consumer<String> {

    @Override
    public void accept(String s) {

    }

    @Override
    public Consumer<String> andThen(Consumer<? super String> after) {
        return Consumer.super.andThen(after);
    }

}
