package com.example.atomizer2_0.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.atomizer2_0.R;

import java.util.List;

public class MoreHistoryAdapter2 extends ArrayAdapter<RoomData> implements Filterable {
    private List<RoomData> mData;
    private Context mContext;
    private TextView modeTextView,nameTextView,principalTextView,timeTextView,dateTextView;
    private TextView viewTextView;
    private int resourceId;

    public MoreHistoryAdapter2(Context context, int resource, List<RoomData> objects){
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
        //CheckBox checkbox=view.findViewById(R.id.checkbox);
        modeTextView.setText(roomData.getMode()+"");
        nameTextView.setText(roomData.getRoomName()+"");
        principalTextView.setText(roomData.getPrincipal()+"");
        timeTextView.setText(roomData.getRoomTime()+"");
        dateTextView.setText(roomData.getTaskData()+"");


//        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
//                if(isChecked){
//                    MainActivity.checkList.set(position,isChecked);
//                }else{
//                    MainActivity.checkList.set(position,isChecked);
//                }
//                for (int i = 0; i< MainActivity.checkList.size(); i++){
//                    if (MainActivity.checkList.get(i)){
//                        MainActivity.checkList.set(i,true);
//                    }else {
//                        MainActivity.checkList.set(i,false);
//                    }
//                }
//            }
//        });
        return view;
    }

}



