package ru.codebattle.client;

import lombok.extern.slf4j.Slf4j;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

import java.util.Iterator;
import java.util.List;

import static ru.codebattle.client.api.LoderunnerAction.DO_NOTHING;

@Slf4j
class Player {

    private Player() {
        //restrict creation only to fabric method
    }

    static Player getInstance() {
        return new Player();
    }

    LoderunnerAction decide(GameBoard gameBoard) {
        BoardPoint myPosition = gameBoard.getMyPosition();
        List<BoardPoint> goldPositions = gameBoard.getGoldPositions();
        BoardPoint nearestGold = getNearestPoint(myPosition, goldPositions);
        Direction.navigate(null, myPosition, nearestGold, gameBoard);
        while (Direction.action.equals(DO_NOTHING)) {
            goldPositions.remove(nearestGold);
            nearestGold = getNearestPoint(myPosition, goldPositions);
            Direction.navigate(null, myPosition, nearestGold, gameBoard);
        }
        return Direction.action;
    }

    private BoardPoint getNearestPoint(BoardPoint myPosition, List<BoardPoint> points) {
        if (points.isEmpty()) return null;
        Iterator<BoardPoint> iterator = points.iterator();
        BoardPoint nearest = iterator.next();
        while (iterator.hasNext()) {
            BoardPoint challenger = iterator.next();
            int nearestDiatance = Math.abs(myPosition.getX() - nearest.getX()) + Math.abs(myPosition.getY() - nearest.getY());
            int challengerDiatance = Math.abs(myPosition.getX() - challenger.getX()) + Math.abs(myPosition.getY() - challenger.getY());
            if (nearestDiatance > challengerDiatance) nearest = challenger;
        }
        return nearest;
    }

}
