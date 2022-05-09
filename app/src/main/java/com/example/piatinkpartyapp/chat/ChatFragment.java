package com.example.piatinkpartyapp.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.databinding.FragmentChatBinding;
import com.example.piatinkpartyapp.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentChatBinding binding;
    private ClientViewModel model;
    private ChatAdapter chatAdapter;

    public ChatFragment() {
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
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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

    private void setUpChatRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvChatMessages.setLayoutManager(layoutManager);
        binding.rvChatMessages.setItemAnimator(new DefaultItemAnimator());
        binding.rvChatMessages.setAdapter(chatAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.arrowBackBtn.setOnClickListener(this);
        binding.button2.setOnClickListener(v -> onClick_SendChatMessage(v));
        /*
        ORDER OF THIS CODE IS IMPORTANT !!!!

        First retrieve view model, because we need an instance, so we can retrieve our LiveData
        to instantiate other objects such as an adapter !

        and use getActivity, so the ViewModel lives in the Activity Container and not in it's own,
        therefore will not be created "new" every time... but live so long till the mother activity
        closes.
         */
        model = new ViewModelProvider(getActivity()).get(ClientViewModel.class);

        // NOTE - IMPORTANT : instantiate adapter, then define observer, else exception !
        chatAdapter = new ChatAdapter(model.getChatMessages().getValue());
        setUpChatRecyclerView();

        /*
        this will be called instant, when fragment gets created !!! so you have to instantiate
        your liveData else you will get an exception
         */
        model.getChatMessages().observe(getActivity(), newMessage -> addChatMessageToRecyclerView());
        return binding.getRoot();
    }

    private void addChatMessageToRecyclerView() {
        chatAdapter.notifyDataSetChanged();
        recyclerViewScrollDown();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.arrowBackBtn) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    private void onClick_SendChatMessage(View v) {
        final ChatMessage newChatMessage = new ChatMessage(
                model.getPlayerID(),
                binding.etChatMessage.getText().toString(),
                Utils.getDateAsString(),
                ChatMessage.MessageType.IN
        );
        model.getChatMessages().getValue().add(newChatMessage);
        chatAdapter.notifyDataSetChanged();
        recyclerViewScrollDown();
        model.sendToAllChatMessage(newChatMessage.getMessage());
    }

    private void recyclerViewScrollDown(){
        binding.rvChatMessages.scrollToPosition(model.getChatMessages().getValue().size()-1);
    }
}