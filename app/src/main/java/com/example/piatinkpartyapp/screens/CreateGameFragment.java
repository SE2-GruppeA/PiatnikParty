package com.example.piatinkpartyapp.screens;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.piatinkpartyapp.R;
import com.example.piatinkpartyapp.networking.NetworkHandler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class CreateGameFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button ButtonBack;
    private Button ButtonStartLobby;


    public CreateGameFragment(){}

    public static CreateGameFragment newInstance(String param1, String param2) {
        CreateGameFragment fragment = new CreateGameFragment();
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
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_create_game, container, false);

        //add views
        ButtonBack = root.findViewById(R.id.buttonBack);
        ButtonStartLobby = root. findViewById(R. id. buttonStartLobby);

        //add onclick listeners
        ButtonBack.setOnClickListener(this);
        ButtonStartLobby.setOnClickListener(this);

        TextView textView1 = root.findViewById(R.id.textView);
        TextView textView2 = root.findViewById(R.id.textView2);

        //set the local ip as gameserver ip, we need to do this because the ClientViewModel autmatically
        //creates an instance of gameclient
        //needs to be relocated !!!

        WifiManager wm = (WifiManager) getActivity().getSystemService(getActivity().WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        NetworkHandler.GAMESERVER_IP = ip;

        textView1.setText("Your IP Address is:");
        textView2.setText(ip);

        return root;
    }

    @Override
    public void onClick(View view) {
        if (view == ButtonBack) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } else if(view == ButtonStartLobby) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(android.R.id.content,
                    new WaitingPlayersFragment()).commit();
        }
    }

    public String getIp() {

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
}