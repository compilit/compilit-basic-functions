package com.compilit.functions;

/**
 * The general exception which will be thrown for every invalid input
 */
public final class MatcherInputException extends RuntimeException {
  MatcherInputException(String message) {
    super(message);
  }
}
