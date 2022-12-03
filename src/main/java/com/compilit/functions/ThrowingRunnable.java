package com.compilit.functions;

@FunctionalInterface
public interface ThrowingRunnable<E extends Exception> {
    /**
     * A runnable just like the native Java one, but specifically for handling logic that potentially throws checked exceptions
     * @throws E the potential checked exception
     */
    void run() throws E;
}
