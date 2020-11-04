package com.example.atomizer2_0.ui.main;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.atomizer2_0.Adapter.CustomDialog;
import com.example.atomizer2_0.Adapter.MoreHistoryAdapter;
import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.example.atomizer2_0.MainActivity.historyData;

public class GenericFragment2 extends Fragment implements View.OnClickListener{
    private Button historyButton,settingButton,afterSaleButton,descriptionButton,remoteButton,parameterButton;
    private LinearLayout homeButton;
    private Fragment contactFragment;
    private DescriptionFragment descriptionFragment;
    private SystemSettingFragment systemSettingFragment;
    private LinearLayout linearLayout;
    private LinearLayout moreHistoryLinearLayout;
    private LinearLayout descriptionLinearLayout;
    private LinearLayout systemSettingLinearLayout;
    private boolean moreHistoryFlag=false;
    private boolean descriptionFlag=false;
    private boolean systemSettingFlag=false;
    private int viewFLag=0;

    //morehistory
    private ListView moreHistoryList;
    private Button viewButton;
    private SearchView searchView;
    private MoreHistoryAdapter moreHistoryAdapter;
    private Filter filter;
    private List<RoomData> historyDataFilter = new ArrayList<RoomData>();
    private List<RoomData> historyDataFilter2 = new ArrayList<RoomData>();
    private ArrayAdapter<String> roomAdapter;
    private List<String> roomItems;
    private ArrayAdapter<String> dateAdapter;
    private List<String> dateItems;
    private ArrayAdapter<String> modeAdapter;
    private List<String> modeItems;
    private Spinner roomSpinner;
    private Spinner modeSpinner;
    private Spinner dateSpinner;
    private Spinner rankSpinner;
    private Button buttonSubmit;
    private boolean flag=false;
    private boolean rankFlag=false;

    //systemSetting
    private SeekBar seekBarLight;


