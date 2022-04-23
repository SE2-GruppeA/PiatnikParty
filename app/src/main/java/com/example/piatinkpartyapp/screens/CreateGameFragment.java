package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.piatinkpartyapp.R;

public class CreateGameFragment extends Fragment implements View.OnClickListener {

    private Button BtnBack;
    private Button BtnStartGame;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onClick(View view) {
        if(view == BtnBack){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }else if( view == BtnStartGame){
            getActivity().getSupportFragmentManager().beginTransaction().replace(androidx.fragment.R.id.fragment_container_view_tag,new WaitingPlayersFragment()).commit();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_play_game, container, false);;
        //add views
        BtnBack = root.findViewById(R.id.BtnBack2);
        BtnStartGame = root.findViewById(R. id. LobbyStartGameBtn);
        //add onclick listeners
        BtnBack.setOnClickListener(this);
        BtnStartGame.setOnClickListener(this);

        return root;
    }
}