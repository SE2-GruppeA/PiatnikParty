package com.example.piatinkpartyapp;

import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.NetworkHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class GameViewModel extends ViewModel {

    private GameServer server;
    private GameClient mainClient;
    private GameClient client1;

    public void createGameServer(String ip) {
        NetworkHandler.GAMESERVER_IP = ip;
        server = new GameServer();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainClient = new GameClient(ip);
    }

    public void joinGameServer(String ip) {
        client1 = new GameClient(ip);
    }
}