    public static GenericFragment2 newInstance() {
        return new GenericFragment2();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.more_setting_fragment;
        MainActivity.lastFragmentId=R.id.more_setting_fragment;
        View root= inflater.inflate(R.layout.generic_fragment2, container, false);
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
        linearLayout=root.findViewById(R.id.linearLayout);
        moreHistoryLinearLayout=loadMoreHistoryView();
        viewFLag=1;
        moreHistoryFlag=true;
        historyButton.setTextColor(Color.parseColor("#C4C4C4"));

        return root;
    }
    private LinearLayout loadMoreHistoryView(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout  layout = (LinearLayout) inflater.inflate(
                R.layout.more_history, null).findViewById(R.id.more_history_fragment);
        linearLayout.addView(layout);
        moreHistoryList = layout.findViewById(R.id.moreHistoryList);
        roomSpinner=layout.findViewById(R.id.roomSpinner);
        modeSpinner=layout.findViewById(R.id.modeSpinner);
        dateSpinner=layout.findViewById(R.id.dateSpinner);
        buttonSubmit=layout.findViewById(R.id.buttonSubmit);
        rankSpinner=layout.findViewById(R.id.rankSpinner);

        searchView = (SearchView) layout.findViewById(R.id.sv);
        //设置SearchView自动缩小为图标
        searchView.setIconifiedByDefault(false);//设为true则搜索栏 缩小成俄日一个图标点击展开
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        //设置默认提示文字
        searchView.setQueryHint("");

        //配置监听器
        //viewButton=root.findViewById(R.id.viewButton);
        historyDataFilter.clear();
        historyDataFilter=historyData;
        moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyDataFilter,genericHandler);
        moreHistoryList.setAdapter(moreHistoryAdapter);
        flag=false;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //单机搜索按钮时激发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际应用中应该在该方法内执行实际查询，此处仅使用Toast显示用户输入的查询内容
                return false;
            }

            //用户输入字符时激发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                historyDataFilter.clear();
                //如果newText不是长度为0的字符串
                if (TextUtils.isEmpty(newText)) {
                    //清除ListView的过滤
                    moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyData,genericHandler);
                    moreHistoryList.setAdapter(moreHistoryAdapter);
                    flag=false;
                } else {
                    //使用用户输入的内容对ListView的列表项进行过滤
                    //moreHistoryList.setFilterText(newText);
                    for (int i=0;i<historyData.size();i++){
                        RoomData roomDataItem = null;
                        if (historyData.get(i).getMode().contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                        if ((historyData.get(i).getRoomTime()+"").contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                        if (historyData.get(i).getPrincipal().contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                        if (historyData.get(i).getTaskData().contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                        if (historyData.get(i).getRoomName().contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                        if ((historyData.get(i).getRoomArea()+"").contains(newText)){
                            roomDataItem=historyData.get(i);
                            historyDataFilter.add(roomDataItem);
                            continue;
                        }else {

                        }
                    }
                    Log.e("tag","historyDataFilter:"+historyDataFilter.size());
                    moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item,historyData,genericHandler);
                    moreHistoryList.setAdapter(moreHistoryAdapter);
                    flag=false;
                }
                return true;
            }
        });
        modeItems=new ArrayList<String>();
        modeItems.add("未选择");
        modeItems.add("快速消毒");
        modeItems.add("广谱消毒");
        modeItems.add("专业消毒");
        modeAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,modeItems);

        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeAdapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
                TextView tv = (TextView)view;
                tv.setTextSize(23.0f);    //设置大小
                tv.setTextColor(Color.parseColor("#EEEEEE"));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });




        roomItems=new ArrayList<String>();
        roomItems.add("未选择");
        for (int i=0;i<historyDataFilter.size();i++){
            roomItems.add(historyDataFilter.get(i).getRoomName());
        }

        roomItems=removeDuplicate(roomItems);
        Collections.swap(roomItems, roomItems.size()-1, 0);
        Log.e("tag",roomItems.get(0));
        roomAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,roomItems);

        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomSpinner.setAdapter(roomAdapter);

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
                TextView tv = (TextView)view;
                tv.setTextSize(23.0f);    //设置大小
                tv.setTextColor(Color.parseColor("#EEEEEE"));
                if (i!=0){


                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        dateItems=new ArrayList<String>();
        dateItems.add("未选择");
        for (int i=0;i<historyDataFilter.size();i++){
            dateItems.add(historyDataFilter.get(i).getTaskData().substring(0,10));
        }
        dateItems=removeDuplicate(dateItems);
        Collections.swap(dateItems, dateItems.size()-1, 0);
        dateAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,dateItems);

        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
                TextView tv = (TextView)view;
                tv.setTextSize(23.0f);    //设置大小
                tv.setTextColor(Color.parseColor("#EEEEEE"));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        rankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
                TextView tv = (TextView)view;
                tv.setTextSize(23.0f);    //设置大小
                tv.setTextColor(Color.parseColor("#EEEEEE"));
                if (i==0){
                    if (!rankFlag){//正序

                    }else {
                        Collections.reverse(historyDataFilter);
                        Collections.reverse(historyDataFilter2);
                        moreHistoryAdapter.notifyDataSetChanged();
                        rankFlag=false;
                    }

                }else {
                    if (rankFlag){//反序

                    }else {
                        Collections.reverse(historyDataFilter);
                        Collections.reverse(historyDataFilter2);
                        moreHistoryAdapter.notifyDataSetChanged();
                        rankFlag=true;
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyDataFilter2.clear();
                if (roomSpinner.getSelectedItemPosition()!=0&&
                        modeSpinner.getSelectedItemPosition()!=0&&
                        dateSpinner.getSelectedItemPosition()!=0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getRoomName().equals(roomItems.get(roomSpinner.getSelectedItemPosition()))&&
                                historyDataFilter.get(j).getMode().equals(modeItems.get(modeSpinner.getSelectedItemPosition()))&&
                                historyDataFilter.get(j).getTaskData().substring(0,10).equals(dateItems.get(dateSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()!=0&&
                        modeSpinner.getSelectedItemPosition()==0&&
                        dateSpinner.getSelectedItemPosition()==0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getRoomName().equals(roomItems.get(roomSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()!=0&&
                        modeSpinner.getSelectedItemPosition()!=0&&
                        dateSpinner.getSelectedItemPosition()==0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getRoomName().equals(roomItems.get(roomSpinner.getSelectedItemPosition()))&&
                                historyDataFilter.get(j).getMode().equals(modeItems.get(modeSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()==0&&
                        modeSpinner.getSelectedItemPosition()!=0&&
                        dateSpinner.getSelectedItemPosition()!=0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getMode().equals(modeItems.get(modeSpinner.getSelectedItemPosition()))&&
                                historyDataFilter.get(j).getTaskData().substring(0,10).equals(dateItems.get(dateSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()!=0&&
                        modeSpinner.getSelectedItemPosition()==0&&
                        dateSpinner.getSelectedItemPosition()!=0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getRoomName().equals(roomItems.get(roomSpinner.getSelectedItemPosition()))&&
                                historyDataFilter.get(j).getTaskData().substring(0,10).equals(dateItems.get(dateSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()==0&&
                        modeSpinner.getSelectedItemPosition()!=0&&
                        dateSpinner.getSelectedItemPosition()==0){
                    for (int j=0;j<historyDataFilter.size();j++){
                        RoomData roomDataItem = null;
                        if (historyDataFilter.get(j).getMode().equals(modeItems.get(modeSpinner.getSelectedItemPosition()))){
                            roomDataItem=historyDataFilter.get(j);
                            historyDataFilter2.add(roomDataItem);
                        }else {

                        }
                    }

                }
                else if (roomSpinner.getSelectedItemPosition()==0&&
                        modeSpinner.getSelectedItemPosition()==0&&
                        dateSpinner.getSelectedItemPosition()==0){

                    historyDataFilter2=historyDataFilter;
                }

                moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyDataFilter2,genericHandler);
                moreHistoryList.setAdapter(moreHistoryAdapter);
                flag=true;
            }
        });
        return layout;

    }
    private LinearLayout loadDescriptionView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.description, null).findViewById(R.id.description_fragment);
        linearLayout.addView(layout);
        return layout;
    }
    private LinearLayout loadSystemSettingView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        LinearLayout layout = (LinearLayout) inflater.inflate(
                R.layout.system_setting, null).findViewById(R.id.system_setting_fragment);
        linearLayout.addView(layout);
        seekBarLight=layout.findViewById(R.id.seekBarLight);
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
        return layout;
    }

    public List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.historyButton:
                if (viewFLag==2){
                    descriptionLinearLayout.setVisibility(View.GONE);
                }
                else if (viewFLag==3){
                    systemSettingLinearLayout.setVisibility(View.GONE);
                }
                moreHistoryLinearLayout.setVisibility(View.VISIBLE);
                viewFLag=1;
                descriptionButton.setTextColor(Color.parseColor("#ffffff"));
                historyButton.setTextColor(Color.parseColor("#C4C4C4"));
                settingButton.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.settingButton:
                if (viewFLag==1){
                    moreHistoryLinearLayout.setVisibility(View.GONE);
                }
                else if (viewFLag==2){
                    descriptionLinearLayout.setVisibility(View.GONE);
                }
                if (!systemSettingFlag){
                    systemSettingLinearLayout=loadSystemSettingView();
                    systemSettingFlag=true;
                }
                viewFLag=3;
                systemSettingLinearLayout.setVisibility(View.VISIBLE);
                settingButton.setTextColor(Color.parseColor("#C4C4C4"));
                historyButton.setTextColor(Color.parseColor("#ffffff"));
                descriptionButton.setTextColor(Color.parseColor("#ffffff"));
                break;
            case R.id.afterSaleButton:
                break;
            case R.id.descriptionButton:
                if (viewFLag==1){
                    moreHistoryLinearLayout.setVisibility(View.GONE);
                }
                else if (viewFLag==3){
                    systemSettingLinearLayout.setVisibility(View.GONE);
                }
                if (!descriptionFlag){
                    descriptionLinearLayout=loadDescriptionView();
                    descriptionFlag=true;
                }
                descriptionLinearLayout.setVisibility(View.VISIBLE);
                viewFLag=2;

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
    public Handler genericHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                RoomData roomData=(RoomData)msg.obj;
                CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
                builder.setMessage("消毒模式："+roomData.getMode()+"    负责人："+roomData.getPrincipal()+"   空间体积："+roomData.getRoomArea()+"\n"+
                        "消毒时间："+roomData.getTaskData()+"   消毒时长:"+roomData.getRoomTime());
                builder.setTitleText("任务名称："+roomData.getRoomName());
                builder.setPositiveButton("打印", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //设置你的操作事项
                        //Toast.makeText(MainActivity.this,"queding",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("取消",
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(MainActivity.this,"queding",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }

        }
    };
}
