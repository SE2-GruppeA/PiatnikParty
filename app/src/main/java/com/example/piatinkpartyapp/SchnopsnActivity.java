package com.example.piatinkpartyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class SchnopsnActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView arrowBtn;
    ImageView handCardView1;
    ImageView handCardView2;
    ImageView handCardView3;
    ImageView handCardView4;
    ImageView handCardView5;
    ImageView cardDeckView;
    ImageView swapCardView;
    ImageButton exitBtn;
    TextView scoreTxt;
    Button scoreboardBtn;
    Button voteBtn;
    Button mixCardsBtn;
    private static ImageView currentCard;
    public static SchnopsnDeck deck;
    ArrayList<Card> handCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        addAllViews();
        addOnclickHandlers();
    }

    public void addAllViews() {
        arrowBtn = findViewById(R.id.arrowBtn);
        exitBtn = findViewById(R.id.exitBtn);
        scoreTxt = findViewById((R.id.scoreTxt));
        scoreboardBtn = findViewById(R.id.scoreboardBtn);
        voteBtn = findViewById(R.id.voteBtn);

        handCardView1 = findViewById(R.id.card1);
        handCardView2 = findViewById(R.id.card2);
        handCardView3 = findViewById(R.id.card3);
        handCardView4 = findViewById(R.id.card4);
        handCardView5 = findViewById(R.id.card5);
        cardDeckView = findViewById(R.id.cardDeck);
        currentCard = findViewById(R.id.currentCard);
        swapCardView = findViewById(R.id.swapCard);
        mixCardsBtn = findViewById(R.id.mixBtn);
    }

    public void addOnclickHandlers() {
        arrowBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        scoreboardBtn.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
        mixCardsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == arrowBtn) {
            showSideDrawer();
        } else if (view == exitBtn) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
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
        } else if (view == scoreboardBtn) {
            showScoreboard();
        } else if (view == voteBtn) {
            showVote();
        }else if(view == mixCardsBtn){
            deck.mixCards();
        }
    }

    public void showSideDrawer() {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SideDrawer()).commit();
    }

    public void showScoreboard() {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScoreBoard()).commit();
    }

    public void showVote() {
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, new VotingDialog()).commit();
    }

    /*Sample game setup
    * creating corresponding deck
    * set cards to imageviews
    * get handcards
    * set onlongclicklisteners to imageviews to interact with cards*/

    public void initializeGame(){
        deck = new SchnopsnDeck(GameName.Schnopsn, 1);
        ArrayList<ImageView> handCardImageViews = new ArrayList<>();
        handCardImageViews.add(handCardView1);
        handCardImageViews.add(handCardView2);
        handCardImageViews.add(handCardView3);
        handCardImageViews.add(handCardView4);
        handCardImageViews.add(handCardView5);
        currentCard.setVisibility(View.INVISIBLE);
        Card swapCard = deck.swappingCard();
    }
    private static void setCardImage(String cardName, ImageView imgview){
        Integer rid = getResId(cardName);
        imgview.setImageResource(rid);
        imgview.setContentDescription(cardName);
    }
    public static int getResId(String resName) {

        try {

            Field idField = R.drawable.class.getDeclaredField(resName);
            Log.d("##############",idField.getName());
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}