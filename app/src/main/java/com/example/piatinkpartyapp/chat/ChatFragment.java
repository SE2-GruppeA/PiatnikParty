package com.example.piatinkpartyapp.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.ClientUiLogic.ClientViewModel;
import com.example.piatinkpartyapp.databinding.FragmentChatBinding;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.utils.Utils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    private ArrayList<ChatMessage> chatMessages;
    private FragmentChatBinding binding;
    private ClientViewModel model;
    ChatAdapter chatAdapter;

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
        chatMessages = new ArrayList<>();
        setRecyclerViewWithDummyData();

        chatAdapter = new ChatAdapter(chatMessages);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        binding.rvChatMessages.setLayoutManager(layoutManager);
        binding.rvChatMessages.setItemAnimator(new DefaultItemAnimator());
        binding.rvChatMessages.setAdapter(chatAdapter);
    }

    private void setRecyclerViewWithDummyData() {
        chatMessages.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm", ChatMessage.MessageType.OUT));
        chatMessages.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm", ChatMessage.MessageType.IN));
        chatMessages.add(new ChatMessage("Player 2", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm", ChatMessage.MessageType.OUT));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.arrowBackBtn.setOnClickListener(this);

        /* TEST */
        binding.startServerBtn.setOnClickListener(v -> startServer(v));

        setUpChatRecyclerView();
        binding.button2.setOnClickListener(v -> onClick_SendChatMessage(v));
        return binding.getRoot();
    }

    GameServer s;
    private void startServer(View v)  {
        s = new GameServer();
        try {
            s.startNewGameServer();
            Thread.sleep(5000);
            setUpChatObserver();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void setUpChatObserver() {
        model = new ViewModelProvider(this).get(ClientViewModel.class);
        final Observer<ChatMessage> newChatMessageObserver = newChatMessage -> {
            addChatMessageToRecyclerView(newChatMessage);
        };
        try {
            model.getNewChatMessage().observe(getViewLifecycleOwner(), newChatMessageObserver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void addChatMessageToRecyclerView(ChatMessage message) {
        chatMessages.add(message);
        chatAdapter.notifyDataSetChanged();
        binding.rvChatMessages.scrollToPosition(chatMessages.size() - 1);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.arrowBackBtn) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }


    private void onClick_SendChatMessage(View v) {
        String message = binding.etChatMessage.getText().toString();
        System.out.println(message);

        addChatMessageToRecyclerView(new ChatMessage(model.getPlayerID(), message, Utils.getDateAsString(), ChatMessage.MessageType.IN));
        model.sendToAllChatMessage(message);
    }
}