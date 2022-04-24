package com.example.piatinkpartyapp.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.piatinkpartyapp.MainActivity;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.NetworkHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CreateGame extends AppCompatActivity {

    private Button returnButton;
    private Button BtnCreateGame;
    private ServerViewModel model;
    private GameClient gameClient;
    private GameServer gameServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        //Returning Button
        returnButton = (Button) findViewById(R.id.button2);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGame.this, MainActivity.class));
                finish();
            }
        });

        //Button for Creating the Game
        BtnCreateGame = (Button) findViewById(R. id. BtnSpielErstellen);
        BtnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateGame.this, LobbyWaitingOtherPlayersScreen.class));
                finish();

                //set IP and create GameServer
                try {
                    NetworkHandler.GAMESERVER_IP = getIp();
                    gameServer = new GameServer();
                    gameServer.startNewGameServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Creates newMessage  when pressing the button, sends to the Server
                gameClient = new GameClient();
                gameClient.createLobby();
            }
        });
        model = new ViewModelProvider(this).get(ServerViewModel.class);
    }

    // get IP address from client who starts the GameServer
    public String getIp(){
        String ipAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {}

        return ipAddress;
    }

    public void getServerMessage(View v){

    }
}