package com.chess.tour.model;

public enum Movement {
    NORTH(-3, 0),

    SOUTH(3, 0),

    EAST(0, 3),

    WEST(0, -3),

    NORTH_WEST(-2, -2),

    NORTH_EAST(-2, 2),

    SOUTH_WEST(2, -2),

    SOUTH_EAST(2, 2);

    final int rowChange, colChange;

    Movement(int rowChange, int colChange) {
        this.rowChange = rowChange;
        this.colChange = colChange;
    }
}

