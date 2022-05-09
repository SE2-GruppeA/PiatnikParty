package com.example.piatinkpartyapp.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.SchnopsnFragment;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.NetworkHandler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayGameFragment extends Fragment implements View.OnClickListener {

    private Button backBtn;
    private Button connectButton;
    //private ViewModel viewModel;
    private EditText editText;
    private TextView textView;

    ClientViewModel clientViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayGameFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_play_game, container, false);

        textView = (TextView) root.findViewById(R.id.textView6);

        backBtn = (Button) root.findViewById(R.id.returnButton);
        backBtn.setOnClickListener(this);

        connectButton = (Button) root.findViewById(R.id.button3);
        connectButton.setOnClickListener(this);
        editText = (EditText) root.findViewById(R.id.editTextTextPersonName);

        return root;
    }

    private void waitForGameStart() {
        getActivity().getSupportFragmentManager().beginTransaction().add(android.R.id.content,
                new SchnopsnFragment()).commit();
    }

    @Override
    public void onClick(View view) {

        if (view == backBtn)
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        else if (view == connectButton) {
            NetworkHandler.GAMESERVER_IP =  editText.getText().toString();

            clientViewModel = new ViewModelProvider(getActivity()).get(ClientViewModel.class);
            clientViewModel.getConnectionState().observe(getActivity(), connectionState -> setConnectedSuccessfuly(connectionState));
            clientViewModel.isGameStarted().observe(getActivity(), gameStarted -> waitForGameStart());
        }

    }

    public void setConnectedSuccessfuly(boolean connectionSate) {
        if(connectionSate) {
            textView.setText("Erforlgreich verbunden!!!");
        }else{
            textView.setText("Verbindung fehlgeschlagen");
        }
    }

}