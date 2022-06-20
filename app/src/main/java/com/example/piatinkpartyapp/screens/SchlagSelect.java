package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.piatinkpartyapp.clientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.cards.CardValue;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchlagSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchlagSelect extends Fragment implements  View.OnClickListener{

    ImageButton btnSchlagSieben;
    ImageButton btnSchlagAcht;
    ImageButton btnSchlagNeun;
    ImageButton btnSchlagZehn;
    ImageButton btnSchlagUnter;
    ImageButton btnSchlagOber;
    ImageButton btnSchlagKoenig;
    ImageButton btnSchlagAss;
    ClientViewModel clientViewModel;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public SchlagSelect() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchlagSelect.
     */

    public static SchlagSelect newInstance(String param1, String param2) {
        SchlagSelect fragment = new SchlagSelect();
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
        View root = inflater.inflate(R.layout.fragment_schlag_select,container,false);

        btnSchlagSieben = root.findViewById(R.id.btnSieben);
        btnSchlagAcht = root.findViewById(R.id.btnAcht);
        btnSchlagNeun = root.findViewById(R.id.btnNeun);
        btnSchlagZehn = root.findViewById(R.id.btnZehn);
        btnSchlagUnter = root.findViewById(R.id.btnUnter);
        btnSchlagOber = root.findViewById(R.id.btnOber);
        btnSchlagKoenig = root.findViewById(R.id.btnKoenig);
        btnSchlagAss = root.findViewById(R.id.btnAss);

        btnSchlagSieben.setOnClickListener(this);
        btnSchlagAcht.setOnClickListener(this);
        btnSchlagNeun.setOnClickListener(this);
        btnSchlagZehn.setOnClickListener(this);
        btnSchlagUnter.setOnClickListener(this);
        btnSchlagOber.setOnClickListener(this);
        btnSchlagKoenig.setOnClickListener(this);
        btnSchlagAss.setOnClickListener(this);

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
        return root;
    }
    @Override
    public void onClick(View view){
        if(view == btnSchlagSieben){
            setSchlag(CardValue.SIEBEN);
        }else if(view == btnSchlagAcht){
            setSchlag(CardValue.ACHT);
        }else if(view == btnSchlagNeun){
            setSchlag(CardValue.NEUN);
        }else if(view == btnSchlagZehn){
            setSchlag(CardValue.ZEHN);
        }else if(view == btnSchlagUnter){
            setSchlag(CardValue.UNTER);
        }else if(view == btnSchlagOber){
            setSchlag(CardValue.OBER);
        }else if(view == btnSchlagKoenig){
            setSchlag(CardValue.KOENIG);
        }else if(view == btnSchlagAss){
            setSchlag(CardValue.ASS);
        }
    }

    public void setSchlag(CardValue schlag){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        clientViewModel.setSchlag(schlag);
    }
}