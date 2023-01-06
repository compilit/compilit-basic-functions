package com.compilit.functions;

import static com.compilit.functions.FunctionGuards.orDefault;
import static com.compilit.functions.FunctionGuards.orDefaultOnException;
import static com.compilit.functions.FunctionGuards.orHandleCheckedException;
import static com.compilit.functions.FunctionGuards.orHandleException;
import static com.compilit.functions.FunctionGuards.orNull;
import static com.compilit.functions.FunctionGuards.orNullOnException;
import static com.compilit.functions.FunctionGuards.orRuntimeException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

public class FunctionGuardsTests {

    private static final String TEST_VALUE = "test";
    private static final String DEFAULT_TEST_VALUE = "default";
    @Test
    void orNull_noException_shouldReturnValue() {
        assertThat(orNull(() -> TEST_VALUE).get()).isEqualTo(TEST_VALUE);
    }

    @Test
    void orNull_exception_shouldReturnNull() {
        assertThat(orNull(FunctionGuardsTests::runtimeExceptionThrowingMethod).get()).isNull();
    }

    @Test
    void orNull_checkedException_shouldReturnNull() {
        assertThat(orNull(orRuntimeException(FunctionGuardsTests::checkedExceptionThrowingMethod)).get()).isNull();
    }

    @Test
    void orRuntimeException_checkedException_shouldThrowException() {
        assertThatThrownBy(() -> orRuntimeException(FunctionGuardsTests::checkedExceptionThrowingMethod).get())
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void orDefault_noException_shouldReturnValue() {
        assertThat(orDefault(() -> TEST_VALUE, DEFAULT_TEST_VALUE).get()).isEqualTo(TEST_VALUE);
    }

    @Test
    void orDefault_exception_shouldReturnDefaultValue() {
        assertThat(orDefault(FunctionGuardsTests::runtimeExceptionThrowingMethod, DEFAULT_TEST_VALUE).get()).isEqualTo(DEFAULT_TEST_VALUE);
    }

    @Test
    void orNull_nonThrowingFunction_shouldReturnFunction() {
        var function = orNull(String::valueOf);
        assertThat(function.apply(1L)).isEqualTo("1");
    }

    @Test
    void orNull_throwingFunction_shouldReturnNull() {
        var function = orNull(x -> {throw new RuntimeException();});
        assertThat(function.apply(1)).isNull();
    }

    @Test
    void orNullOnException_nonThrowingFunction_shouldReturnFunction() {
        var function = orNullOnException((ThrowingSupplier<?, ? extends Exception>) () -> String.valueOf(1));
        assertThat(function.get()).isEqualTo("1");
    }

    @Test
    void orNullOnException_throwingFunction_shouldReturnNull() {
        var function = orNullOnException(FunctionGuardsTests::checkedExceptionThrowingMethod);
        assertThat(function.get()).isNull();
    }

    @Test
    void orDefault_nonThrowingFunction_shouldReturnFunction() {
        var function = orDefault(String::valueOf, "-1");
        assertThat(function.apply(1)).isEqualTo("1");

    }

    @Test
    void orDefault_throwingFunction_shouldReturnDefault() {
        var function = orDefault(x -> {throw new RuntimeException();}, "-1");
        assertThat(function.apply(1)).isEqualTo("-1");
    }

    @Test
    void orDefaultOnException_nonThrowingFunction_shouldReturnFunction() {
        var function = orDefaultOnException(FunctionGuardsTests::runtimeExceptionThrowingMethod, "1");
        assertThat(function.get()).isEqualTo("1");
    }

    @Test
    void orDefaultOnException_throwingFunction_shouldReturnNull() {
        var function = orDefaultOnException(FunctionGuardsTests::checkedExceptionThrowingMethod, null);
        assertThat(function.get()).isNull();
    }

    @Test
    void orHandleException_nonThrowingFunction_shouldNotHandleException() {
        AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
        exceptionHandled.set(false);
        orHandleException(() -> System.out.println(), x -> exceptionHandled.set(true)).run();
        assertThat(exceptionHandled.get()).isFalse();
    }

    @Test
    void orHandleException_throwingFunction_shouldHandleException() {
        AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
        exceptionHandled.set(false);
        orHandleException(() -> {throw new RuntimeException();}, x -> exceptionHandled.set(true)).run();
        assertThat(exceptionHandled.get()).isTrue();
    }

    @Test
    void orHandleCheckedException_nonThrowingFunction_shouldNotHandleException() {
        AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
        exceptionHandled.set(false);
        orHandleCheckedException(() -> System.out.println(), x -> exceptionHandled.set(true)).run();
        assertThat(exceptionHandled.get()).isFalse();
    }

    @Test
    void orHandleCheckedException_throwingFunction_shouldHandleException() {
        AtomicReference<Boolean> exceptionHandled = new AtomicReference<>();
        exceptionHandled.set(false);
        orHandleCheckedException(() -> {throw new Exception();}, x -> exceptionHandled.set(true)).run();
        assertThat(exceptionHandled.get()).isTrue();
    }


    private static String runtimeExceptionThrowingMethod() {
        throw new RuntimeException();
    }

    private static String checkedExceptionThrowingMethod() throws Exception {
            throw new Exception();
    }

}
