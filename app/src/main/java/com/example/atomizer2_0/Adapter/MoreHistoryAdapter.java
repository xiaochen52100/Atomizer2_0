package com.example.atomizer2_0.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.atomizer2_0.R;
import com.example.atomizer2_0.ui.main.GenericFragment2;

import java.util.List;

public class MoreHistoryAdapter extends ArrayAdapter<RoomData> implements Filterable {
    private List<RoomData> mData;
    private Context mContext;
    private TextView modeTextView,nameTextView,principalTextView,timeTextView,dateTextView;
    private TextView viewTextView;
    private int resourceId;
    private Handler mhandler;

    public MoreHistoryAdapter(Context context, int resource, List<RoomData> objects, Handler handler){
        super(context, resource,objects);
        this.mData=objects;
        this.mContext=context;
        this.resourceId=resource;
        this.mhandler=handler;

    }
    @Override
    public int getCount() {
        return mData.size();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final RoomData roomData=getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        modeTextView=view.findViewById(R.id.modeTextView);
        nameTextView=view.findViewById(R.id.nameTextView);
        principalTextView=view.findViewById(R.id.principalTextView);
        timeTextView=view.findViewById(R.id.timeTextView);
        dateTextView=view.findViewById(R.id.dateTextView);
        viewTextView=view.findViewById(R.id.viewTextView);
        //CheckBox checkbox=view.findViewById(R.id.checkbox);
        modeTextView.setText(roomData.getMode()+"");
        nameTextView.setText(roomData.getRoomName()+"");
        principalTextView.setText(roomData.getPrincipal()+"");
        timeTextView.setText(roomData.getRoomTime()+"");
        dateTextView.setText(roomData.getTaskData()+"");

        viewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("tag",position+"");
                Message msg = new Message();
                msg.what=1;
                msg.obj=roomData;
                mhandler.sendMessage(msg);

            }
        });

        return view;
    }

}



