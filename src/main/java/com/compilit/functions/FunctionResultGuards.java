package com.compilit.functions;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * These functions all transform possible throwing operations into a definite result without throwing any exception.
 */
public final class FunctionResultGuards {

    private FunctionResultGuards() {}

    /**
     * @param supplier a potentially throwing supplier
     * @param <T>      the return type
     * @return either the result of the supplier or null
     */
    public static <T> T orNull(Supplier<T> supplier) {
        return orDefault(supplier, null);
    }

    /**
     * @param throwingSupplier a potentially checked exception throwing supplier
     * @param <T>              the return type
     * @param <E>              the checked exception
     * @return either the wanted value as a String or the given default value
     */
    public static <T, E extends Exception> T orNullOnException(ThrowingSupplier<T, E> throwingSupplier) {
        return orDefaultOnException(throwingSupplier, null);
    }

    /**
     * @param function a potentially throwing supplier
     * @param <I>      the input type
     * @param <O>      the return type
     * @return either the result of the supplier value or null
     */
    public static <I, O> O orNull(Function<I, O> function, I value) {
        return orDefault(function, value, null);
    }

    /**
     * @param function a potentially throwing supplier
     * @param <I>      the input type
     * @param <O>      the return type
     * @return either the result of the supplier value or null
     */
    public static <I, O, E extends Exception> O orNullOnException(
        ThrowingFunction<I, O, E> function,
        I value
    ) {
        return orDefaultOnException(function, value, null);
    }


    /**
     * @param supplier     a potentially throwing supplier
     * @param defaultValue the default value to return in case of an exception
     * @param <T>          the return type
     * @return either the result of the supplier or the given default value
     */
    public static <T> T orDefault(
        Supplier<T> supplier,
        T defaultValue
    ) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param supplier     a potentially throwing supplier
     * @param defaultValue the default value to return in case of an exception
     * @param <T>          the return type
     * @return either the result of the supplier or the given default value
     */
    public static <T, E extends Exception> T orDefaultOnException(
        ThrowingSupplier<T, E> supplier,
        T defaultValue
    ) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param function a potentially throwing function
     * @param <O>      the return type
     * @return either result of the function or the given default value
     */
    public static <I, O, E extends Exception> O orDefaultOnException(
        ThrowingFunction<I, O, E> function,
        I value,
        O defaultValue
    ) {
        try {
            return function.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * @param function a potentially throwing function
     * @param <O>      the return type
     * @return either result of the function or the given default value
     */
    public static <I, O> O orDefault(
        Function<I, O> function,
        I value,
        O defaultValue
    ) {
        try {
            return function.apply(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * @param supplier a potentially throwing supplier
     * @param <T>      the return type
     * @return either the result of the supplier as a String or null
     */
    public static <T> String asStringOrNull(Supplier<T> supplier) {
        return asStringOrDefault(supplier, null);
    }

    /**
     * @param supplier a potentially throwing supplier
     * @param <T>      the return type
     * @param defaultValue the default value you with to return in case of an exception
     * @return either the result of the supplier as a String or the given default value
     */
    public static <T> String asStringOrDefault(Supplier<T> supplier, String defaultValue) {
        return orDefault((() -> supplier.get().toString()), defaultValue);
    }

    /**
     * This function gives you the ability to ignore a checked exception throwing function from
     * within a functional context. It is mainly meant to be using in combination with one of the
     * above functions, since it will return a null value in case of an exception.
     *
     * @param throwingSupplier a potentially checked exception throwing supplier
     * @param <T>              the return type
     * @param <E>              the checked exception
     * @return either the wanted value as a String or the given default value
     */
    public static <T, E extends Exception> Supplier<T> onCheckedException(ThrowingSupplier<T, E> throwingSupplier) {
        return () -> {
            try {
                return throwingSupplier.get();
            } catch (Exception ignored) {
                return null;
            }
        };
    }

    /**
     * Run the runnable without throwing an exception
     * Wraps around checked exception-throwing functions
     *
     * @param runnable         the potentially throwing runnable
     * @param exceptionHandler the handler to handle the potential exception
     */
    public static void orHandleException(Runnable runnable, Consumer<Exception> exceptionHandler) {

        try {
            runnable.run();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }

    }

    /**
     * Run the runnable without throwing an exception
     * Wraps around checked exception-throwing functions
     *
     * @param throwingRunnable the potentially throwing runnable
     * @param exceptionHandler the handler to handle the potential exception
     * @param <E>              the specific checked exception type
     */
    public static <E extends Exception> void orHandleCheckedException(
        ThrowingRunnable<E> throwingRunnable,
        Consumer<Exception> exceptionHandler
    ) {
        try {
            throwingRunnable.run();
        } catch (Exception e) {
            exceptionHandler.accept(e);
        }
    }

}
