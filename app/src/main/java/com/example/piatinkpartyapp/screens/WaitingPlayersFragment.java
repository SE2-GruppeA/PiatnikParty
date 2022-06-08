package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.SchnopsnFragment;
import com.example.piatinkpartyapp.WattnFragment;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.NetworkHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class WaitingPlayersFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "isGameStarted";

    Button backBtn;
    Button BtnStartGame3;

    TextView txtIP;
    ListView lvPlayers;

    GameServer server;
    ClientViewModel clientViewModel;

    //indicates whether the SchnopsnFragment was called or not
    private Boolean isGameStarted;

    public WaitingPlayersFragment() {
        // Required empty public constructor
        isGameStarted = false;
    }

    public static WaitingPlayersFragment newInstance(Boolean isGameStarted) {
        WaitingPlayersFragment fragment = new WaitingPlayersFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, isGameStarted);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            isGameStarted = savedInstanceState.getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waiting_players, container, false);

        //viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        TextView textView = (TextView) view.findViewById(R.id.textView4);

        backBtn = (Button) view.findViewById(R.id.button2);
        backBtn.setOnClickListener(this);

        BtnStartGame3 = (Button) view.findViewById(R.id.BtnStartGame3);
        BtnStartGame3.setOnClickListener(this);

        txtIP = view.findViewById(R.id.txtIP);
        txtIP.setText(NetworkHandler.GAMESERVER_IP);

        lvPlayers = view.findViewById(R.id.lvPlayers);

        //test for gameserver start -> needs relocation
        server = new GameServer();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!isGameStarted) {
            clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
            clientViewModel.getConnectionState().observe(getActivity(), connectionState ->
                    displayConnectionState(connectionState));

            clientViewModel.isGameStarted().observe(getActivity(), isGameStarted -> atGameStart());

            server.getPlayers().observe(getActivity(), players -> updatePlayers(players));
        }

        // Inflate the layout for this fragment
        return view;
    }

    private void updatePlayers(ArrayList<Player> players) {
        ArrayList<String> playerList = new ArrayList<>();

        for(Player p:players){
            playerList.add(p.getPlayerName());
        }

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, playerList);
        lvPlayers.setAdapter(arrayAdapter);
    }

    private void displayConnectionState(boolean connectionState) {
        if(connectionState == true){
            Toast.makeText(requireActivity().getApplicationContext(),"Verbindung zum Server erfolgreich", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(requireActivity().getApplicationContext(),"Verbindung zum Server fehlgeschlagen", Toast.LENGTH_LONG).show();
        }
    }

    private void atGameStart(){
        isGameStarted = true;

        //opening the game ui
        getActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new SchnopsnFragment()).commit();
               // new WattnFragment()).commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //sets the variable if the SchnopsnFragment was opened, so it wont get opened a second time
        outState.putBoolean(ARG_PARAM1, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        if (view == backBtn)
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        else if (view == BtnStartGame3) {
            clientViewModel.startGame();
        }

    }
}