package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.piatinkpartyapp.R;


public class CreateGameFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button ButtonBack;
    private Button ButtonStartLobby;


    private String mParam1;
    private String mParam2;

    public CreateGameFragment(){}

    public static CreateGameFragment newInstance(String param1, String param2) {
        CreateGameFragment fragment = new CreateGameFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_game, container, false);

        //add views
        ButtonBack = root.findViewById(R.id.buttonBack);
        ButtonStartLobby = root. findViewById(R. id. buttonStartLobby);

        //add onclick listeners
        ButtonBack.setOnClickListener(this);
        ButtonStartLobby.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == ButtonBack) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else if(view == ButtonStartLobby) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                    new WaitingPlayersFragment()).commit();
        }
    }
}