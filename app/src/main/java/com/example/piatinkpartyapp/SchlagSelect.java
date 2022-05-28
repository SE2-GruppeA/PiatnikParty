package com.example.piatinkpartyapp;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SchlagSelect#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SchlagSelect extends Fragment implements View.OnClickListener {

    ImageButton btnSieben;
    ImageButton btnAcht;
    ImageButton btnNeun;
    ImageButton btnZehn;
    ImageButton btnUnter;
    ImageButton btnOber;
    ImageButton btnKoenig;
    ImageButton btnAss;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    // TODO: Rename and change types and number of parameters
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schlag_select, container, false);;

        btnSieben = view.findViewById(R.id.btnSieben);
        btnAcht = view.findViewById(R.id.btnAcht);
        btnNeun = view.findViewById(R.id.btnNeun);
        btnZehn = view.findViewById(R.id.btnZehn);
        btnUnter = view.findViewById(R.id.btnUnter);
        btnOber = view.findViewById(R.id.btnOber);
        btnKoenig = view.findViewById(R.id.btnKoenig);
        btnAss = view.findViewById(R.id.btnAss);

        return view;
    }

    @Override
    public void onClick(View view) {
        if(view == btnSieben){

        }else if(view == btnAcht){

        }
    }

    public void setSchlag(){

    }
}