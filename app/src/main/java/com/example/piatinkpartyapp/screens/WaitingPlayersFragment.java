package com.example.piatinkpartyapp.screens;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.GameViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.gamelogic.Game;
import com.example.piatinkpartyapp.gamelogic.Player;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.Packets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WaitingPlayersFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button backBtn;
    Button BtnStartGame3;
    ListView connectedListView;

    private String mParam1;
    private String mParam2;

    ViewModel viewModel;

    public WaitingPlayersFragment() {
        // Required empty public constructor
    }

    public static WaitingPlayersFragment newInstance(String param1, String param2) {
        WaitingPlayersFragment fragment = new WaitingPlayersFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_waiting_players, container, false);

        viewModel = new ViewModelProvider(this).get(GameViewModel.class);

        TextView textView = (TextView) view.findViewById(R.id.textView4);

        backBtn = (Button) view.findViewById(R.id.button2);
        backBtn.setOnClickListener(this);

        BtnStartGame3 = (Button) view.findViewById(R.id.BtnStartGame3);
        BtnStartGame3.setOnClickListener(this);

        connectedListView = (ListView) view.findViewById(R.id.connectedListView);

        Packets.Responses.ConnectedSuccessfully connectedSuccessfully =
                new Packets.Responses.ConnectedSuccessfully();
        int playerID = connectedSuccessfully.getPlayerID();

        ArrayList<Integer> arrayList = new ArrayList<>(Collections.singleton(playerID));

        GameViewModel.ListAdapter listAdapter =
                new GameViewModel.ListAdapter(requireActivity().getApplicationContext(), arrayList);

        connectedListView.setAdapter(listAdapter);

        CreateGameFragment createGameFragment = new CreateGameFragment();

        ((GameViewModel) viewModel).createGameServer(createGameFragment.getIp());

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view == backBtn)
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        else if (view == BtnStartGame3) {
            ((GameViewModel) viewModel).startGame();
        }
    }

    private void reloadData() {

        List<Integer> ids;


    }


}