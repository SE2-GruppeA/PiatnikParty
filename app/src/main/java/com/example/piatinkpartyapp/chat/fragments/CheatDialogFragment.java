package com.example.piatinkpartyapp.chat.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.piatinkpartyapp.R;

public class CheatDialogFragment extends DialogFragment {
    public interface CheatDialogOutputHandler {
        void handleCheatingChoice(Boolean cheating);
    }

    public CheatDialogOutputHandler cheatDialogOutputHandler;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_cheating, null);

        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        customLayout.startAnimation(animShake);

        return new AlertDialog.Builder(requireContext())
                .setView(customLayout)
                .setTitle("Achtung ! - Cheat Window")
                .setMessage("Du hast das Cheat Window akktiviert!\nWillst du wirklich cheaten ?\nEinmal ein Cheater immer ein Cheater ! \uD83D\uDE08\uD83D\uDE08 ")
                .setPositiveButton("Ja, ich will cheaten !", (dialog, which) -> {
                    cheatDialogOutputHandler.handleCheatingChoice(true);
                })
                .setNegativeButton("Nein, nie im Leben", (dialog, which) -> {
                    cheatDialogOutputHandler.handleCheatingChoice(false);
                })
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            cheatDialogOutputHandler = (CheatDialogOutputHandler) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("CheatDialog", "onAttach: ClassCastExcetption: " + e.getMessage());
        }
    }
}