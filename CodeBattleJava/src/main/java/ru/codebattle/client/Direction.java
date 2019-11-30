package ru.codebattle.client;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import static ru.codebattle.client.api.BoardElement.BRICK;
import static ru.codebattle.client.api.BoardElement.LADDER;
import static ru.codebattle.client.api.BoardElement.NONE;
import static ru.codebattle.client.api.LoderunnerAction.DO_NOTHING;
import static ru.codebattle.client.api.LoderunnerAction.DRILL_LEFT;
import static ru.codebattle.client.api.LoderunnerAction.DRILL_RIGHT;
import static ru.codebattle.client.api.LoderunnerAction.GO_DOWN;
import static ru.codebattle.client.api.LoderunnerAction.GO_LEFT;
import static ru.codebattle.client.api.LoderunnerAction.GO_RIGHT;
import static ru.codebattle.client.api.LoderunnerAction.GO_UP;

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
        if (notValidDestination(destination)) {
            Directions diggableBriks = checkDiggableBriks(destination);
            if (from != Directions.DOWN || diggableBriks == null) {
                return null;
            }
        }

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

    private Directions checkDiggableBriks(BoardPoint destination) {
        if (destination == null) return null;
        if (board.hasElementAt(destination, BRICK)
                && board.hasElementAt(destination.shiftLeft(), BRICK)
                && board.hasElementAt(destination.shiftTop(), NONE)
                && (board.hasElementAt(destination.shiftLeft().shiftTop(), NONE)
                || board.hasElementAt(destination.shiftLeft().shiftTop(), LADDER))) {
            return Directions.RIGHT;
        } else if (board.hasElementAt(destination, BRICK)
                && board.hasElementAt(destination.shiftRight(), BRICK)
                && board.hasElementAt(destination.shiftTop(), NONE)
                && (board.hasElementAt(destination.shiftRight().shiftTop(), NONE)
                || board.hasElementAt(destination.shiftRight().shiftTop(), LADDER))) {
            return Directions.LEFT;
        }
        return null;
    }

    private Deque<LoderunnerAction> findFurtherWayRefined(Directions from, BoardPoint myPosition, BoardPoint destination) {
        Deque<LoderunnerAction> actions = null;
        Directions directionToDig = checkDiggableBriks(destination);
        if (helper.isSurface(destination.shiftBottom())
                || helper.isSurface(destination.shiftBottom().shiftLeft())
                || helper.isSurface(destination.shiftBottom().shiftRight())
                || helper.isPipe(destination)) {
            if (helper.isLadder(destination.shiftBottom()) && from != Directions.DOWN) {
                actions = navigateRefined(Directions.UP, myPosition, destination.shiftBottom());
            }
            if (actions == null && from != Directions.LEFT
                    && (helper.isSurface(destination.shiftBottom().shiftLeft())
                    || helper.isPipe(destination.shiftLeft()))) {
                actions = navigateRefined(Directions.RIGHT, myPosition, destination.shiftLeft());
            }
            if (actions == null && from != Directions.RIGHT
                    && (helper.isSurface(destination.shiftBottom().shiftRight())
                    || helper.isPipe(destination.shiftRight()))) {
                actions = navigateRefined(Directions.LEFT, myPosition, destination.shiftRight());
            }
            if (actions == null && from != Directions.UP) {
                actions = navigateRefined(Directions.DOWN, myPosition, destination.shiftTop());
            }
        } else if (directionToDig != null && from == Directions.DOWN) {
            if (directionToDig == Directions.RIGHT) {
                actions = navigateRefined(Directions.DOWN, myPosition, destination.shiftTop().shiftLeft());
                if (actions != null) {
                    actions.offer(DRILL_RIGHT);
                    actions.offer(GO_RIGHT);
                }
            } else {
                actions = navigateRefined(Directions.DOWN, myPosition, destination.shiftTop().shiftRight());
                if (actions != null) {
                    actions.offer(DRILL_LEFT);
                    actions.offer(GO_LEFT);
                }
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
