package com.theopus.xengine.wrapper.utils;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class State<T> {
    final Consumer<T> onChange;
    final Predicate<T> predicate;
    final BiFunction<T, T, T> update;
    T t;

    public State(T value, Consumer<T> onChange, boolean init) {
        t = value;
        this.onChange = onChange;
        this.predicate = data -> data.equals(t);
        this.update = (was, upd) -> upd;
        if (init) {
            onChange.accept(t);
        }
    }

    public State(T value, Consumer<T> onChange, Predicate<T> predicate, boolean init) {
        t = value;
        this.predicate = predicate;
        this.onChange = onChange;
        this.update = (was, upd) -> upd;
        if (init) {
            onChange.accept(t);
        }
    }

    public State(T value, Consumer<T> onChange, BiFunction<T, T, T> update, boolean init) {
        t = value;
        this.predicate = data -> data.equals(t);
        this.onChange = onChange;
        this.update = (was, upd) -> upd;
        if (init) {
            onChange.accept(t);
        }
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

    public static State<Vector3f> v3(Vector3f value, Consumer<Vector3f> onChange) {
        return new State<>(value, onChange, (was, upd) -> {
            was.x = upd.x;
            was.y = upd.y;
            was.z = upd.z;
            return was;
        }, true);
    }

    public static State<Matrix4f> mat4(Matrix4f value, Consumer<Matrix4f> onChange) {
        return new State<Matrix4f>(value, onChange, (was, upd) -> {
            upd.get(was);
            return was;
        }, true);
    }

    public void update(T update) {
        if (!predicate.test(update)) {
            onChange.accept(update);
            t = this.update.apply(t, update);
        }
    }

    public T get() {
        return t;
    }
}
