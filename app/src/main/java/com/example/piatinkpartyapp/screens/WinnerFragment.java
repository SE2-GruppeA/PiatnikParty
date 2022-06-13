package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.R;

public class WinnerFragment extends Fragment implements View.OnClickListener {

    private static final String WINNER = "winner";

    private String winner;

    TextView txtWinner;
    ImageView btnClose;

    private ClientViewModel clientViewModel;

    public WinnerFragment() {
        // Required empty public constructor
        winner = "";
    }

    public static WinnerFragment newInstance(String winner) {
        WinnerFragment fragment = new WinnerFragment();
        Bundle args = new Bundle();
        args.putString(WINNER, winner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            winner = getArguments().getString(WINNER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_winner, container, false);

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        txtWinner = view.findViewById(R.id.txtWinner);
        txtWinner.setText(winner);

        btnClose = view.findViewById(R.id.btnCloseWinnerFragment);
        btnClose.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == btnClose){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            clientViewModel.notifyVote();
        }
    }
}