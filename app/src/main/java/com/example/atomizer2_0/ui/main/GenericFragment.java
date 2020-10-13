package com.example.atomizer2_0.ui.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

public class GenericFragment extends Fragment implements View.OnClickListener{
    private Button historyButton,settingButton,afterSaleButton,descriptionButton,remoteButton,parameterButton;
    private LinearLayout homeButton;
    private Fragment contactFragment;
    private MoreHistoryFragment moreHistoryFragment;
    private DescriptionFragment descriptionFragment;
    private SystemSettingFragment systemSettingFragment;
    public static GenericFragment newInstance() {
        return new GenericFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.more_setting_fragment;
        MainActivity.lastFragmentId=R.id.more_setting_fragment;
        View root= inflater.inflate(R.layout.generic_fragment, container, false);
        historyButton=root.findViewById(R.id.historyButton);
        settingButton=root.findViewById(R.id.settingButton);
        afterSaleButton=root.findViewById(R.id.afterSaleButton);
        descriptionButton=root.findViewById(R.id.descriptionButton);
        remoteButton=root.findViewById(R.id.remoteButton);
        parameterButton=root.findViewById(R.id.parameterButton);
        homeButton=root.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        descriptionButton.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        if (moreHistoryFragment==null){
            moreHistoryFragment=new MoreHistoryFragment();
        }
        if (descriptionFragment==null){
            descriptionFragment=new DescriptionFragment();
        }
        if (systemSettingFragment==null){
            systemSettingFragment=new SystemSettingFragment();
        }

        descriptionButton.setTextColor(Color.parseColor("#C4C4C4"));

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.historyButton:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment, moreHistoryFragment)
                        .commit();
                descriptionButton.setTextColor(Color.parseColor("#ffffff"));
                historyButton.setTextColor(Color.parseColor("#C4C4C4"));
                settingButton.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.settingButton:
                getChildFragmentManager().beginTransaction().hide(descriptionFragment);
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment, systemSettingFragment)
                        .commit();
                settingButton.setTextColor(Color.parseColor("#C4C4C4"));
                historyButton.setTextColor(Color.parseColor("#ffffff"));
                descriptionButton.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.afterSaleButton:
                break;
            case R.id.descriptionButton:
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.fragment, descriptionFragment)
                        .commit();
                descriptionButton.setTextColor(Color.parseColor("#C4C4C4"));
                historyButton.setTextColor(Color.parseColor("#ffffff"));
                settingButton.setTextColor(Color.parseColor("#ffffff"));

                break;
            case R.id.remoteButton:
                break;
            case R.id.parameterButton:
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, TemplateFragment.newInstance())
//                        .commitNow();
                break;
            case R.id.homeButton:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commitNow();
                break;
        }
    }
}
