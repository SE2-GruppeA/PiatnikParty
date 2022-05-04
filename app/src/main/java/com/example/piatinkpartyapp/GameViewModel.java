package com.example.piatinkpartyapp;

import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.NetworkHandler;

import java.io.IOException;

public class GameViewModel extends ViewModel {

    private GameServer server;
    private GameClient client;

    public void createGameServer(String ip) {
        NetworkHandler.GAMESERVER_IP = ip;
        server = new GameServer();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client = new GameClient(ip);
    }

    public void joinGameServer(String ip) {
        client = new GameClient(ip);
    }

    public void startGame() {
        client.startGame();
    }

    public void setCard(Card card) {
        client.setCard(card);
    }
}
