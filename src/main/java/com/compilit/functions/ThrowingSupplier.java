package com.compilit.functions;

@FunctionalInterface
public interface ThrowingSupplier<T, E extends Exception> {
    /**
     * A supplier just like the native Java one, but specifically for handling logic that potentially throws checked exceptions
     * @return the result of the supplier
     * @throws E the potential checked exception
     */
    T get() throws E;
}