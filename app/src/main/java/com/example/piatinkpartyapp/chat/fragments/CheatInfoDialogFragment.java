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


public class CheatInfoDialogFragment extends DialogFragment {
    private static final String[] emojis = new String[]{"\uD83D\uDE05", "\uD83E\uDD29", "\uD83D\uDE0A", "\uD83D\uDE02", "\uD83D\uDE1B", "\uD83D\uDE0B", "\uD83E\uDD2B", "\uD83E\uDD2D"};
    private Random r = new Random();

    public interface CheatDialogInformationHandler {
        void handleCheatingInformation(String cheatCode, int expectedCounterForCheatWindow);
    }

    public CheatDialogInformationHandler cheatDialogInformationHandler;


    private String getPersonalRandomCheatCode() {
        // +1 is to make it inclusive
        int numberOfEmojis = r.nextInt(3 - 1 + 1) + 1;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfEmojis; i++) {
            sb.append(emojis[r.nextInt(emojis.length)]);
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
                .setTitle("Informationen f??r das Cheaten!")
                .setView(myScroll)
                .setMessage("Wie in den meisten Spielen kannst du auch hier cheaten." +
                        " Ob du das willst h??ngt von dir ab?" +
                        "\nUm das Cheat Window einzuschalten musst du den Cheat Code " + expectedCounterForCheatWindow + " mal in den Chat eingeben." +
                        "\nDein Cheat Code lautet: " + cheatCode +
                        "\nACHTUNG : Du musst dir den Cheat Code merken ! Er wird dir nicht wieder angezeigt !")
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
