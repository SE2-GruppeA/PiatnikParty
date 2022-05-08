package com.example.piatinkpartyapp.ClientUiLogic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.chat.ChatMessage;
import com.example.piatinkpartyapp.networking.GameClient;

import java.io.IOException;

public class ClientViewModel extends ViewModel {
    private GameClient client = GameClient.getInstance();

    public ClientViewModel() throws IOException {
    }

    public String getPlayerID() {
        return String.valueOf(client.getPlayerID());
    }

    public LiveData<ChatMessage> getNewChatMessage() throws IOException {
        return client.getNewChatMessage();
    }

    public void sendToAllChatMessage(String message) {
        //client.sendEndToEndMessage(message, 1);
        client.sendToAll(message);
    }

}
