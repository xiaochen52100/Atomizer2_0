package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.nowRoomData;

public class SystemSettingFragment extends Fragment implements View.OnClickListener{
    private MoreHistoryViewModel mMoreHistoryViewModel;
    private SeekBar seekBarLight;
    public static SystemSettingFragment newInstance() {
        return new SystemSettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.system_setting, container, false);
        seekBarLight=root.findViewById(R.id.seekBarLight);
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        seekBarLight.setProgress(screenBrightness);
        seekBarLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Window localWindow = getActivity().getWindow();
                WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
                float f = seekBar.getProgress() / 255.0F;
                localLayoutParams.screenBrightness = f;
                localWindow.setAttributes(localLayoutParams);
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoreHistoryViewModel = ViewModelProviders.of(this).get(MoreHistoryViewModel.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}