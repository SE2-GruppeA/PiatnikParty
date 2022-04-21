package com.example.piatinkpartyapp.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.piatinkpartyapp.MainActivity;
import com.example.piatinkpartyapp.R;

public class CreateGame extends AppCompatActivity {

    private Button returnButton;
    private Button BtnCreateGame;
    private ServerViewModel model;

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
            }
        });









        model = new ViewModelProvider(this).get(ServerViewModel.class);




    }

    public void getServerMessage(View v){

    }
}