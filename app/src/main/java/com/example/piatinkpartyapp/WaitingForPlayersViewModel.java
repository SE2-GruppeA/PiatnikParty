package com.example.piatinkpartyapp;

import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class WaitingForPlayersViewModel extends ViewModel {

    private MutableLiveData<String> mutableLiveData;

    public MutableLiveData<String> getIp() {
        String ipAddress = null;

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ipAddress = inetAddress.getHostAddress().toString();
                    }
                    if (ipAddress != null) {
                        mutableLiveData = new MutableLiveData<>();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }

        return mutableLiveData;
    }

}
