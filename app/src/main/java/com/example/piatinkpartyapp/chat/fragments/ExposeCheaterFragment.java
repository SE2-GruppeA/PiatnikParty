package com.example.piatinkpartyapp.chat.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.piatinkpartyapp.clientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExposeCheaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExposeCheaterFragment extends Fragment implements View.OnClickListener {
    ImageButton closeExposeBtn;
    ClientViewModel clientViewModel;

    Button exposeBtn;
    Spinner spinner_playerIds;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExposeCheaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExposeCheaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExposeCheaterFragment newInstance(String param1, String param2) {
        ExposeCheaterFragment fragment = new ExposeCheaterFragment();
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
        View root = inflater.inflate(R.layout.fragment_expose_cheater2, container, false);

        closeExposeBtn = root.findViewById(R.id.closeExposeBtn);
        exposeBtn = root.findViewById(R.id.exposeBtn);

        closeExposeBtn.setOnClickListener(this);
        exposeBtn.setOnClickListener(this);

        clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        spinner_playerIds = (Spinner) root.findViewById(R.id.spinner_playerIds);
        setUpSpinner();

        return root;
    }

    private void setUpSpinner() {
        // todo : optional get player id's but for now it's fine
        String[] arraySpinner = new String[] {
                "1", "2", "3", "4"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_selectable_list_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_playerIds.setAdapter(adapter);

        // default first player, so we don't have to do null checks
        spinner_playerIds.setSelection(1);
    }

    @Override
    public void onClick(View view) {
        if(view == closeExposeBtn){
            closeVotingDialog();
        } if(view == exposeBtn){
            exposePossibleCheater();
        }
    }

    private void exposePossibleCheater() {
        TextView textView = (TextView)spinner_playerIds.getSelectedView();
        String playerId = textView.getText().toString();

        //todo: live data
        clientViewModel.exposePossibleCheater(Integer.parseInt(playerId));
    }

    public void closeVotingDialog(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}