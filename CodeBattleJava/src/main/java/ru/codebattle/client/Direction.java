package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.HashSet;
import java.util.Set;

import static ru.codebattle.client.api.LoderunnerAction.DO_NOTHING;
import static ru.codebattle.client.api.LoderunnerAction.GO_DOWN;
import static ru.codebattle.client.api.LoderunnerAction.GO_LEFT;
import static ru.codebattle.client.api.LoderunnerAction.GO_RIGHT;
import static ru.codebattle.client.api.LoderunnerAction.GO_UP;

class Direction {
    private static final Set<BoardPoint> visitedPoints = new HashSet<>();
    static LoderunnerAction action = DO_NOTHING;
    private static BoardElementHelper helper;

    private Direction() {
    }

    static void navigate(Directions from, BoardPoint myPosition, BoardPoint destination, GameBoard board) {
        if (helper == null) helper = BoardElementHelper.of(board);
        if (!isValidDestination(destination)) return;

        if (myPosition.equals(destination)) {
            action = goToDirection(from);
            visitedPoints.clear();
            return;
        }

        visitedPoints.add(destination);

        findFurtherWay(from, myPosition, destination, board);

    }

    private static void findFurtherWay(Directions from, BoardPoint myPosition, BoardPoint destination, GameBoard board) {
        if (helper.isSurface(destination.shiftBottom()) || helper.isPipe(destination)) {
            if (helper.isLadder(destination.shiftBottom()) && from != Directions.DOWN) {
                navigate(Directions.UP, myPosition, destination.shiftBottom(), board);
            }
            if (action.equals(DO_NOTHING) && from != Directions.LEFT) {
                navigate(Directions.RIGHT, myPosition, destination.shiftLeft(), board);
            }
            if (action.equals(DO_NOTHING) && from != Directions.RIGHT) {
                navigate(Directions.LEFT, myPosition, destination.shiftRight(), board);
            }
        }
        if (action.equals(DO_NOTHING) && from != Directions.UP) {
            navigate(Directions.DOWN, myPosition, destination.shiftTop(), board);
        }
    }

    private static boolean isValidDestination(BoardPoint destination) {
        return destination != null
                && !visitedPoints.contains(destination)
                && !helper.isWall(destination);
    }

    private static LoderunnerAction goToDirection(Directions from) {
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
