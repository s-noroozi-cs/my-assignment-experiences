package com.chess.tour.impl;

import com.chess.tour.PieceTourSolution;
import com.chess.tour.model.Cell;
import com.chess.tour.model.Movement;
import com.chess.tour.model.PieceTourResponse;
import com.chess.tour.model.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WarnsdorffImpl implements PieceTourSolution {
    private final int BOARD_SIZE = 10;

    @Override
    public PieceTourResponse solve(Position currentPosition) {
        final Cell[][] board = new Cell[BOARD_SIZE][BOARD_SIZE];

        //Separating strategy of choosing next tile due to the checking all possible solutions.
        //This is good pattern to able separating algorithm decision section for other parts of it
        // But unfortunately all of the solution is not complete,
        // And I think could not solve the problem correctly.

        PieceTourResponse response = solve(board, currentPosition, getStrategyCost(board, (a, b) -> a < b));
//        response = solve(board, currentPosition, getStrategyNearEdge(board));
//        response = solve(board, currentPosition, getStrategyRandom(board));

        return response;
    }


    public PieceTourResponse solve(final Cell[][] board, Position currentPosition,
                                   Function<List<Position>, Optional<Position>> strategy) {

        for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
            for (int colIndex = 0; colIndex < board.length; colIndex++) {
                board[rowIndex][colIndex] = new Cell(new Position(rowIndex, colIndex));
            }
        }

        int visitedCount = 0;


        board[currentPosition.getRow()][currentPosition.getCol()].setVisitedOrder(visitedCount);
        visitedCount++;

        Optional<Position> nextPosition = findMinimumPathCost(board, currentPosition, strategy);
        while (nextPosition.isPresent()) {
            Position current = nextPosition.get();
            board[current.getRow()][current.getCol()].setVisitedOrder(visitedCount);
            visitedCount++;
            nextPosition = findMinimumPathCost(board, current, strategy);
        }

        boolean solved = visitedCount == (BOARD_SIZE * BOARD_SIZE);
        return new PieceTourResponse(board, solved);
    }

    private Optional<Position> findMinimumPathCost(Cell[][] board, Position currentPosition,
                                                   Function<List<Position>, Optional<Position>> strategy) {
        List<Position> positions = availablePositionsForMovement(board, currentPosition);
        return strategy.apply(positions);
    }

    private int calcPathCost(Cell[][] board, Position position) {
        return availablePositionsForMovement(board, position).size();
    }

    private List<Position> availablePositionsForMovement(Cell[][] board, Position current) {
        List<Position> availableMovement = new ArrayList<Position>();
        for (Movement movement : Movement.values()) {
            availableMovement.add(current.makeNewPosition(movement));
        }

        return availableMovement.stream()
                .filter(p -> p.getRow() >= 0 && p.getCol() >= 0)
                .filter(p -> p.getRow() < board.length && p.getCol() < board.length)
                .filter(p -> board[p.getRow()][p.getCol()].notVisitedYet())
                .collect(Collectors.toList());
    }

    private Function<List<Position>, Optional<Position>> getStrategyCost(
            Cell[][] board,
            BiFunction<Integer, Integer, Boolean> compare) {
        return positions -> {
            if (positions != null && positions.size() > 0) {
                if (positions.size() == 1)
                    return Optional.of(positions.get(0));
                else {
                    int minIndex = 0;
                    int minCost = calcPathCost(board, positions.get(0));

                    for (int i = 1; i < positions.size(); i++) {
                        int pathCost = calcPathCost(board, positions.get(i));
                        if (compare.apply(pathCost, minCost)) {
                            minCost = pathCost;
                            minIndex = i;
                        }
                    }
                    return Optional.of(positions.get(minIndex));
                }
            }
            return Optional.empty();
        };
    }

    private Function<List<Position>, Optional<Position>> getStrategyNearEdge(final Cell[][] board) {
        final Function<Position, Integer> calc = p -> p.getRow() * p.getCol();

        return positions -> {
            if (positions != null && positions.size() > 0) {
                if (positions.size() == 1)
                    return Optional.of(positions.get(0));
                else {
                    int minIndex = 0;
                    int minCost = calc.apply(positions.get(0));

                    for (int i = 1; i < positions.size(); i++) {
                        int pathCost = calc.apply(positions.get(i));
                        if (pathCost < minCost) {
                            minCost = pathCost;
                            minIndex = i;
                        }
                    }
                    return Optional.of(positions.get(minIndex));
                }
            }
            return Optional.empty();
        };
    }

    private Function<List<Position>, Optional<Position>> getStrategyRandom(final Cell[][] board) {
        return positions -> {
            if (positions != null && positions.size() > 0) {
                int index = (int) (Math.random() * positions.size());
                return Optional.of(positions.get(index));
            }
            return Optional.empty();
        };
    }
}