package com.example.piatinkpartyapp.chat.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.piatinkpartyapp.R;


// should be called in LiveData isCheater Handler !!
public class IsCheaterDialogFragment extends DialogFragment {
    boolean isCheater;

    public IsCheaterDialogFragment(boolean isCheater) {
        this.isCheater = isCheater;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_cheating, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(customLayout);
        if (isCheater) {
            builder
                    .setTitle("Exposed Cheater !\uD83E\uDD29\uD83E\uDD29")
                    .setMessage("Gut gemacht ! \uD83E\uDD73\uD83E\uDD73\uD83E\uDD73" +
                            "\n Du hast einen Cheater expoed !" +
                            "\n Dir werden +5 Punkte gutgeschrieben." +
                            "\n und dem Cheater werden -20 Punkte abgezogen.")
                    .setNeutralButton("Schade !", (dialog, which) -> {

                    });
        } else {
            builder
                    .setTitle("Agghh doch kein Cheater !!! \uD83D\uDE31\uD83D\uDE31\uD83E\uDD76")
                    .setMessage("Uh kann jeden passieren! \uD83D\uDE35" +
                            "\n Du hast zu schnell reagiert - zu impulsive! \uD83E\uDD21" +
                            "\n Dir werden -5 Punkte abgezogen." +
                            "\n Nächstes mal denk lieber besser nach !")
                    .setNeutralButton("Juhuu ! ", (dialog, which) -> {

                    });
        }
        AlertDialog alertDialog = builder.create();
        return alertDialog;
    }
}