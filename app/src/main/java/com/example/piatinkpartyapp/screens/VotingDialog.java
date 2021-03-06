package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.piatinkpartyapp.clientuilogic.ClientViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.cards.GameName;
import com.example.piatinkpartyapp.gamelogic.Game;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VotingDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VotingDialog extends Fragment implements View.OnClickListener {
    ImageButton closeVoteBtn;
    ClientViewModel clientViewModel;

    Button schnopsnBtn;
    Button wattenBtn;
    Button btnVoteEnd;
    Button pensionistlnBtn;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public VotingDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VotingDialog.
     */

    public static VotingDialog newInstance(String param1, String param2) {
        VotingDialog fragment = new VotingDialog();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_voting_dialog, container, false);

        closeVoteBtn = root.findViewById(R.id.closeVoteBtn);
        schnopsnBtn = root.findViewById(R.id.schnappsenBtn);
        wattenBtn = root.findViewById(R.id.wattenBtn);
        pensionistlnBtn = root.findViewById(R.id.pensionistelnBtn);
        btnVoteEnd = root.findViewById(R.id.btnVoteEnd);

        closeVoteBtn.setOnClickListener(this);
        schnopsnBtn.setOnClickListener(this);
        wattenBtn.setOnClickListener(this);
        pensionistlnBtn.setOnClickListener(this);
        btnVoteEnd.setOnClickListener(this);

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        return root;
    }

    @Override
    public void onClick(View view) {
        if(view == closeVoteBtn){
            closeVotingDialog();
        }else if(view == schnopsnBtn){
            clientViewModel.voteForNextGame(GameName.Schnopsn);
            closeVotingDialog();
        }else if(view == wattenBtn){
            clientViewModel.voteForNextGame(GameName.Wattn);
            closeVotingDialog();
        }else if(view == pensionistlnBtn){
            clientViewModel.voteForNextGame(GameName.Pensionisteln);
            closeVotingDialog();
        }else if(view == btnVoteEnd){
            //vote for the end of the game
            clientViewModel.voteForNextGame(GameName.endOfGame);
            closeVotingDialog();
        }
    }

    public void closeVotingDialog(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}