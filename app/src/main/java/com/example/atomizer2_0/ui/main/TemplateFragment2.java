package com.example.atomizer2_0.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.Adapter.MoreHistoryAdapter;
import com.example.atomizer2_0.Adapter.MoreHistoryAdapter2;
import com.example.atomizer2_0.Adapter.RoomAdapter;
import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.atomizer2_0.MainActivity.historyData;
import static com.example.atomizer2_0.MainActivity.nowRoomData;
import static com.example.atomizer2_0.MainActivity.sharedPreferenceUtil;
import static com.example.atomizer2_0.MainActivity.templateRoomData;

public class TemplateFragment2 extends Fragment implements View.OnClickListener{
    private TemplateViewModel mTemplateViewModel;
    private RoomAdapter templateRoomAdapter;
    private ListView roomList;
    private Button addNewRoomButton;
    private LinearLayout homeButton;
    private ListView moreHistoryList;
    private EditText roomArea;
    private EditText roomName;
    private SeekBar seekBarRoomTime;
    private Button confirmBtn;
    private Button useLwhBtn;
    private EditText editRoomTime;
    private Spinner modeSpinner;
    private TextView modeTextView;
    private EditText editRoomHeight,editRoomWidth,editRoomLength;
    private boolean useLwh=true;
    private MoreHistoryAdapter2 moreHistoryAdapter;
    private static RoomData roomDataTemplate;
    private List<RoomData> historyDataFilter = new ArrayList<RoomData>();
    public static TemplateFragment2 newInstance() {
        return new TemplateFragment2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.template_2, container, false);
//        MainActivity.barTitle.setText("参数设定");
        MainActivity.nowFragmentId=R.id.template_fragment_2;
        addNewRoomButton=root.findViewById(R.id.addNewRoomButton);
        addNewRoomButton.setOnClickListener(this);

        roomArea=(EditText)root.findViewById(R.id.roomArea);
        roomName=(EditText) root.findViewById(R.id.roomName);
        seekBarRoomTime=(SeekBar)root.findViewById(R.id.seekBarRoomTime);
        confirmBtn=root.findViewById(R.id.confirmBtn);
        editRoomTime=(EditText)root.findViewById(R.id.editRoomTime);
//        modeSpinner=(Spinner)root.findViewById(R.id.modeSpinner);
        modeTextView=(TextView)root.findViewById(R.id.modeTextView);
        editRoomHeight=(EditText)root.findViewById(R.id.editRoomHeight);
        editRoomWidth=(EditText)root.findViewById(R.id.editRoomWidth);
        editRoomLength=(EditText)root.findViewById(R.id.editRoomLength);
        useLwhBtn=(Button)root.findViewById(R.id.useLwhBtn);
        confirmBtn.setOnClickListener(this);
        useLwhBtn.setOnClickListener(this);
        if (roomDataTemplate==null){
            if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
                modeTextView.setText("广谱消毒");
                roomDataTemplate=new RoomData("广谱消毒"," ","房间1",20,7,50);
            }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
                modeTextView.setText("专业消毒");
                roomDataTemplate=new RoomData("专业消毒"," ","房间1",20,7,50);
            }
        }
        if (roomDataTemplate!=null){
            roomName.setText(roomDataTemplate.getRoomName());
            roomArea.setText(roomDataTemplate.getRoomArea()+"");
            seekBarRoomTime.setProgress(roomDataTemplate.getRoomProcess());
            editRoomTime.setText(roomDataTemplate.getRoomTime()+"");
            if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
                modeTextView.setText("广谱消毒");
                roomDataTemplate.setMode("广谱消毒");
            }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
                modeTextView.setText("专业消毒");
                roomDataTemplate.setMode("专业消毒");
            }
        }
//        homeButton=root.findViewById(R.id.homeButton);
//        homeButton.setOnClickListener(this);
        historyDataFilter.clear();
        if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
            for (int j=0;j<historyData.size();j++){
                RoomData roomDataItem = null;
                //Log.e("tag",historyData.get(j).getMode());
                if (historyData.get(j).getMode().equals("广谱消毒")){
                    roomDataItem=historyData.get(j);
                    historyDataFilter.add(roomDataItem);
                }else {

                }
            }

        } else if (MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
            for (int j=0;j<historyData.size();j++){
                RoomData roomDataItem = null;
                //Log.e("tag",historyData.get(j).getMode());
                if (historyData.get(j).getMode().equals("专业消毒")){
                    roomDataItem=historyData.get(j);
                    historyDataFilter.add(roomDataItem);
                }else {

                }
            }

        }
        moreHistoryList = root.findViewById(R.id.moreHistoryList);
        moreHistoryAdapter=new MoreHistoryAdapter2(getContext(),R.layout.more_history_item2, historyDataFilter);
        moreHistoryList.setAdapter(moreHistoryAdapter);

        moreHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("tag",moreHistoryAdapter.getItem(position).getRoomName());
                if (!moreHistoryAdapter.getItem(position).getMode().equals("快速消毒")){
                    roomDataTemplate=moreHistoryAdapter.getItem(position);
                    roomName.setText(roomDataTemplate.getRoomName());
                    roomArea.setText(roomDataTemplate.getRoomArea()+"");
                    seekBarRoomTime.setProgress(roomDataTemplate.getRoomProcess());
                    editRoomTime.setText(roomDataTemplate.getRoomTime()+"");

                    if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
                        modeTextView.setText("广谱消毒");
                    }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
                        modeTextView.setText("专业消毒");
                    }

                }


            }
        });



