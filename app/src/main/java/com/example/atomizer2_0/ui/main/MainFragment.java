package com.example.atomizer2_0.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.barButton;

public class MainFragment extends Fragment implements View.OnClickListener{

    private MainViewModel mViewModel;
    private Button buttonQuick,buttonBroad,buttonProfession,buttonSetting;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.main_fragment, container, false);
        buttonQuick=root.findViewById(R.id.buttonQuick);
        buttonBroad=root.findViewById(R.id.buttonBroad);
        buttonProfession=root.findViewById(R.id.buttonProfession);
        buttonSetting=root.findViewById(R.id.buttonSetting);
        buttonQuick.setOnClickListener(this);
        buttonBroad.setOnClickListener(this);
        buttonProfession.setOnClickListener(this);
        buttonSetting.setOnClickListener(this);
        MainActivity.nowFragmentId=R.id.main;
        MainActivity.lastFragmentId=R.id.main;
        MainActivity.barTitle.setText("主界面");
        barButton.setImageResource(R.mipmap.arrow_bold_left_128);


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonQuick:
                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, QuickDisinfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonBroad:
                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonProfession:
                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonSetting:
                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainSettingFragment.newInstance())
                        .commitNow();
                break;
        }
    }
}