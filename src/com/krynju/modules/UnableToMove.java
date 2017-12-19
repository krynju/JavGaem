package com.krynju.modules;

/**
 * Exception thrown when the movement is not possible to make
 */
public class UnableToMove extends Exception {
    public UnableToMove(String message) {
        super(message);
    }
}
