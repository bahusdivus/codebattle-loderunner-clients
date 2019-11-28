package ru.codebattle.client;

import lombok.extern.slf4j.Slf4j;
import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;

@Slf4j
class Player {

    LoderunnerAction decide(GameBoard gameBoard) {
        BoardPoint myPosition = gameBoard.getMyPosition();
        if (gameBoard.getElementAt(myPosition).equals(BoardElement.HERO_LADDER)
                && (gameBoard.getElementAt(myPosition.shiftTop()).equals(BoardElement.LADDER)
                || gameBoard.getElementAt(myPosition.shiftTop()).equals(BoardElement.NONE))) {
            return LoderunnerAction.GO_UP;
        } else {
            BoardPoint nextPosition = myPosition.shiftLeft(Direction.shift);
            if(isOtherPlayerHere(gameBoard, nextPosition)) {
                return Direction.drill;
            }
            if (gameBoard.getElementAt(nextPosition).equals(BoardElement.BRICK) ||
                    gameBoard.getElementAt(nextPosition).equals(BoardElement.UNDESTROYABLE_WALL)) {
                Direction.switchDirection();
                return Direction.action;
            } else {
                return Direction.action;
            }
        }
    }

    private boolean isOtherPlayerHere(GameBoard gameBoard, BoardPoint nextPosition) {
        BoardElement elementAt = gameBoard.getElementAt(nextPosition);
        switch (elementAt) {
            case OTHER_HERO_LADDER:
            case OTHER_HERO_LEFT:
            case OTHER_HERO_RIGHT:
            case OTHER_HERO_SHADOW_LADDER:
            case OTHER_HERO_SHADOW_LEFT:
            case OTHER_HERO_SHADOW_RIGHT:
                return true;
            default:
                return false;
        }
    }


    private static class Direction {
        static LoderunnerAction action = LoderunnerAction.GO_LEFT;
        static int shift = 1;
        static LoderunnerAction drill = LoderunnerAction.DRILL_LEFT;

        static void switchDirection() {
            if (action.equals(LoderunnerAction.GO_LEFT)) {
                action = LoderunnerAction.GO_RIGHT;
                shift = -1;
                drill = LoderunnerAction.DRILL_RIGHT;
            } else {
                action = LoderunnerAction.GO_LEFT;
                shift = 1;
                drill = LoderunnerAction.DRILL_LEFT;
            }
        }
    }
}