//        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
//                TextView tv = (TextView)view;
//                tv.setTextSize(23.0f);    //设置大小
//                tv.setTextColor(Color.parseColor("#EEEEEE"));
//
//                if (i==0){
//                    roomDataTemplate.setMode("广谱消毒");
//                }else{
//                    roomDataTemplate.setMode("专业消毒");
//                }
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent){}
//        });

        seekBarRoomTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int roomAreas=Integer.parseInt(roomArea.getText().toString());
                int roomTimes=0;
                if (roomAreas>0&&roomAreas<100){
                    roomTimes=7;
                }
                else if(roomAreas>=100&&roomAreas<300){
                    roomTimes=13;
                }
                else if(roomAreas>=300&&roomAreas<1000){
                    roomTimes=23;
                }
                else if(roomAreas>=1000&&roomAreas<=3000){
                    roomTimes=45;
                }
                double timeDouble=((seekBar.getProgress()-50)/100.00)*(roomTimes)+(roomTimes);
                int time=(int)timeDouble;
                editRoomTime.setText(time+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //MainActivity.roomData.get(position).getRoomArea()/2
                int roomAreas=Integer.parseInt(roomArea.getText().toString());
                int roomTimes=0;
                if (roomAreas>0&&roomAreas<100){
                    roomTimes=7;
                }
                else if(roomAreas>=100&&roomAreas<300){
                    roomTimes=13;
                }
                else if(roomAreas>=300&&roomAreas<1000){
                    roomTimes=23;
                }
                else if(roomAreas>=1000&&roomAreas<=3000){
                    roomTimes=45;
                }
                double timeDouble=((seekBar.getProgress()-50)/100.00)*(roomTimes)+(roomTimes);
                int time=(int)timeDouble;
                editRoomTime.setText(time+"");
                roomDataTemplate.setRoomTime(time);
//                templateRoomData.get(position).setRoomTime(time);
//                templateRoomData.get(position).setRoomProcess(seekBar.getProgress());
//                MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);

            }
        });

        editRoomHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!editRoomLength.getText().toString().equals("")){
//                    if (!editRoomHeight.getText().toString().equals("")){
//                        if (!editRoomWidth.getText().toString().equals("")){
//                            int roomAreas=Integer.parseInt(editRoomHeight.getText().toString())*Integer.parseInt(editRoomWidth.getText().toString())*Integer.parseInt(editRoomLength.getText().toString());
//                            int roomTimes=0;
//                            if (roomAreas>0&&roomAreas<100){
//                                roomTimes=7;
//                            }
//                            else if(roomAreas>=100&&roomAreas<300){
//                                roomTimes=13;
//                            }
//                            else if(roomAreas>=300&&roomAreas<1000){
//                                roomTimes=23;
//                            }
//                            else if(roomAreas>=1000&&roomAreas<=3000){
//                                roomTimes=45;
//                            }
//                            seekBarRoomTime.setProgress(50);
//                            editRoomTime.setText(roomTimes+"");
//                            roomArea.setText(roomAreas+"");
//                        }
//                    }
//                }
            }
        });

        editRoomWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!editRoomLength.getText().toString().equals("")){
//                    if (!editRoomHeight.getText().toString().equals("")){
//                        if (!editRoomWidth.getText().toString().equals("")){
//                            int roomAreas=Integer.parseInt(editRoomHeight.getText().toString())*Integer.parseInt(editRoomWidth.getText().toString())*Integer.parseInt(editRoomLength.getText().toString());
//                            int roomTimes=0;
//                            if (roomAreas>0&&roomAreas<100){
//                                roomTimes=7;
//                            }
//                            else if(roomAreas>=100&&roomAreas<300){
//                                roomTimes=13;
//                            }
//                            else if(roomAreas>=300&&roomAreas<1000){
//                                roomTimes=23;
//                            }
//                            else if(roomAreas>=1000&&roomAreas<=3000){
//                                roomTimes=45;
//                            }
//                            seekBarRoomTime.setProgress(50);
//                            editRoomTime.setText(roomTimes+"");
//                            roomArea.setText(roomAreas+"");
//                        }
//                    }
//                }

            }
        });

        editRoomLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (!editRoomLength.getText().toString().equals("")){
//                    if (!editRoomHeight.getText().toString().equals("")){
//                        if (!editRoomWidth.getText().toString().equals("")){
//                            int roomAreas=Integer.parseInt(editRoomHeight.getText().toString())*Integer.parseInt(editRoomWidth.getText().toString())*Integer.parseInt(editRoomLength.getText().toString());
//                            int roomTimes=0;
//                            if (roomAreas>0&&roomAreas<100){
//                                roomTimes=7;
//                            }
//                            else if(roomAreas>=100&&roomAreas<300){
//                                roomTimes=13;
//                            }
//                            else if(roomAreas>=300&&roomAreas<1000){
//                                roomTimes=23;
//                            }
//                            else if(roomAreas>=1000&&roomAreas<=3000){
//                                roomTimes=45;
//                            }
//                            seekBarRoomTime.setProgress(50);
//                            editRoomTime.setText(roomTimes+"");
//                            roomArea.setText(roomAreas+"");
//                        }
//                    }
//                }


            }
        });

        roomName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!roomName.getText().toString().equals("")){
                    roomDataTemplate.setRoomName(roomName.getText().toString());
                }

                //templateRoomData.get(position).setRoomName(roomName.getText().toString());
                //MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);
            }
        });
        roomArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!roomArea.getText().toString().equals("")){
                    int roomAreas=Integer.parseInt(roomArea.getText().toString());
                    int roomTimes=0;
                    if (roomAreas>0&&roomAreas<100){
                        roomTimes=7;
                    }
                    else if(roomAreas>=100&&roomAreas<300){
                        roomTimes=13;
                    }
                    else if(roomAreas>=300&&roomAreas<1000){
                        roomTimes=23;
                    }
                    else if(roomAreas>=1000&&roomAreas<=3000){
                        roomTimes=45;
                    }
                    seekBarRoomTime.setProgress(50);
                    editRoomTime.setText(roomTimes+"");
                    roomDataTemplate.setRoomTime(roomTimes);
                    roomDataTemplate.setRoomArea(roomAreas);

                }

            }
        });

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
//                templateRoomData.add(new RoomData("广谱消毒","李","房间"+(templateRoomData.size()+1),20,20,50));
//                sharedPreferenceUtil.writeObject(getContext(),"RoomList",templateRoomData);
//                templateRoomAdapter.notifyDataSetChanged();
                roomDataTemplate=new RoomData("广谱消毒"," ","房间1",20,7,50);
                roomName.setText(roomDataTemplate.getRoomName());
                roomArea.setText(roomDataTemplate.getRoomArea()+"");
                seekBarRoomTime.setProgress(roomDataTemplate.getRoomProcess());
                editRoomTime.setText(roomDataTemplate.getRoomTime()+"");

                if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
                    modeTextView.setText("广谱消毒");
                    roomDataTemplate.setMode("广谱消毒");
                }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
                    modeTextView.setText("专业消毒");
                    roomDataTemplate.setMode("专业消毒");
                }

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
            case R.id.confirmBtn:
