package com.example.piatinkpartyapp.chat;

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

import java.util.concurrent.ThreadLocalRandom;


public class CheatInfoDialogFragment extends DialogFragment {
    private static final String[] emojis = new String[]{"\uD83D\uDE05", "\uD83D\uDE02", "\uD83D\uDE1B", "\uD83D\uDE0B", "\uD83E\uDD2B", "\uD83E\uDD2D"};


    public interface CheatDialogInformationHandler {
        void handleCheatingInformation(String cheatCode, int expectedCounterForCheatWindow);
    }

    public CheatDialogInformationHandler cheatDialogInformationHandler;


    private String getPersonalRandomCheatCode() {
        // +1 is to make it inclusive
        int numberOfEmojis = ThreadLocalRandom.current().nextInt(1, 3 + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfEmojis; i++) {
            sb.append(emojis[ThreadLocalRandom.current().nextInt(0, emojis.length)]);
        }
        return sb.toString();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_cheating_info, null);

        Animation animShake = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        customLayout.startAnimation(animShake);

        int expectedCounterForCheatWindow = ThreadLocalRandom.current().nextInt(2, 3 + 1);
        String cheatCode = getPersonalRandomCheatCode();
        final ScrollView myScroll = new ScrollView(getContext());
        myScroll.addView(customLayout);

        return new AlertDialog.Builder(requireContext())
                .setTitle("Informationen für das Cheaten!")
                .setView(myScroll)
                .setMessage("Wie in den meisten Spielen kannst du auch hier cheaten." +
                        " Ob du das willst hängt von dir ab?"  +
                        "\n\nUm das Cheat Window einzuschalten musst du den Cheat Code " + expectedCounterForCheatWindow + " mal in den Chat eingeben." +
                        "\n\nDein Cheat Code lautet: " + cheatCode +
                        "\n\nACHTUNG : Du musst dir den Cheat Code merken ! Er wird dir nicht wieder angezeigt !")
                .setNeutralButton("Verstanden! ", (dialog, which) -> {
                    cheatDialogInformationHandler.handleCheatingInformation(cheatCode, expectedCounterForCheatWindow);
                })
                .create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            cheatDialogInformationHandler = (CheatDialogInformationHandler) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e("CheatDialog", "onAttach: ClassCastExcetption: " + e.getMessage());
        }
    }

}
