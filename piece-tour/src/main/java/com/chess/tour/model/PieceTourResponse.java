package com.chess.tour.model;

public class PieceTourResponse {
    private final Cell[][] board;
    private final boolean solved;

    public PieceTourResponse(Cell[][] board, boolean solved) {
        this.board = board;
        this.solved = solved;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public Cell[] sortByOrder() {
        Cell[] cells = new Cell[board.length * board.length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getVisitedOrder() >= 0)
                    cells[board[i][j].getVisitedOrder()] = board[i][j];
            }
        }
        return cells;
    }

    public boolean isSolved() {
        return solved;
    }
}
