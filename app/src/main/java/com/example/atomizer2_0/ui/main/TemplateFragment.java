package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.Adapter.RoomAdapter;
import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.sharedPreferenceUtil;
import static com.example.atomizer2_0.MainActivity.templateRoomData;

public class TemplateFragment extends Fragment implements View.OnClickListener{
    private TemplateViewModel mTemplateViewModel;
    private RoomAdapter templateRoomAdapter;
    private ListView roomList;
    private Button addNewRoomButton;
    private Button backButton;
    private LinearLayout homeButton;
    public static TemplateFragment newInstance() {
        return new TemplateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.template, container, false);
//        MainActivity.barTitle.setText("参数设定");
        MainActivity.nowFragmentId=R.id.template_fragment;
        roomList=(ListView)root.findViewById(R.id.templateList);
        addNewRoomButton=root.findViewById(R.id.addNewRoomButton);
        backButton=root.findViewById(R.id.backButton);
        addNewRoomButton.setOnClickListener(this);
        templateRoomAdapter=new RoomAdapter(getContext(),R.layout.moulde_item, templateRoomData,mHandler);
        roomList.setAdapter(templateRoomAdapter);
        backButton.setOnClickListener(this);
        homeButton=root.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTemplateViewModel = ViewModelProviders.of(this).get(TemplateViewModel.class);
        // TODO: Use the ViewModel

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.moreHistoryButton:
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, MoreHistoryFragment.newInstance())
//                        .commitNow();
//                break;
            case R.id.addNewRoomButton:
                templateRoomData.add(new RoomData("广谱消毒","李","房间"+(templateRoomData.size()+1),20,20,50));
                sharedPreferenceUtil.writeObject(getContext(),"RoomList",templateRoomData);
                templateRoomAdapter.notifyDataSetChanged();
                break;
            case R.id.backButton:
                if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
                    getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                        .commitNow();
                }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                            .commitNow();
                }
                break;
            case R.id.homeButton:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commitNow();
                break;
        }
    }
    private Handler mHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                        .commitNow();
            }else if(msg.what == 2){
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                        .commitNow();
            }


        }
    };
}
