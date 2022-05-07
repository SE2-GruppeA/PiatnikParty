package com.example.piatinkpartyapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.piatinkpartyapp.cards.Card;
import com.example.piatinkpartyapp.networking.GameClient;
import com.example.piatinkpartyapp.networking.GameServer;
import com.example.piatinkpartyapp.networking.NetworkHandler;
import com.example.piatinkpartyapp.networking.Packets;
import com.example.piatinkpartyapp.screens.WaitingPlayersFragment;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameViewModel extends ViewModel {

    private GameServer server;
    private GameClient client;

    public void createGameServer(String ip) {
        NetworkHandler.GAMESERVER_IP = ip;
        server = new GameServer();
        try {
            server.startNewGameServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        client = new GameClient(ip);
    }

    public void joinGameServer(String ip) {
        client = new GameClient(ip);
    }

    public void startGame() {
        client.startGame();
    }

    public void setCard(Card card) {
        client.setCard(card);
    }

    public GameClient getClient() {
        return client;
    }

    public GameServer getServer() {
        return server;
    }

    public void setClient(GameClient client) {
        this.client = client;
    }

    public void setServer(GameServer server) {
        this.server = server;
    }

    public static String getIp() {

        String ipAddress = null;

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            System.out.println("The IP address could not be found!!!");
        }

        return ipAddress;
    }

    public static class ListAdapter extends BaseAdapter {

        GameClient gameClient = new GameClient(getIp());

        WaitingPlayersFragment waitingPlayersFragment = new WaitingPlayersFragment();
        Context context = waitingPlayersFragment.getContext();

        int player = gameClient.getPlayerID();

        private ArrayList<Integer> data;

        public ListAdapter(Context context, ArrayList<Integer> data) {
            this.data = data;
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public GameClient getGameClient() {
            return gameClient;
        }

        public int getPlayer() {
            return player;
        }

        public List<Integer> getData() {
            return data;
        }

        public WaitingPlayersFragment getWaitingPlayersFragment() {
            return waitingPlayersFragment;
        }

        @Override
        public CharSequence[] getAutofillOptions() {
            return super.getAutofillOptions();
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setGameClient(GameClient gameClient) {
            this.gameClient = gameClient;
        }

        public void setPlayer(int player) {
            this.player = player;
        }

        public void setWaitingPlayersFragment(WaitingPlayersFragment waitingPlayersFragment) {
            this.waitingPlayersFragment = waitingPlayersFragment;
        }

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
