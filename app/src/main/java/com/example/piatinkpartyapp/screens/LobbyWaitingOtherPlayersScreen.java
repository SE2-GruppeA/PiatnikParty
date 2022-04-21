package com.example.piatinkpartyapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.piatinkpartyapp.R;

public class LobbyWaitingOtherPlayersScreen extends AppCompatActivity {

    private Button BtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby_waiting_other_players_screen);


        //Button for Creating the Game
        BtnBack = (Button) findViewById(R. id. BtnSpielErstellen);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LobbyWaitingOtherPlayersScreen.this, CreateGame.class));
                finish();
            }
        });

    }
}