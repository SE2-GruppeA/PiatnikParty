package com.example.piatinkpartyapp.screens;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.piatinkpartyapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EinstellungenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EinstellungenFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Button backBtn;
    private SeekBar seekBar;
    private ContentResolver contentResolver;
    private Window window;
    private int brightness;
    private TextView percentageView;

    private SeekBar seekBarVolume;
    private TextView percantageViewVolume;
    private int volume;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EinstellungenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EinstellungenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EinstellungenFragment newInstance(String param1, String param2) {
        EinstellungenFragment fragment = new EinstellungenFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_einstellungen, container, false);

        backBtn = (Button) root.findViewById(R.id.button);
        backBtn.setOnClickListener(this);

        seekBar = (SeekBar) root.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);

        seekBarVolume = (SeekBar) root.findViewById(R. id. seekBarVolume);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Settings.System.canWrite(getContext());

                if (i <= 1) {
                    volume = 1;
                } else {
                    volume = i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                boolean grantPermissions = Settings.System.canWrite(getContext());
                //Settings.ACTION_MANAGE_WRITE_SETTINGS;
                AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

                if (!grantPermissions) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    startActivity(intent);

                    Toast.makeText(getActivity().getApplicationContext(),
                            "The permissions could be granted", Toast.LENGTH_SHORT).show();

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Now you can change the screen brightness", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Screen brightness changed to " + volume, Toast.LENGTH_SHORT).show();

                    Log.e("Error", "The permissions could be granted");
                }

                am.setStreamVolume(AudioManager.STREAM_MUSIC,volume,0);

            }
        });

        seekBarVolume.setMax(100);

        percentageView = (TextView) root.findViewById(R.id.percentageView);

        contentResolver = requireActivity().getContentResolver();

        window = requireActivity().getWindow();

        seekBar.setMax(100);

        try {
            brightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("Error","Brightness cannot be changed");
            //e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Brightness cannot be changed", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Settings.System.canWrite(getContext());

        if (progress <= 1) {
            brightness = 1;
        } else {
            brightness = progress;
        }

        float percentage = (brightness / (float) 100) * 100;

        percentageView.setText((int) percentage + " %");


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Nothing here
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        boolean grantPermissions = Settings.System.canWrite(getContext());
        //Settings.ACTION_MANAGE_WRITE_SETTINGS;

        if (!grantPermissions) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(intent);

            Toast.makeText(getActivity().getApplicationContext(),
                    "The permissions could be granted", Toast.LENGTH_SHORT).show();

            Toast.makeText(getActivity().getApplicationContext(),
                    "Now you can change the screen brightness", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Screen brightness changed to " + brightness, Toast.LENGTH_SHORT).show();

            Log.e("Error", "The permissions could be granted");
        }

        Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);

        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.screenBrightness = brightness / (float) 100;

        window.setAttributes(layoutParams);
    }
}