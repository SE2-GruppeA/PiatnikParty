package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.piatinkpartyapp.R;


public class GameRulesFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private Button BtnBackGameRules;

    public GameRulesFragment() { }

    public static GameRulesFragment newInstance(String param1, String param2) {
        GameRulesFragment fragment = new GameRulesFragment();
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_rules, container, false);
        BtnBackGameRules = view.findViewById(R. id. buttonBackGameRules);
        BtnBackGameRules.setOnClickListener(this);

        return  inflater.inflate(R.layout.fragment_game_rules, container, false);
    }

    @Override
    public void onClick(View view) {
        if(view == BtnBackGameRules){
            //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}