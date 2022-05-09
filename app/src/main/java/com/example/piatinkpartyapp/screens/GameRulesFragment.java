package com.example.piatinkpartyapp.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piatinkpartyapp.R;


public class GameRulesFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private Button BtnBackGameRules;
    private Button BtnSchnopsn;
    private Button BtnWattn;
    private Button BtnHosnObe;
    private Button BtnPens;
    private Button BtnCards;

    private TextView schonpsn_rules;
    private TextView wattn_rules;
    private TextView hosnObe_rules;
    private TextView pensionistln_rules;

    private ImageView kasi;
    private  ImageView krac;
    private  ImageView pn;
    private  ImageView hz;
    private  ImageView hu;
    private ImageView ho;
    private  ImageView hk;
    private  ImageView ha;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_rules, container, false);
        BtnBackGameRules = (Button) view.findViewById(R. id. buttonBackGameRules);
        BtnBackGameRules.setOnClickListener(this);

        kasi = view.findViewById(R.id.imageViewKaSi);
        kasi.setVisibility(View.INVISIBLE);
        kasi.setImageResource(R.drawable.karo_sieben);
        kasi.setContentDescription("KARO SIEBEN");
        setCardListener(kasi.getContentDescription().toString(),kasi);

        krac = view.findViewById(R.id.imageViewKrAc);
        krac.setVisibility(View.INVISIBLE);
        krac.setImageResource(R.drawable.kreuz_acht);
        krac.setContentDescription("KREUZ ACHT");
        setCardListener(krac.getContentDescription().toString(), krac);


        pn = view.findViewById(R.id.imageViewPN);
        pn.setVisibility(View.INVISIBLE);
        pn.setImageResource(R.drawable.pick_neun);
        pn.setContentDescription("PICK NEUN");
        setCardListener(pn.getContentDescription().toString(), pn);


        hz = view.findViewById(R.id.imageViewHZ);
        hz.setVisibility(View.INVISIBLE);
        hz.setImageResource(R.drawable.herz_zehn);
        hz.setContentDescription("HERZ ZEHN");
        setCardListener(hz.getContentDescription().toString(),hz);

        hu = view.findViewById(R.id.imageViewHU);
        hu.setVisibility(View.INVISIBLE);
        hu.setImageResource(R.drawable.herz_unter);
        hu.setContentDescription("HERZ UNTER");
        setCardListener(hu.getContentDescription().toString(),hu);

        ho = view.findViewById(R.id.imageViewHO);
        ho.setVisibility(View.INVISIBLE);
        ho.setImageResource(R.drawable.herz_ober);
        ho.setContentDescription("HERZ OBER");
        setCardListener(ho.getContentDescription().toString(),ho);

        hk = view.findViewById(R.id.imageViewHK);
        hk.setVisibility(View.INVISIBLE);
        hk.setImageResource(R.drawable.herz_koenig);
        hk.setContentDescription("HERZ KÖNIG");
        setCardListener(hk.getContentDescription().toString(),hk);

        ha = view.findViewById(R.id.imageViewHA);
        ha.setContentDescription("HERZ ASS");
        ha.setImageResource(R.drawable.herz_ass);
        ha.setVisibility(View.INVISIBLE);
        setCardListener(ha.getContentDescription().toString(),ha);


        BtnCards = (Button) view.findViewById(R.id.buttonKarten);

        BtnCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hosnObe_rules.setVisibility(View.INVISIBLE);
                pensionistln_rules.setVisibility(View.INVISIBLE);
                schonpsn_rules.setVisibility(View.INVISIBLE);
                wattn_rules.setVisibility(View.INVISIBLE);
                kasi.setVisibility(View.VISIBLE);
                krac.setVisibility(View.VISIBLE);
                pn.setVisibility(View.VISIBLE);
                hz.setVisibility(View.VISIBLE);
                hu.setVisibility(View.VISIBLE);
                ho.setVisibility(View.VISIBLE);
                hk.setVisibility(View.VISIBLE);
                ha.setVisibility(View.VISIBLE);
            }
        });
        BtnHosnObe = (Button) view.findViewById(R.id.buttonHosnObe);
        BtnHosnObe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                hosnObe_rules.setVisibility(View.VISIBLE);
                schonpsn_rules.setVisibility(View.INVISIBLE);
                wattn_rules.setVisibility(View.INVISIBLE);
                pensionistln_rules.setVisibility(View.INVISIBLE);

                kasi.setVisibility(View.INVISIBLE);
                krac.setVisibility(View.INVISIBLE);
                pn.setVisibility(View.INVISIBLE);
                hz.setVisibility(View.INVISIBLE);
                hu.setVisibility(View.INVISIBLE);
                ho.setVisibility(View.INVISIBLE);
                hk.setVisibility(View.INVISIBLE);
                ha.setVisibility(View.INVISIBLE);
            }
        });
        BtnPens = (Button) view.findViewById(R.id.buttonPensionistln);
        BtnPens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pensionistln_rules.setVisibility(View.VISIBLE);
                schonpsn_rules.setVisibility(View.INVISIBLE);
                wattn_rules.setVisibility(View.INVISIBLE);
                hosnObe_rules.setVisibility(View.INVISIBLE);

                kasi.setVisibility(View.INVISIBLE);
                krac.setVisibility(View.INVISIBLE);
                pn.setVisibility(View.INVISIBLE);
                hz.setVisibility(View.INVISIBLE);
                hu.setVisibility(View.INVISIBLE);
                ho.setVisibility(View.INVISIBLE);
                hk.setVisibility(View.INVISIBLE);
                ha.setVisibility(View.INVISIBLE);
            }
        });
        BtnSchnopsn = (Button) view.findViewById(R.id.buttonSchnopsn);
        BtnSchnopsn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                schonpsn_rules.setVisibility(View.VISIBLE);
                wattn_rules.setVisibility(View.INVISIBLE);
                pensionistln_rules.setVisibility(View.INVISIBLE);
                hosnObe_rules.setVisibility(View.INVISIBLE);
                kasi.setVisibility(View.INVISIBLE);
                krac.setVisibility(View.INVISIBLE);
                pn.setVisibility(View.INVISIBLE);
                hz.setVisibility(View.INVISIBLE);
                hu.setVisibility(View.INVISIBLE);
                ho.setVisibility(View.INVISIBLE);
                hk.setVisibility(View.INVISIBLE);
                ha.setVisibility(View.INVISIBLE);
            }
        });
        BtnWattn = view.findViewById(R.id.buttonWattn);
        BtnWattn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wattn_rules.setVisibility(View.VISIBLE);
                schonpsn_rules.setVisibility(View.INVISIBLE);
                pensionistln_rules.setVisibility(View.INVISIBLE);
                hosnObe_rules.setVisibility(View.INVISIBLE);

                kasi.setVisibility(View.INVISIBLE);
                krac.setVisibility(View.INVISIBLE);
                pn.setVisibility(View.INVISIBLE);
                hz.setVisibility(View.INVISIBLE);
                hu.setVisibility(View.INVISIBLE);
                ho.setVisibility(View.INVISIBLE);
                hk.setVisibility(View.INVISIBLE);
                ha.setVisibility(View.INVISIBLE);

            }
        });

        schonpsn_rules = view.findViewById(R.id.editTextTextSchnopsn);
        schonpsn_rules.setVisibility(View.INVISIBLE);

        wattn_rules = view.findViewById(R.id.editTextTextWattn);
        wattn_rules.setVisibility(View.INVISIBLE);

        hosnObe_rules = view.findViewById(R.id.editTextTextHosnObe);
       hosnObe_rules.setVisibility(View.INVISIBLE);


        pensionistln_rules = view.findViewById(R.id.editTextTextPensionistln);
        pensionistln_rules.setVisibility(View.INVISIBLE);



        return  view;
    }

    private void setCardListener(String desc, ImageView img){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), desc, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }
}