package com.example.piatinkpartyapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.piatinkpartyapp.MainActivity;
import com.example.piatinkpartyapp.R;

public class PlayGame extends AppCompatActivity {

    private Button BtnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);


        //Button to return to MainActivity
        BtnReturn = (Button) findViewById(R. id.BtnReturn);

        BtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlayGame.this, MainActivity.class));
                finish();
            }
        });
    }
}