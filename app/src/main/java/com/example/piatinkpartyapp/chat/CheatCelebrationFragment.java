package com.example.piatinkpartyapp.chat;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.piatinkpartyapp.R;


import static nl.dionsegijn.konfetti.core.Position.Relative;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class CheatCelebrationFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View customLayout = getLayoutInflater().inflate(R.layout.fragment_cheat_celebration, null);
        final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.angel_vs_evil);
        Shape.DrawableShape drawableShape = new Shape.DrawableShape(drawable, true);
        KonfettiView konfettiView = (KonfettiView) customLayout.findViewById(R.id.konfettiView);

        EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
        Party party = new PartyFactory(emitterConfig)
                .angle(270)
                .spread(90)
                .setSpeedBetween(1f, 5f)
                .timeToLive(2000L)
                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                .sizes(new Size(12, 5f, 0.2f))
                .position(0.0, 0.0, 1.0, 0.0)
                .build();

        for (int i = 0; i < 100; i++) {
            konfettiView.start(
                    new PartyFactory(emitterConfig)
                            .angle(Angle.BOTTOM)
                            .spread(Spread.ROUND)
                            .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                            .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                            .setSpeedBetween(0f, 15f)
                            .position(new Relative(0.0, 0.0).between(new Relative(1.0, 0.0)))
                            .build()
            );
        }

        return new AlertDialog.Builder(requireContext())
                .setView(customLayout)
                .create();
    }

    /*
    put in Fragment
    public void cheatCelebrationWindow(){
        new Thread(()->{
            CheatCelebrationFragment dialog = new CheatCelebrationFragment();
            // I know this is considered deprecated but I could not find any other way to solve this
            dialog.setTargetFragment(ChatFragment.this, 1);
            dialog.show(getFragmentManager(), TAG + "CheatDialogFragment");

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
        }).start();

    }
     */
}