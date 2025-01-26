package dev.pavatus.stargate.api;

public interface Disposable {

    default void age() {
    }

    void dispose();

    default boolean isAged() {
        return false;
    }
}
