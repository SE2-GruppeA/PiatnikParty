package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.clientuilogic.ClientViewModel;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreBoard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreBoard extends Fragment implements View.OnClickListener {
    ImageButton closeBtn;
    TableLayout tblPlayerScore;

    ClientViewModel clientViewModel;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public ScoreBoard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScoreBoard.
     */

    public static ScoreBoard newInstance(String param1, String param2) {
        ScoreBoard fragment = new ScoreBoard();
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
        View root = inflater.inflate(R.layout.fragment_score_board, container, false);;

        //add views
        closeBtn = root.findViewById(R.id.closeBtn);
        tblPlayerScore = root.findViewById(R.id.tblPlayerScore);

        //add onclick listeners
        closeBtn.setOnClickListener(this);

        //add view model and observer(s)
        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        clientViewModel.getPlayers().observe(getViewLifecycleOwner(), this::updateScoreboard);

        return root;
    }

    private void updateScoreboard(Map<String, Integer> players) {
        tblPlayerScore.removeAllViews();

        for(String name:players.keySet()){
            addRowToScoreboard(name, players.get(name));
        }
    }

    public void addRowToScoreboard(String name, Integer score){
        TableRow scoreRow = new TableRow(getActivity());

        scoreRow.addView(createTextView(name, Gravity.LEFT, 24));
        scoreRow.addView(createTextView(score.toString(), Gravity.RIGHT,24));

        tblPlayerScore.addView(scoreRow, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public TextView createTextView(String text, Integer gravity, float fontSize){
        TextView textView = new TextView(getActivity());

        textView.setText(text);
        textView.setGravity(gravity);
        textView.setTextSize(fontSize);

        return textView;
    }

    @Override
    public void onClick(View view) {
        if(view == closeBtn){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

            if(clientViewModel.getCloseGameScoreboard()){
                clientViewModel.closeGame(true);
            }
        }
    }
}