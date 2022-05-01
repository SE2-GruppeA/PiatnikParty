package com.example.piatinkpartyapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.piatinkpartyapp.databinding.FragmentChatBinding;

import java.util.ArrayList;

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

        ChatMessageAdapter chatMessageAdapter = new ChatMessageAdapter(chatMessages);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        binding.rvChatMessages.setLayoutManager(layoutManager);
        binding.rvChatMessages.setItemAnimator(new DefaultItemAnimator());
        binding.rvChatMessages.setAdapter(chatMessageAdapter);
    }

    private void setRecyclerViewWithDummyData() {
        chatMessages.add(new ChatMessage("Player 1", "Hello Valon - Player2 \uD83D\uDE02\uD83D\uDE02\uD83D\uDE02\uD83D\uDE02 \uD83D\uDE08", "today at 10:30 pm",false));
        chatMessages.add(new ChatMessage("Player 2", "Hello Player 1, whats up ?", "today at 10:35 pm", true));
        chatMessages.add(new ChatMessage("Player 1", "Nothing much hbu \uD83D\uDE02\uD83D\uDE02? ", "today at 10:36 pm",false));
        chatMessages.add(new ChatMessage("Player 2", "I good thx ! \uD83D\uDE02\uD83D\uDE02? ", "today at 10:38 pm",true));
        chatMessages.add(new ChatMessage("Player 2", "I'm kinda hungry \uD83E\uDD24\uD83E\uDD24\uD83E\uDD24 ", "today at 10:39 pm",true));
        chatMessages.add(new ChatMessage("Player 2", "Ye I feel ya, fasting is hard \uD83D\uDE14\uD83D\uDE14 ", "today at 10:41 pm",false));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.arrowBackBtn.setOnClickListener(this);
        setUpChatRecyclerView();
        return binding.getRoot();
    }


    @Override
    public void onClick(View view) {
        if (view == binding.arrowBackBtn) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}