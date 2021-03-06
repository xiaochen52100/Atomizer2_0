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
import android.widget.LinearLayout;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.barButton;
import static com.example.atomizer2_0.MainActivity.modeText;

public class MainFragment extends Fragment implements View.OnClickListener{

    private MainViewModel mViewModel;
    private LinearLayout buttonQuick,buttonBroad,buttonProfession,buttonSetting;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.main_fragment2, container, false);
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
        modeText.setVisibility(View.GONE);
//        MainActivity.barTitle.setText("主界面");
//        barButton.setImageResource(R.mipmap.arrow_bold_left_128);


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
//                barButton.setVisibility(View.VISIBLE);
                modeText.setVisibility(View.VISIBLE);
                modeText.setText("当前模式：快速消毒");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, QuickDisinfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonBroad:
 //               barButton.setVisibility(View.VISIBLE);
                modeText.setVisibility(View.VISIBLE);
                modeText.setText("当前模式：广谱消毒");
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonProfession:
                modeText.setVisibility(View.VISIBLE);
                modeText.setText("当前模式：专业消毒");
//                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                        .commitNow();
                break;
            case R.id.buttonSetting:
                modeText.setVisibility(View.VISIBLE);
                modeText.setText("其它设置");
//                barButton.setVisibility(View.VISIBLE);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, GenericFragment2.newInstance())
                        .commitNow();
//                MainActivity.checkList.clear();
//                for (int i=0;i<MainActivity.historyData.size();i++){
//                    MainActivity.checkList.add(false);
//                }
                break;
        }
    }
}