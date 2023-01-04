package com.compilit.functions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * These functions all transform possible throwing operations into 'safe' ones which will not throw any exception.
 */
public final class FunctionGuards {
    private FunctionGuards() {}

    /**
     * @param supplier a potentially throwing supplier
     * @param <T>      the return type
     * @return either the result of the supplier or null
     */
    public static <T> Supplier<T> orNull(Supplier<T> supplier) {
        return orDefault(supplier, null);
    }

    /**
     * @param throwingSupplier a potentially checked exception throwing supplier
     * @param <T>              the return type
     * @param <E>              the checked exception
     * @return either the wanted value as a String or the given default value
     */
    public static <T, E extends Exception> Supplier<T> orNullOnException(ThrowingSupplier<T, E> throwingSupplier) {
        return orDefaultOnException(throwingSupplier, null);
    }

    /**
     * @param function a potentially throwing supplier
     * @param <I>      the input type
     * @param <O>      the return type
     * @return either the result of the supplier value or null
     */
    public static <I, O> Function<I, O> orNull(Function<I, O> function) {
        return orDefault(function, null);
    }

    /**
     * @param function a potentially throwing supplier
     * @param <I>      the input type
     * @param <O>      the return type
     * @return either the result of the supplier value or null
     */
    public static <I, O, E extends Exception> Function<I, O> orNullOnException(ThrowingFunction<I, O, E> function) {
        return orDefaultOnException(function, null);
    }


    /**
     * @param supplier     a potentially throwing supplier
     * @param defaultValue the default value to return in case of an exception
     * @param <T>          the return type
     * @return either the result of the supplier or the given default value
     */
    public static <T> Supplier<T> orDefault(
        Supplier<T> supplier,
        T defaultValue
    ) { return () -> {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    };
    }

    /**
     * @param supplier     a potentially throwing supplier
     * @param defaultValue the default value to return in case of an exception
     * @param <T>          the return type
     * @return either the result of the supplier or the given default value
     */
    public static <T, E extends Exception> Supplier<T> orDefaultOnException(
        ThrowingSupplier<T, E> supplier,
        T defaultValue
    ) {
        return () -> {
            try {
                return supplier.get();
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }

    /**
     * @param function a potentially throwing function
     * @param <O>      the return type
     * @return either result of the function or the given default value
     */
    public static <I, O, E extends Exception> Function<I, O> orDefaultOnException(
        ThrowingFunction<I, O, E> function,
        O defaultValue
    ) {
        return input -> {
            try {
                return function.apply(input);
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }


    /**
     * @param function a potentially throwing function
     * @param <O>      the return type
     * @return either result of the function or the given default value
     */
    public static <I, O> Function<I, O> orDefault(
        Function<I, O> function,
        O defaultValue
    ) {
        return input -> {
            try {
                return function.apply(input);
            } catch (Exception e) {
                return defaultValue;
            }
        };
    }

    /**
     * Take the potentially throwing supplier and in case an exception takes place, transform the exception into a runtime exception
     *
     * @param throwingSupplier a potentially checked exception throwing supplier
     * @param <T>              the return type
     * @param <E>              the checked exception
     * @return a non-checked-exception-throwing supplier. Runtime exception can still take place
     */
    public static <T, E extends Exception> Supplier<T> orRuntimeException(ThrowingSupplier<T, E> throwingSupplier) {
        return () -> {
            try {
                return throwingSupplier.get();
            } catch (Exception checkedException) {
                throw new RuntimeException(checkedException);
            }
        };
    }

    /**
     * Returns a 'safe' runnable which will always run without throwing an exception
     * @param runnable the potentially throwing runnable
     * @param exceptionHandler the handler to handle the potential exception
     * @return a runnable
     */
    public static Runnable orHandleException(Runnable runnable, Consumer<Exception> exceptionHandler) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        };
    }

    /**
     * Returns a 'safe' runnable which will always run without throwing an exception
     * Wraps around checked exception-throwing functions
     * @param throwingRunnable the potentially throwing runnable
     * @param exceptionHandler the handler to handle the potential exception
     * @param <E> the specific checked exception type
     * @return a runnable
     */
    public static <E extends Exception> Runnable orHandleCheckedException(ThrowingRunnable<E> throwingRunnable, Consumer<Exception> exceptionHandler) {
        return () -> {
            try {
                throwingRunnable.run();
            } catch (Exception e) {
                exceptionHandler.accept(e);
            }
        };
    }
}
