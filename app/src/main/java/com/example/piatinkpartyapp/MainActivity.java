package com.example.piatinkpartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.piatinkpartyapp.screens.*;

import com.example.piatinkpartyapp.networking.GameClient;

public class MainActivity extends AppCompatActivity {

    private Button BtnLobbyPlay;
    private Button createGame;
    private Button einstellungen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling the Lobby Activity
        BtnLobbyPlay = (Button) findViewById(R. id.BtnPlayGame);

        BtnLobbyPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, PlaySelectedGame.class));
                finish();
            }
        });

        // Calling the create game activity
        createGame = (Button) findViewById(R.id.BtnCreateGame);

        createGame.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateGame.class));
                finish();

                // TEST : connects to server and logs info test!
                // TODO : how to listen to responses from server ?
                GameClient g = new GameClient("192.168.1.15");
            }
        }));

        einstellungen = (Button) findViewById(R.id.BtnOptions);

        einstellungen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Einstellungen.class));
            }
        });

    }
}