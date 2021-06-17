package com.chess.tour.validation;

import java.util.Optional;

public class TourValidation {

    public static Optional<String> validatePosition(int row, int col, int boardSize) {
        if (row < 0 || row >= boardSize || col < 0 || col >= boardSize)
            return Optional.of("position (row and col) is not valid. " +
                    "valid range between 0-" + (boardSize-1) + " for both row and column arguments.");

        return Optional.empty();
    }

    public static Optional<String> validateInteger(String number) {
        try {
            Integer.parseInt(number);
            return Optional.empty();
        } catch (Throwable ex) {
            return Optional.of(ex.getMessage());
        }
    }
}
