package com.chess.tour.model;

public class Position {
    final int row, col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position makeNewPosition(Movement movement) {
        int newRow = this.row + movement.rowChange;
        int newCol = this.col + movement.colChange;
        return new Position(newRow, newCol);
    }

    @Override
    public String toString() {
        return "Position{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
}
