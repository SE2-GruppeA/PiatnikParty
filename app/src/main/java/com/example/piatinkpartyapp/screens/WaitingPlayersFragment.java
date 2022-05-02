package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.WaitingForPlayersViewModel;


public class WaitingPlayersFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView textView;

    private String mParam1;
    private String mParam2;

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

        ViewModel viewModel = new ViewModelProvider(this).get(WaitingForPlayersViewModel.class);

        String text = ((WaitingForPlayersViewModel) viewModel).getIp().toString();

        textView = (TextView) view.findViewById(R.id.textView4);
        textView.setText(text);

        // Inflate the layout for this fragment
        return view;
    }
}