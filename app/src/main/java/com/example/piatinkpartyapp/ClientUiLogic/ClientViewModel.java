package com.example.piatinkpartyapp.ClientUiLogic;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.networking.GameClient;

import java.io.IOException;
import java.util.ArrayList;

public class ClientViewModel extends ViewModel {
    private GameClient client = GameClient.getInstance();

    public ClientViewModel() throws IOException { }

    public String getPlayerID() {
        return String.valueOf(client.getPlayerID());
    }

    public MutableLiveData<ArrayList<ChatMessage>> getChatMessages() { return client.getChatMessages(); }

    public void sendToAllChatMessage(String message) {
        client.sendToAll(message);
    }
}
