package com.chess.tour;

import com.chess.tour.model.PieceTourResponse;
import com.chess.tour.model.Position;

public interface PieceTourSolution {
    int BOARD_SIZE = 10;

    PieceTourResponse solve(Position currentPosition);
}
