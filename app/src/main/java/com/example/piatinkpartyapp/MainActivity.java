package com.example.piatinkpartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.piatinkpartyapp.screens.CreateGame;
import com.example.piatinkpartyapp.screens.Einstellungen;
import com.example.piatinkpartyapp.screens.PlayGame;


public class MainActivity extends AppCompatActivity {

    private Button BtnLobbyPlay;
    private Button createGame;
    private Button einstellungen;
    private Button rules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Calling the Lobby Activity
        BtnLobbyPlay = (Button) findViewById(R. id.BtnPlayGame);

        BtnLobbyPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlayGame.class));
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
            }
        }));

        einstellungen = (Button) findViewById(R.id.BtnOptions);

        einstellungen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Einstellungen.class));
            }
        });
        rules = (Button) findViewById(R.id.BtnGameRules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SchnopsnActivity.class));
            }
        });

    }
}