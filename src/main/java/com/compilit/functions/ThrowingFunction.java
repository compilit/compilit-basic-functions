package com.compilit.functions;

@FunctionalInterface
public interface ThrowingFunction<I, O, E extends Exception> {

    /**
     * A function just like the native Java one, but specifically for handling logic that potentially throws checked exceptions
     * @param input the input of the logic
     * @return the result of the function
     * @throws E the potential checked exception
     */
    O apply(I input) throws E;
}
