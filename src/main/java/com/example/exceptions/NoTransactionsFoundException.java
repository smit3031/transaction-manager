package com.example.exceptions;

/**
 * Custom exception class to represent the scenario where no transactions are found.
 */
public class NoTransactionsFoundException extends RuntimeException {

    /**
     * Constructs a NoTransactionsFoundException with the specified detail message.
     *
     * @param message The detail message.
     */
    public NoTransactionsFoundException(String message) {
        super(message);
    }
}

