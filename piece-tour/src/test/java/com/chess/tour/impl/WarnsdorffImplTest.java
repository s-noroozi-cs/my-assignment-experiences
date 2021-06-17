package com.chess.tour.impl;

import com.chess.tour.PieceTourSolution;
import com.chess.tour.model.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WarnsdorffImplTest {
    PieceTourSolution pieceTourSolution = new WarnsdorffImpl();

    @Test
    public void checkSolutionForRandom() {
        for (int i = 0; i < 100; i++) {
            int row = (int) (Math.random() * 10);
            int col = (int) (Math.random() * 10);
            Position position = new Position(row, col);
            boolean isSolved = pieceTourSolution.solve(position).isSolved();
            System.out.print("Iteration " + (i + 1) + ": ");
            if (isSolved) {
                System.out.println("Found a tour for " + position);
            } else {
                Assert.fail("Could not find a tour for " + position);
            }
        }
    }
}
