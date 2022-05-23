package com.example.piatinkpartyapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.cards.WattnDeck;
import com.example.piatinkpartyapp.chat.ChatFragment;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Responses;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

public class WattnFragment extends Fragment implements View.OnClickListener {
        ImageView arrowBtn;
        ImageView handCardView1;
        ImageView handCardView2;
        ImageView handCardView3;
        ImageView handCardView4;
        ImageView handCardView5;

        ImageButton exitBtn;
        TextView scoreTxt;
        Button scoreboardBtn;
        Button voteBtn;
        Button mixCardsBtn;
        Boolean isMyTurn;
        Boolean schlagToSet;
        Boolean trumpToSet;
public static ImageView currentCard1;
public static ImageView currentCard2;
public static WattnDeck deck;
        ArrayList<Card> handCards;
      public static  ArrayList<Card> currentCards;
        ArrayList<ImageView> currentCardImageViews;

        ClientViewModel clientViewModel;
        ArrayList<ImageView> handCardImageViews;

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private static final String ARG_PARAM1 = "param1";
private static final String ARG_PARAM2 = "param2";

// TODO: Rename and change types of parameters
private String mParam1;
private String mParam2;

public WattnFragment() {
        // Required empty public constructor
        }

/**
 * Use this factory method to create a new instance of
 * this fragment using the provided parameters.
 *
 * @param param1 Parameter 1.
 * @param param2 Parameter 2.
 * @return A new instance of fragment SchnopsnFragment.
 */
// TODO: Rename and change types and number of parameters
public static WattnFragment newInstance(String param1, String param2) {
        WattnFragment fragment = new WattnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
        }

@Override
public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //set fullscreen and landscape mode
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //todo: remove, but used for testing
        //startChatTestServer();
        System.out.println("pepep");
        }



    /*
    Only used for testing so I can create a client on same device to connect to
     */
        GameServer s;
private void startChatTestServer() {
        s = new GameServer();
        try {
        s.startNewGameServer();
        Thread.sleep(2000);
        } catch (IOException e) {
        e.printStackTrace();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }

private void waitForMyTurn(Boolean isMyTurn) {
        if(isMyTurn) {
                Toast.makeText(requireActivity().getApplicationContext(), "Du bist dran", Toast.LENGTH_SHORT).show();
                this.isMyTurn = true;
        }else{
                this.isMyTurn = false;
        }
        }
        private void waitForTrump(Boolean trumpToSet){
        if(trumpToSet){
                Toast.makeText(requireActivity().getApplicationContext(),"Mit Doppelklick Trumpf setzen",Toast.LENGTH_LONG).show();
                this.trumpToSet = true;
        }else{
                this.trumpToSet = false;
        }
}
        private void waitForHit(Boolean schlagToSet){
        if(schlagToSet){
                Toast.makeText(requireActivity().getApplicationContext(),"mit doppelklick schlag setzen",Toast.LENGTH_LONG).show();
                this.schlagToSet = true;
        }else{
                this.schlagToSet = false;
        }
        }

        private Symbol selectTrump(){
                final Symbol[] sym = {null};
                int j = 0;
                for(ImageView imageView:handCardImageViews){

                        imageView.setOnClickListener(new View.OnClickListener() {
                                //add double click listeners in order to set hit & trump
                                Long doubleClickLastTime = 0L;
                                @Override
                                public void onClick(View view) {

                                        if (System.currentTimeMillis() - doubleClickLastTime < 300) {
                                                doubleClickLastTime = 0L;
                                                //    if (deck.getHit() == null) {
                                       /* String[] desc = handCardView.getContentDescription().toString().split("_");
                                        for (CardValue v : CardValue.values()) {
                                                if (v.name().equals(desc[1])) {
                                                        deck.setHit(v);

                                                }
                                        }*/
                                                // } else if (deck.getTrump() == null) {
                                                Toast.makeText(getActivity(), "test works",Toast.LENGTH_LONG).show();
                                                String[] desc = imageView.getContentDescription().toString().split("_");
                                                for (Symbol s : Symbol.values()) {
                                                        if (s.name().equals(desc[0])) {
                                                                sym[0] = s;
                                                                Toast.makeText(getActivity(), "Trump is "+s, Toast.LENGTH_LONG).show();
                                                        }
                                                }

                                                // }
                                                //  Toast.makeText(getActivity(), "Hit is " + deck.getHit(), Toast.LENGTH_LONG).show();
                                                // Toast.makeText(getActivity(), "Trump is " + deck.getTrump(), Toast.LENGTH_LONG).show();
                                        } else {
                                                doubleClickLastTime = System.currentTimeMillis();
                                        }
                                }


                        });
                }
                return sym[0];
        }


private void showTrump(Symbol trump){
        Toast.makeText(requireActivity().getApplicationContext(), "Trump is "+ trump, Toast.LENGTH_LONG).show();
}
private void initHandCardsViews(){
        handCardImageViews = new ArrayList<>();
        handCardImageViews.add(handCardView1);
        handCardImageViews.add(handCardView2);
        handCardImageViews.add(handCardView3);
        handCardImageViews.add(handCardView4);
        handCardImageViews.add(handCardView5);

        //current cards
        currentCards = new ArrayList<>();
        currentCardImageViews = new ArrayList<>();
        currentCardImageViews.add(currentCard1);
        currentCardImageViews.add(currentCard2);

        isMyTurn = false;
        schlagToSet =true;
        trumpToSet = true;
        }

private void updateHandCards(ArrayList<Card> handCards) {
        this.handCards = handCards;

        int j = 0;
        //set onclickListeners to handcards
        for(ImageView imageView:handCardImageViews){

        setCardImage(handCards.get(j).frontSide.toLowerCase(Locale.ROOT),imageView);
        j++;
        handCardViewListener(imageView);
        }
        }

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_wattn, container, false);

        addAllViews(root);

        addOnclickHandlers();
        initHandCardsViews();

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        clientViewModel.getHandCards().observe(getActivity(), handCards -> updateHandCards(handCards));
        clientViewModel.isMyTurn().observe(getActivity(), isMyTurn -> waitForMyTurn(isMyTurn));
        clientViewModel.schlagToSet().observe(getActivity(),schlagToSet -> waitForHit(schlagToSet));
        clientViewModel.trumpToSet().observe(getActivity(), trumpToSet -> waitForTrump(trumpToSet));
        clientViewModel.getPlayedCard().observe(getActivity(), playedCard -> setPlayedCard(playedCard));
       // clientViewModel.selectTrump().observe(getActivity(), selTrump -> selectTrump());
      // clientViewModel.getHandoutCard().observe(getActivity(), card -> getHandoutCard(card));

        //initializeGame();
       //  deck = new WattnDeck(GameName.Wattn,2);
        return root;
        }

