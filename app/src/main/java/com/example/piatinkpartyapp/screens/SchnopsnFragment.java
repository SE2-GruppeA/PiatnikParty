package com.example.piatinkpartyapp.screens;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.cards.CardValue;
import com.example.piatinkpartyapp.cards.SchnopsnDeck;
import com.example.piatinkpartyapp.cards.Symbol;
import com.example.piatinkpartyapp.chat.fragments.ChatFragment;
import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.chat.fragments.ExposeCheaterFragment;
import com.example.piatinkpartyapp.chat.fragments.ExposeDialogFragment;
import com.example.piatinkpartyapp.chat.fragments.IsCheaterDialogFragment;
import com.example.piatinkpartyapp.networking.Responses;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchnopsnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchnopsnFragment extends Fragment implements View.OnClickListener, ExposeDialogFragment.ExposeDialogHandler {
    private static final String TAG = "SchnopsnFragment";

    ImageView arrowBtn;
    ImageView handCardView1;
    ImageView handCardView2;
    ImageView handCardView3;
    ImageView handCardView4;
    ImageView handCardView5;
    ImageView cardDeckView;
    ImageView swapCardView;
    ImageView imgTrump;

    ImageButton exitBtn;
    TextView scoreTxt;
    Button scoreboardBtn;
    Button voteBtn;
    Button mixCardsBtn;
    Button btnCheat;
    Button btnExpose;
    private static ImageView currentCard1;
    private static ImageView currentCard2;
    public static SchnopsnDeck deck;
    ArrayList<Card> handCards;

    ClientViewModel clientViewModel;
    ArrayList<ImageView> handCardImageViews;

    //indicates the clients turn, so they can not
    //play cards when its false
    Boolean isMyTurn;

    //sensorics
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    Boolean mixedCards;
    Boolean cardsToMix;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SchnopsnFragment() {
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
    public static SchnopsnFragment newInstance(String param1, String param2) {
        SchnopsnFragment fragment = new SchnopsnFragment();
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
        //sensorics
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        //set fullscreen and landscape mode
        requireActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            mixedCards = false;
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                cardsToMix = true;

                Toast.makeText(getContext(), "shake detected", Toast.LENGTH_LONG).show();
                clientViewModel.mixCards();
               // mixCards(cardsToMix);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void mixCards(Boolean mixedCards) {
        if (mixedCards) {
            this.mixedCards = false;
            mixedCards = false;// da gemsicht wurde
            Toast.makeText(requireActivity().getApplicationContext(), "die Karten des Stapels wurden neu gemischt!", Toast.LENGTH_LONG).show();
        }

    }

    private void waitForMyTurn(Boolean isMyTurn) {
        if (isMyTurn) {
            Toast.makeText(requireActivity().getApplicationContext(), "Du bist dran", Toast.LENGTH_SHORT).show();
            this.isMyTurn = true;
        } else {
            this.isMyTurn = false;
        }
    }

    private void initHandCardsViews() {
        handCardImageViews = new ArrayList<>();
        handCardImageViews.add(handCardView1);
        handCardImageViews.add(handCardView2);
        handCardImageViews.add(handCardView3);
        handCardImageViews.add(handCardView4);
        handCardImageViews.add(handCardView5);
        isMyTurn = false;
    }

    private void updateHandCards(ArrayList<Card> handCards) {
        this.handCards = handCards;

        int j = 0;
        //set onclickListeners to handcards
        for (ImageView imageView : handCardImageViews) {

            setCardImage(handCards.get(j).frontSide.toLowerCase(Locale.ROOT), imageView);
            j++;
            handCardViewListener(imageView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_schnopsn, container, false);

        addAllViews(root);

        addOnclickHandlers();
        initHandCardsViews();

        //checks if the layout is already landscape
        //if it would not be in landscape mode some dialogs would get displayed twice
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            clientViewModel = new ViewModelProvider(this).get(ClientViewModel.class);

            clientViewModel.getHandCards().observe(getViewLifecycleOwner(), handCards -> updateHandCards(handCards));
            clientViewModel.isMyTurn().observe(getViewLifecycleOwner(), isMyTurn -> waitForMyTurn(isMyTurn));
            clientViewModel.getHandoutCard().observe(getViewLifecycleOwner(), card -> getHandoutCard(card));
            clientViewModel.isVotingForNextGame().observe(getViewLifecycleOwner(), votingForNextGame -> voteForNextGame(votingForNextGame));
            clientViewModel.isEndOfRound().observe(getViewLifecycleOwner(), winner -> atRoundEnd(winner));
            clientViewModel.getPlayedCard().observe(getViewLifecycleOwner(), playedCard -> setPlayedCard(playedCard));
            clientViewModel.getPoints().observe(getViewLifecycleOwner(), points -> setScorePoints(points));
            clientViewModel.isSetTrump().observe(getViewLifecycleOwner(), setTrump -> playerSetTrump(setTrump));
            clientViewModel.isSetSchlag().observe(getViewLifecycleOwner(), setSchlag -> playerSetSchlag(setSchlag));
            clientViewModel.getTrump().observe(getViewLifecycleOwner(), trump -> setTrump(trump));
            clientViewModel.getWinnerId().observe(getViewLifecycleOwner(), winnerId -> showWinner(winnerId));

            //if a new chatmessage is received, the arrow gets a little red circle, indicating the new message
            clientViewModel.getChatMessages().observe(getViewLifecycleOwner(), message -> notifyNewMessage(message));

            //when a game is started, the client gets notified
            clientViewModel.isSchnopsnStarted().observe(getViewLifecycleOwner(), started -> initializeSchnopsn(started));
            clientViewModel.isWattnStarted().observe(getViewLifecycleOwner(), started -> initializeWattn(started));
            clientViewModel.isPensionistlnStarted().observe(getViewLifecycleOwner(), started -> initializePensionistln(started));
            clientViewModel.isHosnObeStarted().observe(getViewLifecycleOwner(), started -> initializeHosnObe(started));

            //shaking phone to mix cards

            clientViewModel.mixedCards().observe(getViewLifecycleOwner(), mixedCards -> mixCards(mixedCards));
        }

        //initializeGame();
        return root;
    }

    private void initializeHosnObe(Boolean started) {
        if (started) {

        }
    }

    private void initializePensionistln(Boolean started) {
        if (started) {

        }
    }

    private void initializeWattn(Boolean started) {
        if (started) {

        }
    }

    private void initializeSchnopsn(Boolean started) {
        if (started) {
            resetImageView(currentCard1);
            resetImageView(currentCard2);

            scoreTxt.setText("0");
        }
    }

    private void resetImageView(ImageView imageView) {
        imageView.setImageResource(getResId("placeholder"));
    }

    private void notifyNewMessage(ArrayList<ChatMessage> message) {
        if (message.size() > 0) {
            arrowBtn.setImageResource(getResId("arrow_new_message"));
        }
    }

    private void playerSetSchlag(Boolean setSchlag) {
        if (setSchlag) {
            requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new SchlagSelect()).commit();
        }
    }

    private void playerSetTrump(Boolean setTrump) {
        if (setTrump) {
            requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new TrumpSelect()).commit();
        }
    }

    private void setScorePoints(Integer points) {
        scoreTxt.setText(points.toString());
    }

    private void setTrump(Symbol trump) {
        String symbol = trump.toString();
        Integer rid = getResId(symbol.toLowerCase(Locale.ROOT));
        imgTrump.setImageResource(rid);
        imgTrump.setContentDescription(symbol);
    }

    private void setPlayedCard(Responses.SendPlayedCardToAllPlayers playedCard) {
        play(playedCard.card, playedCard.playerID);
    }

    private void atRoundEnd(Integer winner) {
        showRoundWinner(winner);
    }

    private void voteForNextGame(Boolean votingForNextGame) {
        if (votingForNextGame) {
            showVote();
        }
    }

    private void getHandoutCard(Card c) {
        handCards.add(c);
        for (ImageView i : handCardImageViews) {
            if (i.getContentDescription().equals("backside")) {
                setCardImage(c.frontSide.toLowerCase(Locale.ROOT), i);
                i.setContentDescription(c.frontSide.toLowerCase(Locale.ROOT));
                break;
            }
        }
    }

    private void addOnclickHandlers() {
        arrowBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        scoreboardBtn.setOnClickListener(this);
        voteBtn.setOnClickListener(this);
        btnCheat.setOnClickListener(this);
        btnExpose.setOnClickListener(this);
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
        cardDeckView = view.findViewById(R.id.cardDeck);
        currentCard1 = view.findViewById(R.id.currentCard);
        currentCard2 = view.findViewById(R.id.currentCard2);
        swapCardView = view.findViewById(R.id.swapCard);
        mixCardsBtn = view.findViewById(R.id.mixBtn);
        imgTrump = view.findViewById(R.id.imgTrump);
        btnCheat = view.findViewById(R.id.btnCheat);
        btnExpose = view.findViewById(R.id.btnExpose);


    }

    @Override
    public void onClick(View view) {
        if (view == arrowBtn) {
            openChatFragment();
        } else if (view == exitBtn) {
            exitGame(view);
        } else if (view == scoreboardBtn) {
            showScoreboard();
        } else if (view == voteBtn) {
            clientViewModel.forceVoting();
        } else if (view == mixCardsBtn) {
            deck.mixCards();
        } else if (view == btnCheat) {
            clientViewModel.cheatRequest();
        } else if (view == btnExpose) {
            openExposeDialog();
        }
    }

    // todo: isCheater comes from LiveData
    private void isCheaterLiveDataHandler(boolean isCheater) {
        IsCheaterDialogFragment dialog;

        if (isCheater) {
            dialog = new IsCheaterDialogFragment(true);
        } else {
            dialog = new IsCheaterDialogFragment(false);
        }

        // I know this is considered deprecated but I could not find any other way to solve this
        dialog.setTargetFragment(SchnopsnFragment.this, 1);
        dialog.show(getFragmentManager(), TAG + " IsCheaterDialogFragment");
    }

    private void openExposeDialog() {
        ExposeDialogFragment dialog = new ExposeDialogFragment();
        // I know this is considered deprecated but I could not find any other way to solve this
        dialog.setTargetFragment(SchnopsnFragment.this, 1);
        dialog.show(getFragmentManager(), TAG + " CheatDialogFragment");
    }

    @Override
    public void handleExpose(Boolean doExpose) {
        Log.d(TAG, "Expose ? " + doExpose);
        if (doExpose) {
            // open new fragment !
            requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, new ExposeCheaterFragment()).commit();
        }
    }

    public void openChatFragment() {
        //convert arrow back, if a new message was received indicated by the red circle
        arrowBtn.setImageResource(getResId("arrow"));
        showSideDrawer();
    }

    public void exitGame(View view) {
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

    private static void setCardImage(String cardName, ImageView imgview) {
        Integer rid = getResId(cardName);
        imgview.setImageResource(rid);
        imgview.setContentDescription(cardName);
    }

    public static int getResId(String resName) {

        try {

            Field idField = R.drawable.class.getDeclaredField(resName);
            Log.d("##############", idField.getName());
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    //play card from handcards, displayed & set to currentCard
    private static void play(Card c, int position) {
        String s = c.frontSide.toLowerCase(Locale.ROOT);

        if (position == 1) {
            setCardImage(s, currentCard1);
        } else if (position == 2) {
            setCardImage(s, currentCard2);
        }
    }

    /*longonclicklistener to handcards
     * if longonclick to handcard, card gets played, if it is available (handcards showing the backside image are unavailable -checked via imageview description)*/
    private void handCardViewListener(ImageView handCardView) {
        handCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //cards can only be played if it is the clients turn
                if (isMyTurn) {
                    //currenCard view is only to be visible if there is a played card - invisible by default
                    currentCard1.setVisibility(View.VISIBLE);
                    //card only playable if it is availalbe (not showing its backside)
                    if (!handCardView.getContentDescription().equals("backside")) {
                        String[] x = handCardView.getContentDescription().toString().split("_");

                        //select selected card form handcards array list
                        Card c = new Card(Symbol.randomSymbol(), CardValue.ACHT);
                        for (Card d : handCards) {
                            if (d.symbol.name().equals(x[0].toUpperCase(Locale.ROOT)) && d.cardValue.name().equals(x[1].toUpperCase(Locale.ROOT))) {
                                c = handCards.get(handCards.indexOf(d));
                            }
                        }

                        //play selected card, remove it from handcards & show backside of this card
                        //play(c);
                        clientViewModel.setCard(c);
                        handCards.remove(c);
                        setCardImage("backside", handCardView);
                    }
                } else {
                    Toast.makeText(requireActivity().getApplicationContext(),
                            "Warte auf deine Runde",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    private void goBack() {
        //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void showRoundWinner(Integer winnerID){
        requireActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content, WinnerFragment.newInstance("Player " + winnerID)).commit();
    }

    private void showWinner(Integer winnerID){
        Toast.makeText(requireActivity().getApplicationContext(),
                "Player " + winnerID + " hat den Stich bekommen",
                Toast.LENGTH_SHORT).show();
    }
}