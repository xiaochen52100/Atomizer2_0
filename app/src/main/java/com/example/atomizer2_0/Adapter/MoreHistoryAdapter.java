package com.example.atomizer2_0.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.atomizer2_0.R;
import com.example.atomizer2_0.ui.main.MoreHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class MoreHistoryAdapter extends ArrayAdapter<RoomData> {
    private List<RoomData> mData;
    private Context mContext;
    private TextView modeTextView,nameTextView,principalTextView,timeTextView,dateTextView;
    private int resourceId;
    private CheckBox checkbox;

    public MoreHistoryAdapter(Context context, int resource, List<RoomData> objects){
        super(context, resource,objects);
        this.mData=objects;
        this.mContext=context;
        this.resourceId=resource;

    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RoomData roomData=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        modeTextView=view.findViewById(R.id.modeTextView);
        nameTextView=view.findViewById(R.id.nameTextView);
        principalTextView=view.findViewById(R.id.principalTextView);
        timeTextView=view.findViewById(R.id.timeTextView);
        dateTextView=view.findViewById(R.id.dateTextView);
        checkbox=view.findViewById(R.id.checkbox);
        modeTextView.setText(roomData.getMode()+"");
        nameTextView.setText(roomData.getRoomName()+"");
        principalTextView.setText(roomData.getPrincipal()+"");
        timeTextView.setText(roomData.getRoomTime()+"");
        dateTextView.setText(roomData.getTaskData()+"");

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(isChecked){
                    MoreHistoryFragment.checkList.set(position,isChecked);
                }else{
                    MoreHistoryFragment.checkList.set(position,isChecked);
                }
                for (int i=0;i<MoreHistoryFragment.checkList.size();i++){
                    Log.e("tag","checkList("+i+"):"+MoreHistoryFragment.checkList.get(i));
                }
            }
        });
        return view;
    }

}



