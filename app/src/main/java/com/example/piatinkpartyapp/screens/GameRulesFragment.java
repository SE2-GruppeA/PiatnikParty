package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piatinkpartyapp.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;


public class GameRulesFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private Button BtnBackGameRules;
    private Button BtnSchnopsn;
    private Button BtnWattn;
    private Button BtnHosnObe;
    private Button BtnPensionisteln;
    private Button BtnCards;
    ArrayList<Button> ruleButtons;

    private TextView schonpsn_rules;
    private TextView wattn_rules;
    private TextView hosnObe_rules;
    private TextView pensionistln_rules;
    ArrayList<TextView> rules;

    private ImageView karo_sieben;
    private  ImageView kreuz_acht;
    private  ImageView pick_neun;
    private  ImageView herz_zehn;
    private  ImageView herz_unter;
    private ImageView herz_ober;
    private  ImageView herz_koenig;
    private  ImageView herz_ass;
    private ArrayList<ImageView> cardViews;

    public GameRulesFragment() { }

    public static GameRulesFragment newInstance(String param1, String param2) {
        GameRulesFragment fragment = new GameRulesFragment();
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

    private void setCard(ImageView imgView){

        imgView.setImageResource(getResId(imgView.getContentDescription().toString()));
        imgView.setVisibility(View.INVISIBLE);
        setCardListener(imgView.getContentDescription().toString(),imgView);
    }
    public static int getResId(String resName) {

        try {

            Field idField = R.drawable.class.getDeclaredField(resName);
            Log.d("##############", idField.getName());
            return idField.getInt(idField);
        } catch (Exception e) {
            //e.printStackTrace();
            return -1;
        }
    }
    private void setCardsVisibility(int visibility){
        for(ImageView imageView:cardViews){
            imageView.setVisibility(visibility);

        }
    }

    private void hideTextViewsExcept(TextView textView){
        for(TextView t : rules){
            if(t!= textView ){
                t.setVisibility(View.INVISIBLE);
            }else{
                t.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_rules, container, false);
        BtnBackGameRules = (Button) view.findViewById(R. id. buttonBackGameRules);
        BtnBackGameRules.setOnClickListener(this);
        cardViews = new ArrayList<>();
        ruleButtons = new ArrayList<>();
        rules = new ArrayList<>();

        schonpsn_rules = view.findViewById(R.id.editTextTextSchnopsn);
        rules.add(schonpsn_rules);
        wattn_rules = view.findViewById(R.id.editTextTextWattn);
        rules.add(wattn_rules);
        hosnObe_rules = view.findViewById(R.id.editTextTextHosnObe);
        rules.add(hosnObe_rules);
        pensionistln_rules = view.findViewById(R.id.editTextTextPensionistln);
        rules.add(pensionistln_rules);
        hideTextViewsExcept(null);

        BtnHosnObe = (Button) view.findViewById(R.id.buttonHosnObe);
        BtnSchnopsn = (Button)view.findViewById(R.id.buttonSchnopsn);
        BtnWattn = (Button)view.findViewById(R.id.buttonWattn);
        BtnPensionisteln = (Button)view.findViewById(R.id.buttonPensionistln);
        BtnCards = (Button)view.findViewById(R.id.buttonKarten);

        karo_sieben = view.findViewById(R.id.imageViewKaroSieben);
        cardViews.add(karo_sieben);

        kreuz_acht = view.findViewById(R.id.imageViewKreuzAcht);
        cardViews.add(kreuz_acht);

        pick_neun = view.findViewById(R.id.imageViewPickNeun);
        cardViews.add(pick_neun);

        herz_zehn = view.findViewById(R.id.imageViewHerzZehn);
        cardViews.add(herz_zehn);

        herz_unter = view.findViewById(R.id.imageViewHerzUnter);
        cardViews.add(herz_unter);

        herz_ober = view.findViewById(R.id.imageViewHerzOber);
        cardViews.add(herz_ober);

        herz_koenig = view.findViewById(R.id.imageViewHerzKoenig);
        cardViews.add(herz_koenig);

        herz_ass = view.findViewById(R.id.imageViewHerzAss);
        cardViews.add(herz_ass);

        for(ImageView imageView : cardViews){
            setCard(imageView);
        }

        BtnCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            hideTextViewsExcept(null);
            setCardsVisibility(View.VISIBLE);
            }
        });


        BtnHosnObe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideTextViewsExcept(hosnObe_rules);
                setCardsVisibility(View.INVISIBLE);
            }
        });

        BtnPensionisteln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hideTextViewsExcept(pensionistln_rules);
               setCardsVisibility(View.INVISIBLE);
            }
        });

        BtnSchnopsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideTextViewsExcept(schonpsn_rules);
                setCardsVisibility(View.INVISIBLE);
            }
        });

        BtnWattn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideTextViewsExcept(wattn_rules);
                setCardsVisibility(View.INVISIBLE);

            }
        });
        return  view;
    }

    private void setCardListener(String desc, ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] s = desc.split("_");
                Toast.makeText(getContext(), s[0].toUpperCase(Locale.ROOT) + " " + s[1].toUpperCase(Locale.ROOT), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}