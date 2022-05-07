package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.piatinkpartyapp.GameViewModel;
import com.example.piatinkpartyapp.R;


public class WaitingPlayersFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button backBtn;
    Button BtnStartGame3;

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


}