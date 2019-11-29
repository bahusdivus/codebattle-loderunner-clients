package ru.codebattle.client;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

class BoardElementHelper {
    private static BoardElementHelper instance = null;
    private final GameBoard board;

    private BoardElementHelper(GameBoard board) {
        this.board = board;
    }

    static BoardElementHelper of(GameBoard board) {
        if (instance == null) {
            instance = new BoardElementHelper(board);
        }
        return instance;
    }

    boolean isSurface(BoardPoint point) {
        if (isLadder(point)) return true;
        BoardElement element = board.getElementAt(point);
        switch (element) {
            case BRICK:
            case UNDESTROYABLE_WALL:
            case PIT_FILL_1:
            case PIT_FILL_2:
            case PIT_FILL_3:
            case PIT_FILL_4:
            case DRILL_PIT:
                return true;
            default:
                return false;
        }
    }

    boolean isLadder(BoardPoint point) {
        BoardElement element = board.getElementAt(point);
        switch (element) {
            case LADDER:
            case ENEMY_LADDER:
            case HERO_LADDER:
            case OTHER_HERO_LADDER:
            case HERO_SHADOW_LADDER:
            case OTHER_HERO_SHADOW_LADDER:
                return true;
            default:
                return false;
        }
    }

    boolean isWall(BoardPoint point) {
        BoardElement element = board.getElementAt(point);
        switch (element) {
            case BRICK:
            case UNDESTROYABLE_WALL:
                return true;
            default:
                return false;
        }
    }

    boolean isPipe(BoardPoint point) {
        BoardElement element = board.getElementAt(point);
        switch (element) {
            case PIPE:
            case ENEMY_PIPE_LEFT:
            case ENEMY_PIPE_RIGHT:
            case HERO_PIPE_LEFT:
            case HERO_PIPE_RIGHT:
            case OTHER_HERO_PIPE_LEFT:
            case OTHER_HERO_PIPE_RIGHT:
            case HERO_SHADOW_PIPE_LEFT:
            case HERO_SHADOW_PIPE_RIGHT:
            case OTHER_HERO_SHADOW_PIPE_LEFT:
            case OTHER_HERO_SHADOW_PIPE_RIGHT:
                return true;
            default:
                return false;
        }
    }
}
