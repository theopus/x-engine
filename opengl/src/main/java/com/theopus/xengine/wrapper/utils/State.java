package com.theopus.xengine.wrapper.utils;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class State<T> {
    private T t;
    private final Consumer<T> onChange;
    private final Predicate<T> predicate;

    public State(T value, Consumer<T> onChange, boolean init) {
        t = value;
        this.onChange = onChange;
        this.predicate = data -> data.equals(t);
        if (init) {
            onChange.accept(t);
        }
    }

    public State(T value, Consumer<T> onChange, Predicate<T> predicate, boolean init) {
        t = value;
        this.predicate = predicate;
        this.onChange = onChange;
        if (init) {
            onChange.accept(t);
        }
    }

    public void update(T update) {
        if (!predicate.test(update)) {
            onChange.accept(update);
            t = update;
        }
    }

    public T get() {
        return t;
    }

    public static <C> State<C> of(C value, Consumer<C> consumer, Predicate<C> predicate) {
        return new State<>(value, consumer, predicate, true);
    }

    public static <C> State<C> of(C value, Consumer<C> consumer) {
        return new State<>(value, consumer, true);
    }


    public static State<Boolean> bool(Boolean value, Consumer<Boolean> onTrue, Consumer<Boolean> onFalse) {
        Consumer<Boolean> tConsumer = b -> {
            if (b) onTrue.accept(b);
            else onFalse.accept(b);
        };
        return new State<>(value, tConsumer, true);
    }

    public static void main(String[] args) {
        State<Boolean> bool = State.bool(false, b -> System.out.println("true activated"), b -> System.out.println("false activated"));

    }
}
