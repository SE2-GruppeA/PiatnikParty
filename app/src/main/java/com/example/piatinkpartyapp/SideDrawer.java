package com.example.piatinkpartyapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SideDrawer#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SideDrawer extends Fragment implements View.OnClickListener {
    View root;

    ImageView arrowBackBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SideDrawer() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SideDrawer.
     */
    // TODO: Rename and change types and number of parameters
    public static SideDrawer newInstance(String param1, String param2) {
        SideDrawer fragment = new SideDrawer();
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
        root = inflater.inflate(R.layout.fragment_side_drawer, container, false);

        //add views
        arrowBackBtn = root.findViewById(R.id.arrowBackBtn);

        // add onclick listeners
        arrowBackBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        if(view == arrowBackBtn){
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}