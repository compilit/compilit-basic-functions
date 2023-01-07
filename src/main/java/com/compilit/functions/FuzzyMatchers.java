package com.compilit.functions;

import java.util.function.Predicate;

/**
 * These functions are used to apply fuzzy matching to Strings. Meaning that the values need to
 * partially match conform the given percentage. It does this in three ways:
 * 1. It will check if there are sequences of characters matching between the two values.
 * 2. It will check if the total amount of matching characters.
 * 3. It will check the length of both values.
 * All of these checks have to match for at least the given match percentage,
 * or 80%, which is the default.
 */
public final class FuzzyMatchers {

  static final float MAX_PERCENTAGE = 100;
  static final float DEFAULT_MATCHING_PERCENTAGE = 80;

  private FuzzyMatchers() {}

  /**
   * Case-sensitive fuzzy matching
   *
   * @param otherValue the second value to try to match
   * @return a Predicate that returns true if both values match for at least 80% (the default
   * matching percentage)
   */
  public static Predicate<String> fuzzyMatches(String otherValue) {
    return value -> fuzzyMatches(value, otherValue);
  }

  /**
   * Case-insensitive fuzzy matching
   *
   * @param otherValue the second value to try to match
   * @return a Predicate that returns true if both values match for at least 80% (the default
   * matching percentage)
   */
  public static Predicate<String> fuzzyMatchesIgnoreCase(String otherValue) {
    return value -> fuzzyMatchesIgnoreCase(value, otherValue);
  }

  /**
   * Case-sensitive fuzzy matching
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
   * Case-sensitive fuzzy matching
   *
   * @param value      the first value to try to match
   * @param otherValue the second value to try to match
   * @return true if both values match for at least 80% (the default matching percentage)
   */
  public static boolean fuzzyMatches(String value, String otherValue) {
    return fuzzyMatches(value, otherValue, DEFAULT_MATCHING_PERCENTAGE);
  }


  /**
   * Case-insensitive fuzzy matching
   *
   * @param value      the first value to try to match
   * @param otherValue the second value to try to match
   * @return true if both values match for at least 80% (the default matching percentage)
   */
  public static boolean fuzzyMatchesIgnoreCase(String value, String otherValue) {
    return fuzzyMatchesIgnoreCase(value, otherValue, DEFAULT_MATCHING_PERCENTAGE);
  }

  /**
   * Case-insensitive fuzzy matching
   *
   * @param value              the first value to try to match
   * @param otherValue         the second value to try to match
   * @param matchingPercentage the desired matching percentage between the two values
   * @return true if both values match for at least the given matching percentage
   */
  public static boolean fuzzyMatchesIgnoreCase(
    String value,
    String otherValue,
    float matchingPercentage
  ) {
    if (bothValuesAreNullOrEmpty(value, otherValue))
      return true;
    if (oneValueIsNullOrEmpty(value, otherValue))
      return false;
    value = value.toLowerCase();
    otherValue = otherValue.toLowerCase();
    return fuzzyMatches(value, otherValue, matchingPercentage);
  }

  /**
   * Case-sensitive fuzzy matching
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
    if (matchingPercentage > MAX_PERCENTAGE) {
      throw new MatcherInputException("Matching percentage cannot exceed 100");
    }
    if (bothValuesAreNullOrEmpty(value, otherValue))
      return true;
    if (oneValueIsNullOrEmpty(value, otherValue))
      return false;
    float lengthMatchPercentage = getLengthMatchPercentage(value, otherValue);
    float charMatchPercentage = getCharMatchPercentage(value, otherValue);
    float charSequenceMatchPercentage = getCharSequenceMatchPercentage(value, otherValue);
    return charSequenceMatchPercentage >= matchingPercentage
      && charMatchPercentage >= matchingPercentage
      && lengthMatchPercentage >= matchingPercentage;
  }

  private static boolean oneValueIsNullOrEmpty(String value, String otherValue) {
    return (value == null || otherValue == null)
      || (value.isEmpty() || otherValue.isEmpty());
  }

  private static boolean bothValuesAreNullOrEmpty(String value, String otherValue) {
    return (value == null && otherValue == null)
      || ((value != null && value.isEmpty()) && (otherValue != null && otherValue.isEmpty()));
  }

  /**
   * @param value      the first String you wish to compare to the other
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
   * @param value      the first String you wish to compare to the other
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
   * @param value      the first String you wish to compare to the other
   * @param otherValue the other String you wish to compare to the first
   * @return a float representing the percentage of the matching length between the two Strings
   */
  public static float getLengthMatchPercentage(
    String value,
    String otherValue
  ) {
    ;
    float longestValueLength = Math.max(value.length(), otherValue.length());
    float percentageDivider = MAX_PERCENTAGE / longestValueLength;
    float lengthMatchPercentage = MAX_PERCENTAGE;
    if (value.length() > otherValue.length()) {
      var difference = subtract(value, otherValue);
      lengthMatchPercentage -= multiply(percentageDivider, difference);
    } else if (otherValue.length() > value.length()) {
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
