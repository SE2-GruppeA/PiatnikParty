package com.example.piatinkpartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SchnopsnActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView arrowBtn;
    ImageButton exitBtn;
    TextView scoreTxt;
    Button scoreboardBtn;
    Button voteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        addAllViews();
        addOnclickHandlers();
    }

    public void addAllViews(){
        arrowBtn = findViewById(R.id.arrowBtn);
        exitBtn = findViewById(R.id.exitBtn);
        scoreTxt = findViewById((R.id.scoreTxt));
        scoreboardBtn = findViewById(R.id.scoreboardBtn);
        voteBtn = findViewById(R.id.voteBtn);
    }

    public void addOnclickHandlers(){
        arrowBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        scoreboardBtn.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == arrowBtn){
            showSideDrawer();
        }else if(view == exitBtn){
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Möchtest du dieses Spiel wirklich verlassen?").setPositiveButton("Ja", dialogClickListener)
                    .setNegativeButton("Nein", dialogClickListener).show();
        }else if(view == scoreboardBtn){
        }else if(view == voteBtn){

        }
    }

    public void showSideDrawer(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SideDrawer()).commit();
    }

}