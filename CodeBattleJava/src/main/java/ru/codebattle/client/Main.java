package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static final String SERVER_ADDRESS = "codebattle-spb-2019.francecentral.cloudapp.azure.com";
    private static final String PLAYER_NAME = "0ms9a3rlxjzn58z78foe";
    private static final String AUTH_CODE = "4817665042750827153";

    public static void main(String[] args) throws URISyntaxException, IOException {
        LodeRunnerClient client = new LodeRunnerClient(SERVER_ADDRESS, PLAYER_NAME, AUTH_CODE);

        client.run(Player.getInstance());

        System.in.read();

        client.initiateExit();
    }
}
