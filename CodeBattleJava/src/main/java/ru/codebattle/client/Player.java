package ru.codebattle.client;

import lombok.extern.slf4j.Slf4j;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import static ru.codebattle.client.api.LoderunnerAction.DO_NOTHING;
import static ru.codebattle.client.api.LoderunnerAction.GO_LEFT;
import static ru.codebattle.client.api.LoderunnerAction.GO_RIGHT;
import static ru.codebattle.client.api.LoderunnerAction.SUICIDE;

@Slf4j
class Player {

    private Player() {}
    private Deque<LoderunnerAction> loderunnerActions = null;
    private int doNothingCount = 0;
    private static final int MAX_ROUTE_LENGHT = 5;
    private static final int MAX_DO_NOTHING = 120;

    static Player getInstance() {
        return new Player();
    }

    LoderunnerAction decide(GameBoard gameBoard) {
        BoardPoint myPosition = gameBoard.getMyPosition();
        List<BoardPoint> goldPositions = gameBoard.getGoldPositions();
        BoardPoint nearestGold = getNearestPoint(myPosition, goldPositions);
        Direction direction = Direction.of(gameBoard);
        if (goldPositions.contains(myPosition.shiftLeft())) return GO_LEFT;
        if (goldPositions.contains(myPosition.shiftRight())) return GO_RIGHT;
        if (loderunnerActions == null || loderunnerActions.peek() == null) {
            loderunnerActions = direction.navigateRefined(null, myPosition, nearestGold);
            while ((loderunnerActions == null || loderunnerActions.peek() == null) && !goldPositions.isEmpty()) {
                goldPositions.remove(nearestGold);
                nearestGold = getNearestPoint(myPosition, goldPositions);
                loderunnerActions = direction.navigateRefined(null, myPosition, nearestGold);
            }
        }
        if (loderunnerActions == null || loderunnerActions.peek() == null) {
            if (doNothingCount++ <= MAX_DO_NOTHING) {
                return DO_NOTHING;
            }
            doNothingCount = 0;
            return SUICIDE;
        } else {
            doNothingCount = 0;
        }

        while (loderunnerActions.size() > MAX_ROUTE_LENGHT) {
            loderunnerActions.removeLast();
        }
        return loderunnerActions.poll();
    }

    private BoardPoint getNearestPoint(BoardPoint myPosition, List<BoardPoint> points) {
        if (points.isEmpty()) return null;
        Iterator<BoardPoint> iterator = points.iterator();
        BoardPoint nearest = iterator.next();
        while (iterator.hasNext()) {
            BoardPoint challenger = iterator.next();
            int nearestDistance = Math.abs(myPosition.getX() - nearest.getX()) + Math.abs(myPosition.getY() - nearest.getY());
            int challengerDistance = Math.abs(myPosition.getX() - challenger.getX()) + Math.abs(myPosition.getY() - challenger.getY());
            if (nearestDistance > challengerDistance) nearest = challenger;
        }
        return nearest;
    }

}
