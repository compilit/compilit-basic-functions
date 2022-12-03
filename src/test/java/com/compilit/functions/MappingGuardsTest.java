package com.compilit.functions;

import static com.compilit.functions.MappingGuards.asStringOrDefault;
import static com.compilit.functions.MappingGuards.asStringOrNull;
import static com.compilit.functions.MappingGuards.onCheckedException;
import static com.compilit.functions.MappingGuards.orDefaultOnException;
import static com.compilit.functions.MappingGuards.orHandleCheckedException;
import static com.compilit.functions.MappingGuards.orHandleException;
import static com.compilit.functions.MappingGuards.orNullOnException;
import static com.compilit.functions.MappingGuards.orDefault;
import static com.compilit.functions.MappingGuards.orNull;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

public class MappingGuardsTest {

    private static final String TEST_VALUE = "test";
    private static final String DEFAULT_TEST_VALUE = "default";
    @Test
    void orElseNull_noException_shouldReturnValue() {
        assertThat(orNull(() -> TEST_VALUE)).isEqualTo(TEST_VALUE);
    }

    @Test
    void orElseNull_exception_shouldReturnNull() {
        assertThat(orNull(() -> runtimeExceptionThrowingMethod())).isNull();
    }

    @Test
    void orElseNull_checkedException_shouldReturnNull() {
        assertThat(orNull(onCheckedException(() -> checkedExceptionThrowingMethod()))).isNull();
    }

    @Test
    void orElseDefault_noException_shouldReturnValue() {
        assertThat(orDefault(() -> TEST_VALUE, DEFAULT_TEST_VALUE)).isEqualTo(TEST_VALUE);
    }

    @Test
    void orElseDefault_exception_shouldReturnDefaultValue() {
        assertThat(orDefault(() -> runtimeExceptionThrowingMethod(), DEFAULT_TEST_VALUE)).isEqualTo(DEFAULT_TEST_VALUE);
    }

    @Test
    void asStringOrElseNull_shouldReturnStringValue() {
        assertThat(asStringOrNull(() -> 1)).isEqualTo("1");
    }

    @Test
    void asStringOrElseNull_exception_shouldReturnNull() {
        assertThat(asStringOrNull(MappingGuardsTest::runtimeExceptionThrowingMethod)).isNull();
    }

    @Test
    void asStringOrElseDefault_shouldReturnStringValue() {
        assertThat(asStringOrDefault(() -> 1, DEFAULT_TEST_VALUE)).isEqualTo("1");
    }

    @Test
    void asStringOrElseDefault_exception_shouldReturnDefault() {
        assertThat(asStringOrDefault(MappingGuardsTest::runtimeExceptionThrowingMethod, DEFAULT_TEST_VALUE)).isEqualTo(DEFAULT_TEST_VALUE);
    }

    @Test
    void orNull_nonThrowingFunction_shouldReturnResult() {
        var result = orNull(String::valueOf, 1);
        assertThat(result).isEqualTo("1");
    }

    @Test
    void orNull_throwingFunction_shouldReturnNull() {
        var result = orNull(x -> {throw new RuntimeException();}, 1);
        assertThat(result).isNull();
    }

    @Test
    void orNullOnException_nonThrowingFunction_shouldReturnResult() {
        var result = orNullOnException((ThrowingSupplier<? extends Object, ? extends Exception>) () -> String.valueOf(1));
        assertThat(result).isEqualTo("1");
    }

    @Test
    void orNullOnException_throwingFunction_shouldReturnNull() {
        var result = orNullOnException(MappingGuardsTest::checkedExceptionThrowingMethod);
        assertThat(result).isNull();
    }

    @Test
    void orDefault_nonThrowingFunction_shouldReturnResult() {
        assertThat(orDefault(String::valueOf, 1, "-1")).isEqualTo("1");

    }

    @Test
    void orDefault_throwingFunction_shouldReturnDefault() {
        assertThat(orDefault(x -> {throw new RuntimeException();}, 1, "-1")).isEqualTo("-1");
    }

    @Test
    void orDefaultOnException_nonThrowingFunction_shouldReturnResult() {
        var result = orDefaultOnException(MappingGuardsTest::runtimeExceptionThrowingMethod, "1");
        assertThat(result).isEqualTo("1");
    }

    @Test
    void orDefaultOnException_throwingFunction_shouldReturnNull() {
        var result = orDefaultOnException(MappingGuardsTest::checkedExceptionThrowingMethod, null);
        assertThat(result).isNull();
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
