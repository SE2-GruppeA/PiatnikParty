package com.example.piatinkpartyapp.chat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.piatinkpartyapp.R;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class ExposeDialogFragment extends DialogFragment {
    public interface ExposeDialogHandler {
        void handleExpose(Boolean doExpose);
    }

    public ExposeDialogHandler exposeDialogHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_expose_cheater, null);

        final ScrollView myScroll = new ScrollView(getContext());
        myScroll.addView(customLayout);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Informationen für das Exposen!")
                .setView(myScroll)
                .setMessage(
                        "Wie in den meisten Spielen gibt es Cheater" +
                                "\nDas ist hier auch der Fall" +
                                "\nDenkst du das ein Spieler cheatet ?" +
                                "\nDu kannst exposen, mit einem großen ABER !" +
                                "\nWenn der Spieler doch nicht cheatet, werden die Punkte abgezogen !" +
                                "\nÜbe die Funktionalität mit bedacht aus !")

                .setNegativeButton("Exposen ", (dialog, which) -> exposeDialogHandler.handleExpose(true))
                .setPositiveButton("Lieber nicht ! \uD83D\uDE05 ", (dialog, which) -> {
                    exposeDialogHandler.handleExpose(false);
                })
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            exposeDialogHandler = (ExposeDialogHandler) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("ExposeDialog", "onAttach: ClassCastExcetption: " + e.getMessage());
        }
    }
}
