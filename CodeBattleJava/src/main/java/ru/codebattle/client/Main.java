package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    private static final String SERVER_ADDRESS = "codingdojo2019.westeurope.cloudapp.azure.com";
    private static final String PLAYER_NAME = "w1y24bmulw0wzugerc27";
    private static final String AUTH_CODE = "3080348844023863659";

    public static void main(String[] args) throws URISyntaxException, IOException {
        LodeRunnerClient client = new LodeRunnerClient(SERVER_ADDRESS, PLAYER_NAME, AUTH_CODE);

        client.run(new Player());

        System.in.read();

        client.initiateExit();
    }
}
