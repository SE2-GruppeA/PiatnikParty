package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.piatinkpartyapp.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button CreateGameBtn;
    private Button PlayGameBtn;
    private Button EinstellungenBtn;
    private Button GameRulesBtn;
    private Button ShowTableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addAllMainActivityViews();
        addOnclickHandlersMainActivity();
        System.out.println("pepep");
    }

    @Override
    public void onClick(View view) {
        if (view == CreateGameBtn) {
            showCreateGameFragment();
        } else if(view == PlayGameBtn) {
            showPlayGameFragment();
        } else if(view == EinstellungenBtn) {
            showEinstellungenFragment();
        } else if(view == ShowTableBtn) {
            showTableMainActivity();
        } else if(view == GameRulesBtn) {
            showGameRulesFragment();
        }
    }

    public void addAllMainActivityViews(){
        CreateGameBtn = findViewById(R. id. BtnCreateGame);
        PlayGameBtn = findViewById(R. id. BtnPlayGame);
        EinstellungenBtn = findViewById(R. id. BtnOptions);
        GameRulesBtn = findViewById( R. id. BtnGameRules);
        ShowTableBtn = findViewById(R. id. btnShowTable);
    }

    public void addOnclickHandlersMainActivity() {
        CreateGameBtn.setOnClickListener(this);
        PlayGameBtn.setOnClickListener(this);
        EinstellungenBtn.setOnClickListener(this);
        GameRulesBtn.setOnClickListener(this);
        ShowTableBtn.setOnClickListener(this);
    }

    public void showCreateGameFragment() {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new CreateGameFragment()).commit();

    }
    public void showPlayGameFragment() {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new PlayGameFragment()).commit();
    }

    public void showEinstellungenFragment(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new EinstellungenFragment()).commit();
    }

    public void showGameRulesFragment(){
        getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new GameRulesFragment()).commit();
    }

    public void showTableMainActivity(){
        /*Intent a = new Intent(this, SchnopsnActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);*/

       /* getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new SchnopsnFragment()).commit();*/
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new WattnFragment()).commit();
    }
}