/*private void getHandoutCard(Card c) {
        handCards.add(c);
        for(ImageView i : handCardImageViews){
        if(i.getContentDescription().equals("backside")){
        setCardImage(c.frontSide.toLowerCase(Locale.ROOT),i);
        i.setContentDescription(c.frontSide.toLowerCase(Locale.ROOT));
        break;
        }
        }
        }*/

private void addOnclickHandlers() {
        arrowBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        scoreboardBtn.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
        //mixCardsBtn.setOnClickListener(this);
        }

private void addAllViews(View view) {
        arrowBtn = view.findViewById(R.id.arrowBtn);
        exitBtn = view.findViewById(R.id.exitBtn);
        scoreTxt = view.findViewById((R.id.scoreTxt));
        scoreboardBtn = view.findViewById(R.id.scoreboardBtn);
        voteBtn = view.findViewById(R.id.voteBtn);

        handCardView1 = view.findViewById(R.id.card1);
        handCardView2 = view.findViewById(R.id.card2);
        handCardView3 = view.findViewById(R.id.card3);
        handCardView4 = view.findViewById(R.id.card4);
        handCardView5 = view.findViewById(R.id.card5);
        currentCard1 = view.findViewById(R.id.currentCardPlayer1);
        currentCard2 = view.findViewById(R.id.currentCardPlayer2);
     //   mixCardsBtn = view.findViewById(R.id.mixBtn);
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
        goBack();
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
        }else if(view == mixCardsBtn) {
        deck.mixCards();
        }
        }

public void showSideDrawer() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ChatFragment()).commit();
        }

public void showScoreboard() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ScoreBoard()).commit();
        }

public void showVote() {
        requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new VotingDialog()).commit();
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
//play card from handcards, displayed & set to currentCard
private static void play(Card c, int position){
        String s = c.frontSide.toLowerCase(Locale.ROOT);
        if(position ==1) {
                setCardImage(s, currentCard1);
        }else if(position == 2){
                setCardImage(s, currentCard2);
        }

        }
  private void setPlayedCard(Responses.SendPlayedCardToAllPlayers playedCard){
        play(playedCard.card,playedCard.playerID);
  }
/*longonclicklistener to handcards
 * if longonclick to handcard, card gets played, if it is available (handcards showing the backside image are unavailable -checked via imageview description)*/
private void handCardViewListener(ImageView handCardView){
        handCardView.setOnLongClickListener(new View.OnLongClickListener() {
@Override
public boolean onLongClick(View view) {
        //currenCard view is only to be visible if there is a played card - invisible by default
        currentCard1.setVisibility(View.VISIBLE);
        //card only playable if it is availalbe (not showing its backside)
        if(!handCardView.getContentDescription().equals("backside")){
        String[] x = handCardView.getContentDescription().toString().split("_");

        //select selected card form handcards array list
        Card c = new Card(Symbol.randomSymbol(), CardValue.ACHT);
        for(Card d : handCards){
        if(d.symbol.name().equals(x[0].toUpperCase(Locale.ROOT)) && d.cardValue.name().equals(x[1].toUpperCase(Locale.ROOT))){
        c = handCards.get(handCards.indexOf(d));
        }
        }

        //play selected card, remove it from handcards & show backside of this card
        //play(c);
        clientViewModel.setCard(c);
        handCards.remove(c);
        setCardImage("backside",handCardView);
        }
        return false;
        }
        });

        //final Integer[] doubleClickLastTime = {0};


        }

private void goBack() {
        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }

        }
