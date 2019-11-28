package ru.codebattle.client;

import java.util.function.Consumer;
import java.util.function.Function;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;
import ru.codebattle.client.api.LoderunnerBase;

import java.io.Console;
import java.net.URISyntaxException;
import java.util.Random;

public class LodeRunnerClient extends LoderunnerBase {

    private Player player;

    public LodeRunnerClient(String serverAddress, String user, String code) throws URISyntaxException {
        super(serverAddress, user, code);
    }

    public void run(Player player) {
        connect();
        this.player = player;
    }

    @Override
    protected String doMove(GameBoard gameBoard) {
        LoderunnerAction action = player.decide(gameBoard);
        System.out.println(action.toString());
        return loderunnerActionToString(action);
    }

    public void initiateExit()
    {
        setShouldExit(true);
    }
}
