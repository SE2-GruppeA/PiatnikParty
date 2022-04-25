package com.example.piatinkpartyapp.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.SchnopsnActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button CreateGameBtn;
    private Button PlayGameBtn;
    private Button EinstellungenBtn;
    private Button GameRulesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addAllMainActivityViews();
        addOnclickHandlersMainActivity();
    }

    @Override
    public void onClick(View view) {
        if (view == CreateGameBtn) {
            showCreateGameFragment();
        }else if(view==PlayGameBtn){
            showPlayGameFragment();
        }else if(view == EinstellungenBtn){
            showEinstellungenFragment();
        }else if(view == GameRulesBtn){
            showGameRulesFragment();
        }
    }

    public void addAllMainActivityViews(){
        CreateGameBtn = findViewById(R. id. BtnCreateGame);
        PlayGameBtn = findViewById(R. id. BtnPlayGame);
        EinstellungenBtn = findViewById(R. id. BtnOptions);
        GameRulesBtn = findViewById( R. id. BtnGameRules);
    }

    public void addOnclickHandlersMainActivity() {
        CreateGameBtn.setOnClickListener(this);
        PlayGameBtn.setOnClickListener(this);
        EinstellungenBtn.setOnClickListener(this);
        GameRulesBtn.setOnClickListener(this);
    }

    public void showCreateGameFragment() {
        //Broken Code
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new CreateGameFragment()).commit();

    }
    public void showPlayGameFragment(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new PlayGameFragment()).commit();
    }

    public void showEinstellungenFragment(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new EinstellungenFragment()).commit();
    }

    public void showGameRulesFragment(){
        Intent a = new Intent(this, SchnopsnActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
    }


}