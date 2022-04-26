package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.Fragment;



public class PlayGameFragment extends Fragment implements View.OnClickListener{

    private Button BackBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public PlayGameFragment() {
        // Required empty public constructor
    }

    public static PlayGameFragment newInstance(String param1, String param2) {
        PlayGameFragment fragment = new PlayGameFragment();
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

    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // Inflate the layout for this fragment
        //View root = inflater.inflate(R.layout.fragment_play_game, container, false);;
        //add views
        //BackBtn = root.findViewById(R.id.BtnBack);
        //add onclick listeners
        //BackBtn.setOnClickListener(this);
        //return root;
    //}

    @Override
    public void onClick(View view) {
        if(view == BackBtn){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }
}