//                nowRoomData=roomDataTemplate;
//                if (MainActivity.lastFragmentId==R.id.broad_disinfection_fragment){
//                    //nowRoomData.setMode("广谱消毒");
//                    Log.e("TAG","time  "+nowRoomData.getRoomTime());
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, BroadDisnfectionFragment.newInstance())
//                            .commitNow();
//                }else if(MainActivity.lastFragmentId==R.id.profession_disinfection_fragment){
//                    //nowRoomData.setMode("专业消毒");
//                    Log.e("TAG","time  "+nowRoomData.getRoomTime());
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
//                            .commitNow();
//                }
                nowRoomData=roomDataTemplate;
                if (roomDataTemplate.getMode().equals("广谱消毒")){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                            .commitNow();
                }else if(roomDataTemplate.getMode().equals("专业消毒")){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                            .commitNow();
                }
                break;
            case R.id.useLwhBtn:
                if (useLwh){
                    //useLwhBtn.setEnabled(false);
//                    editRoomHeight.setEnabled(false);
//                    editRoomLength.setEnabled(false);
//                    editRoomWidth.setEnabled(false);
                    useLwh=false;
                }else {
                    //useLwhBtn.setEnabled(true);
//                    editRoomWidth.setEnabled(true);
//                    editRoomLength.setEnabled(true);
//                    editRoomHeight.setEnabled(true);
                    if (!editRoomLength.getText().toString().equals("")){
                        if (!editRoomHeight.getText().toString().equals("")){
                            if (!editRoomWidth.getText().toString().equals("")){
                                int roomAreas=Integer.parseInt(editRoomHeight.getText().toString())*Integer.parseInt(editRoomWidth.getText().toString())*Integer.parseInt(editRoomLength.getText().toString());
                                int roomTimes=0;
                                if (roomAreas>0&&roomAreas<100){
                                    roomTimes=7;
                                }
                                else if(roomAreas>=100&&roomAreas<300){
                                    roomTimes=13;
                                }
                                else if(roomAreas>=300&&roomAreas<1000){
                                    roomTimes=23;
                                }
                                else if(roomAreas>=1000&&roomAreas<=3000){
                                    roomTimes=45;
                                }
                                seekBarRoomTime.setProgress(50);
                                editRoomTime.setText(roomTimes+"");
                                roomArea.setText(roomAreas+"");
                                roomDataTemplate.setRoomTime(roomTimes);
                            }
                        }
                    }
                    useLwh=true;
                }
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
