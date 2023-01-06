package com.compilit.functions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class FuzzyMatchersTests {

  @ParameterizedTest
  @MethodSource("validTestCasesDefaultMatchingPercentage")
  void fuzzyMatchesPredicate_matches_shouldReturnTrue(String value, String otherValue) {
    assertThat(FuzzyMatchers.fuzzyMatches(value).test(otherValue)).isTrue();
  }

  @ParameterizedTest
  @MethodSource("validTestCasesDefaultMatchingPercentageCaseInsensitive")
  void fuzzyMatchesIgnoreCasePredicate_matches_shouldReturnTrue(String value, String otherValue) {
    assertThat(FuzzyMatchers.fuzzyMatchesIgnoreCase(value).test(otherValue)).isTrue();
  }

  @ParameterizedTest
  @MethodSource("validTestCasesCustomMatchingPercentage")
  void fuzzyMatchesPredicate_matches_shouldReturnTrue(String value, String otherValue, float matchingPercentage) {
    assertThat(FuzzyMatchers.fuzzyMatches(value, matchingPercentage).test(otherValue)).isTrue();
  }
  @ParameterizedTest
  @MethodSource("validTestCasesGetLengthMatchingPercentage")
  void getLengthMatchPercentage_testCase_shouldReturnExpectedValue(String value, String otherValue, float matchingPercentage) {
    assertThat(FuzzyMatchers.getLengthMatchPercentage(value, otherValue)).isEqualTo(matchingPercentage);
  }

  @ParameterizedTest
  @MethodSource("validTestCasesGetCharMatchingPercentage")
  void getCharMatchPercentage_testCase_shouldReturnExpectedValue(String value, String otherValue, float matchingPercentage) {
    assertThat(FuzzyMatchers.getCharMatchPercentage(value, otherValue)).isEqualTo(matchingPercentage);
  }

  @ParameterizedTest
  @MethodSource("validTestCasesGetCharSequenceMatchingPercentage")
  void getCharSequenceMatchPercentage_testCase_shouldReturnExpectedValue(String value, String otherValue, float matchingPercentage) {
    assertThat(FuzzyMatchers.getCharSequenceMatchPercentage(value, otherValue)).isEqualTo(matchingPercentage);
  }

  @ParameterizedTest
  @MethodSource("invalidTestCases")
  void fuzzyMatchesPredicate_noMatch_shouldReturnFalse(String value, String otherValue) {
    assertThat(FuzzyMatchers.fuzzyMatches(value).test(otherValue)).isFalse();
  }

  @Test
  void fuzzyMatchesPredicate$50_matches50Percent_shouldReturnTrue() {
    assertThat(FuzzyMatchers.fuzzyMatches("1234567890", 50).test("12345")).isTrue();
  }

  @Test
  void fuzzyMatches$50_matches50Percent_shouldReturnTrue() {
    assertThat(FuzzyMatchers.fuzzyMatches("1234567890","12345", 50)).isTrue();
  }

  @Test
  void fuzzyMatches$500_shouldThrowException() {
    assertThatThrownBy(() -> FuzzyMatchers.fuzzyMatches("1234567890","12345", 500))
      .isInstanceOf(MatcherInputException.class);
  }

  private static Stream<Arguments> validTestCasesDefaultMatchingPercentage() {
    return Stream.of(
      Arguments.arguments("Test", "Test"),
      Arguments.arguments("Fropselationtaruks", "Frapselationtaroks"),
      Arguments.arguments("ropselationtaruksf", "frapselationtaroks"),
      Arguments.arguments("1234567890", "12345678"),
      Arguments.arguments("0123456789", "1234567890"),
      Arguments.arguments("Almost correct", "Almast correct")
    );
  }
  private static Stream<Arguments> validTestCasesDefaultMatchingPercentageCaseInsensitive() {
    return Stream.of(
      Arguments.arguments("test", "TEST"),
      Arguments.arguments("fropselationtaruks", "frapselationtaroks"),
      Arguments.arguments("ropselationtaruksf", "FRAPSELATIONTARASK"),
      Arguments.arguments("1234567890", "12345678"),
      Arguments.arguments("0123456789", "1234567890"),
      Arguments.arguments("ALMOST correct", "Almast correct")
    );
  }

  private static Stream<Arguments> validTestCasesCustomMatchingPercentage() {
    return Stream.of(
      Arguments.arguments("test", "testtest", 50f),
      Arguments.arguments("1234567890", "123456789", 90f),
      Arguments.arguments("test", "test", 100f),
      Arguments.arguments("1234567890", "12345678", 10f),
      Arguments.arguments("0123456789", "1234567890", 10f),
      Arguments.arguments("Almost correct", "Almast correct", 10f)
    );
  }

  private static Stream<Arguments> validTestCasesGetLengthMatchingPercentage() {
    return Stream.of(
      Arguments.arguments("test", "testtest", 50f),
      Arguments.arguments("1234567890", "123456789", 90f),
      Arguments.arguments("test", "test", 100f),
      Arguments.arguments("1234567890", "12345678", 80f),
      Arguments.arguments("0123456789", "1234567890", 100f),
      Arguments.arguments("Almost correct", "Almast correct", 100f)
    );
  }

  private static Stream<Arguments> validTestCasesGetCharSequenceMatchingPercentage() {
    return Stream.of(
      Arguments.arguments("test", "testtest", 100f),
      Arguments.arguments("1234567890", "123456789", 100f),
      Arguments.arguments("test", "test", 100f),
      Arguments.arguments("1234567890", "12345678", 100f),
      Arguments.arguments("0123456789", "1234567890", 100f),
      Arguments.arguments("Almost correct", "Almast correct", 92.85714f)
    );
  }
  private static Stream<Arguments> validTestCasesGetCharMatchingPercentage() {
    return Stream.of(
      Arguments.arguments("test", "testtest", 100f),
      Arguments.arguments("1234567890", "123456789", 100f),
      Arguments.arguments("1234", "test", 0f)
    );
  }

  private static Stream<Arguments> invalidTestCases() {
    return Stream.of(
      Arguments.arguments("1234567890","0928314756"),
      Arguments.arguments("b", "frapselationtaroks"),
      Arguments.arguments("z", "fsdlkjfnsldf"),
      Arguments.arguments("abc", "def"),
      Arguments.arguments("abcdef", "abcdefghijk"),
      Arguments.arguments("test",""),
      Arguments.arguments("", "test"),
      Arguments.arguments(null, "test"),
      Arguments.arguments("test", null)
    );
  }

}
