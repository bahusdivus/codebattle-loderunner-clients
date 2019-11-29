package ru.codebattle.client;

import lombok.extern.slf4j.Slf4j;
import ru.codebattle.client.api.GameBoard;
import ru.codebattle.client.api.LoderunnerAction;
import ru.codebattle.client.api.LoderunnerBase;

import java.net.URISyntaxException;

@Slf4j
public class LodeRunnerClient extends LoderunnerBase {

    private Player player;

    LodeRunnerClient(String serverAddress, String user, String code) throws URISyntaxException {
        super(serverAddress, user, code);
    }

    void run(Player player) {
        connect();
        this.player = player;
    }

    @Override
    protected String doMove(GameBoard gameBoard) {
        LoderunnerAction action = player.decide(gameBoard);
        System.out.println(action.toString());
        return loderunnerActionToString(action);
    }

    void initiateExit() {
        setShouldExit(true);
    }
}
