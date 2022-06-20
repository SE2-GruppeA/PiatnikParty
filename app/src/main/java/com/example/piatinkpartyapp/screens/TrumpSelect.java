package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.piatinkpartyapp.clientuilogic.ClientViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.cards.Symbol;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrumpSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrumpSelect extends Fragment implements View.OnClickListener {
    ImageButton btnTrumpHerz;
    ImageButton btnTrumpKaro;
    ImageButton btnTrumpKreuz;
    ImageButton btnTrumpPick;

    ClientViewModel clientViewModel;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public TrumpSelect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrumpSelect.
     */

    public static TrumpSelect newInstance(String param1, String param2) {
        TrumpSelect fragment = new TrumpSelect();
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
        View root = inflater.inflate(R.layout.fragment_trump_select, container, false);

        btnTrumpHerz = root.findViewById(R.id.btnTrumpHerz);
        btnTrumpKaro = root.findViewById(R.id.btnTrumpKaro);
        btnTrumpKreuz = root.findViewById(R.id.btnTrumpKreuz);
        btnTrumpPick = root.findViewById(R.id.btnTrumpPick);

        btnTrumpHerz.setOnClickListener(this);
        btnTrumpKaro.setOnClickListener(this);
        btnTrumpKreuz.setOnClickListener(this);
        btnTrumpPick.setOnClickListener(this);

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        return root;
    }

    @Override
    public void onClick(View view) {
        if(view == btnTrumpHerz){
            setTrump(Symbol.HERZ);
        }else if(view == btnTrumpKaro){
            setTrump(Symbol.KARO);
        }else if(view == btnTrumpKreuz){
            setTrump(Symbol.KREUZ);
        }else if(view == btnTrumpPick){
            setTrump(Symbol.PICK);
        }
    }

    public void setTrump(Symbol trump){
        clientViewModel.setTrump(trump);

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}