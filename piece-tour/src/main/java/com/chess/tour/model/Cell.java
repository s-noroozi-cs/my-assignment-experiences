package com.chess.tour.model;

public class Cell {
    final Position position;
    int visitedOrder = -1;

    public Cell(Position position) {
        this.position = position;
    }

    public void setVisitedOrder(int visitedOrder) {
        this.visitedOrder = visitedOrder;
    }

    public int getVisitedOrder() {
        return visitedOrder;
    }

    public boolean notVisitedYet() {
        return visitedOrder == -1;
    }

    public Position getPosition() {
        return position;
    }
}
