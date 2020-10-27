package com.example.atomizer2_0.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.Adapter.HistoryAdapter;
import com.example.atomizer2_0.Adapter.MoreHistoryAdapter;
import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.atomizer2_0.MainActivity.historyData;

public class MoreHistoryFragment extends Fragment {
    private MoreHistoryViewModel mMoreHistoryViewModel;
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
    public static MoreHistoryFragment newInstance() {
        return new MoreHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.more_history_fragment;
        //MainActivity.barTitle.setText("历史记录");
        View root=inflater.inflate(R.layout.more_history, container, false);
        moreHistoryList = root.findViewById(R.id.moreHistoryList);
        roomSpinner=root.findViewById(R.id.roomSpinner);
        modeSpinner=root.findViewById(R.id.modeSpinner);
        dateSpinner=root.findViewById(R.id.dateSpinner);
        buttonSubmit=root.findViewById(R.id.buttonSubmit);
        rankSpinner=root.findViewById(R.id.rankSpinner);

        searchView = (SearchView) root.findViewById(R.id.sv);
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
        moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyDataFilter);
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
                    moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyData);
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
                    moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyDataFilter);
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

                moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyDataFilter2);
                moreHistoryList.setAdapter(moreHistoryAdapter);
                flag=true;
            }
        });



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoreHistoryViewModel = ViewModelProviders.of(this).get(MoreHistoryViewModel.class);
        // TODO: Use the ViewModel
    }

    public List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
