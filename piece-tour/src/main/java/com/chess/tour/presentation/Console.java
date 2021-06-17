package com.chess.tour.presentation;

import com.chess.tour.PieceTourSolution;
import com.chess.tour.model.Cell;
import com.chess.tour.model.PieceTourResponse;
import com.chess.tour.model.Position;
import com.chess.tour.validation.TourValidation;

import java.util.Scanner;

import static com.chess.tour.PieceTourSolution.BOARD_SIZE;

public class Console {

    public Console(PieceTourSolution pieceTourSolution) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter start position row(first row is 0): ");
        int row = scanner.nextInt();
        System.out.print("Enter start position column(first column is 0): ");
        int col = scanner.nextInt();
        TourValidation.validatePosition(row, col, BOARD_SIZE).ifPresent(msg -> {
            throw new IllegalArgumentException(msg);
        });

        PieceTourResponse response = pieceTourSolution.solve(new Position(row, col));

        Cell[][] board = response.getBoard();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j].getVisitedOrder() + " ");
            }
            System.out.println();
        }
        if (!response.isSolved()) {
            System.err.printf("From this start position (%d,%d), could not find tour!\n", row, col);
        } else {
            System.out.printf("From this start position (%d,%d), found valid tour!\n", row, col);
        }
    }
}
