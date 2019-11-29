package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.*;

import static ru.codebattle.client.api.LoderunnerAction.*;

class Direction {
    private final Set<BoardPoint> visitedPoints = new HashSet<>();
    private BoardElementHelper helper;
    private final GameBoard board;

    private Direction(GameBoard board) {
        this.board = board;
        visitedPoints.addAll(board.getOtherHeroPositions());
        visitedPoints.addAll(board.getEnemyPositions());
    }

    static Direction of(GameBoard board) {
        return new Direction(board);
    }

    Deque<LoderunnerAction> navigateRefined(Directions from, BoardPoint myPosition, BoardPoint destination) {
        if (helper == null) helper = BoardElementHelper.of(board);
        if (notValidDestination(destination)) return null;

        if (myPosition.equals(destination)) {
            Deque<LoderunnerAction> actions = new ArrayDeque<>();
            actions.offer(goToDirection(from));
            return actions;
        }

        visitedPoints.add(destination);
        Deque<LoderunnerAction> actions = findFurtherWayRefined(from, myPosition, destination);
        if (from != null && actions != null) actions.offer(goToDirection(from));
        return actions;
    }

    private Deque<LoderunnerAction> findFurtherWayRefined(Directions from, BoardPoint myPosition, BoardPoint destination) {
        Deque<LoderunnerAction> actions = null;
        if (helper.isSurface(destination.shiftBottom())
                || helper.isSurface(destination.shiftBottom().shiftLeft())
                || helper.isSurface(destination.shiftBottom().shiftRight())
                || helper.isPipe(destination)) {
            if (helper.isLadder(destination.shiftBottom()) && from != Directions.DOWN) {
                actions = navigateRefined(Directions.UP, myPosition, destination.shiftBottom());
            }
            if (actions == null && from != Directions.LEFT
                    && helper.isSurface(destination.shiftBottom().shiftLeft())) {
                actions = navigateRefined(Directions.RIGHT, myPosition, destination.shiftLeft());
            }
            if (actions == null && from != Directions.RIGHT
                    && helper.isSurface(destination.shiftBottom().shiftRight())) {
                actions = navigateRefined(Directions.LEFT, myPosition, destination.shiftRight());
            }
            if (actions == null && from != Directions.UP) {
                actions = navigateRefined(Directions.DOWN, myPosition, destination.shiftTop());
            }
        } else {
            if (from != Directions.UP) {
                actions = navigateRefined(Directions.DOWN, myPosition, destination.shiftTop());
            }
        }

        return actions;
    }

    private boolean notValidDestination(BoardPoint destination) {
        return destination == null
                || visitedPoints.contains(destination)
                || helper.isWall(destination);
    }

    private LoderunnerAction goToDirection(Directions from) {
        switch (from) {
            case UP:
                return GO_UP;
            case DOWN:
                return GO_DOWN;
            case LEFT:
                return GO_LEFT;
            case RIGHT:
                return GO_RIGHT;
        }
        return DO_NOTHING;
    }

    private enum Directions {LEFT, RIGHT, DOWN, UP}
}
