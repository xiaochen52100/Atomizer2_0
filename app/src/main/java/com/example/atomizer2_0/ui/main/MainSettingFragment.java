package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.barButton;

public class MainSettingFragment extends Fragment implements View.OnClickListener{
    private MainSettingViewModel mMainSettingViewModel;
    private Button historyButton,settingButton,afterSaleButton,descriptionButton,remoteButton,parameterButton;
    public static MainSettingFragment newInstance() {
        return new MainSettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.more_setting_fragment;
        MainActivity.lastFragmentId=R.id.more_setting_fragment;
        MainActivity.barTitle.setText("其它设置");
        View root= inflater.inflate(R.layout.main_setting, container, false);
        historyButton=root.findViewById(R.id.historyButton);
        settingButton=root.findViewById(R.id.settingButton);
        afterSaleButton=root.findViewById(R.id.afterSaleButton);
        descriptionButton=root.findViewById(R.id.descriptionButton);
        remoteButton=root.findViewById(R.id.remoteButton);
        parameterButton=root.findViewById(R.id.parameterButton);
        historyButton.setOnClickListener(this);
        parameterButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainSettingViewModel = ViewModelProviders.of(this).get(MainSettingViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.historyButton:
                break;
            case R.id.settingButton:
                break;
            case R.id.afterSaleButton:
                break;
            case R.id.descriptionButton:
                break;
            case R.id.remoteButton:
                break;
            case R.id.parameterButton:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TemplateFragment.newInstance())
                        .commitNow();
                break;
        }
    }
}
