package com.compilit.functions;

import java.util.function.Predicate;

/**
 * These functions are used to apply fuzzy matching to Strings. Meaning that the values need to
 * partially match conform the given percentage. It does this in two ways:
 * - It will check if there is a sequence of characters matching between the two values which
 * has a length more or equal to the desired percentage.
 * - And it will check if the total amount of matching characters is more or equal to
 * the desired percentage.
 */
public final class FuzzyMatchers {

  static final float MAX_PERCENTAGE = 100;
  static final float DEFAULT_MATCHING_PERCENTAGE = 80;

  private FuzzyMatchers() {}

  /**
   * Check each char in both Strings, if they don't match on the same position, decrease matching
   *
   * @param otherValue the second value to try to match
   * @return a Predicate that returns true if both values match for at least 80% (the default
   * matching percentage)
   */
  public static Predicate<String> fuzzyMatches(String otherValue) {
    return value -> fuzzyMatches(value, otherValue);
  }

  /**
   * Check each char in both Strings, if they don't match on the same position, decrease matching
   *
   * @param otherValue         the second value to try to match
   * @param matchingPercentage the desired matching percentage between the two values
   * @return a Predicate that returns true if both values match for at least the given matching
   * percentage
   */
  public static Predicate<String> fuzzyMatches(String otherValue, float matchingPercentage) {
    return value -> fuzzyMatches(value, otherValue, matchingPercentage);
  }

  /**
   * Check each char in both Strings, if they don't match on the same position, decrease matching
   *
   * @param value      the first value to try to match
   * @param otherValue the second value to try to match
   * @return true if both values match for at least 80% (the default matching percentage)
   */
  public static boolean fuzzyMatches(String value, String otherValue) {
    return fuzzyMatches(value, otherValue, DEFAULT_MATCHING_PERCENTAGE);
  }

  /**
   * Check each char in both Strings, if they don't match on the same position, decrease matching
   *
   * @param value              the first value to try to match
   * @param otherValue         the second value to try to match
   * @param matchingPercentage the desired matching percentage between the two values
   * @return true if both values match for at least the given matching percentage
   */
  public static boolean fuzzyMatches(
    String value,
    String otherValue,
    float matchingPercentage
  ) {
    validateInput(value, otherValue, matchingPercentage);
    float lengthMatchPercentage = getLengthMatchPercentage(value, otherValue);
    float charMatchPercentage = getCharMatchPercentage(value, otherValue);
    float charSequenceMatchPercentage = getCharSequenceMatchPercentage(value, otherValue);
    return charSequenceMatchPercentage >= matchingPercentage
      && charMatchPercentage >= matchingPercentage
      && lengthMatchPercentage >= matchingPercentage;
  }

  private static void validateInput(
    String value,
    String otherValue,
    float matchingPercentage
  ) {
    if (value == null || otherValue == null) {
      throw new MatcherInputException("Cannot match null values");
    }
    if (matchingPercentage > MAX_PERCENTAGE) {
      throw new MatcherInputException("Matching percentage cannot exceed 100");
    }
    if (value.isEmpty() || otherValue.isEmpty()) {
      throw new MatcherInputException("Cannot match empty values");
    }
  }

  /**
   * @param value the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of the matching sequences between the two Strings
   */
  public static float getCharSequenceMatchPercentage(
    String value,
    String otherValue
  ) {
    float shortestValueLength = Math.min(value.length(), otherValue.length());
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float charSequenceMatchPercentage = MAX_PERCENTAGE;
    var currentSequence = new StringBuilder();
    for (int index = 0; index < shortestValueLength; index++) {
      if (!value.contains(currentSequence)) {
        charSequenceMatchPercentage -= percentageDivider;
        currentSequence = new StringBuilder();
      } else {
        currentSequence.append(otherValue.charAt(index));
      }
    }
    return charSequenceMatchPercentage;
  }

  /**
   * @param value the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of characters matching between the two Strings
   */
  public static float getCharMatchPercentage(String value, String otherValue) {
    float shortestValueLength = Math.min(value.length(), otherValue.length());
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float charMatchPercentage = MAX_PERCENTAGE;
    for (int index = 0; index < shortestValueLength; index++) {
      if (!value.contains(String.valueOf(otherValue.charAt(index)))) {
        charMatchPercentage -= percentageDivider;
      }
    }
    return charMatchPercentage;
  }

  /**
   * @param value the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of the matching length between the two Strings
   */
  public static float getLengthMatchPercentage(
    String value,
    String otherValue
  ) {;
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float lengthMatchPercentage = MAX_PERCENTAGE;
    if (value.length() > otherValue.length()) {
      var difference = subtract(value, otherValue);
      lengthMatchPercentage -= multiply(percentageDivider, difference);
    } else if(otherValue.length() > value.length()) {
      var difference = subtract(otherValue, value);
      lengthMatchPercentage -= multiply(percentageDivider, difference);
    }
    return lengthMatchPercentage;
  }

  private static float multiply(float percentageDivider, int difference) {
    return difference * percentageDivider;
  }

  private static int subtract(String x, String y) {
    return x.length() - y.length();
  }

}
