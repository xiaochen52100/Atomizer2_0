package com.example.atomizer2_0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.atomizer2_0.R;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<RoomData> {
    private List<RoomData> mData;
    private Context mContext;
    private Button EditRoom;
    private TextView roomArea;
    private TextView roomName;
    private TextView modeTextView;
    private SeekBar seekBarRoomTime;
    private TextView textRoomTime;
    private TextView principalTextView;
    private int resourceId;
    public HistoryAdapter(Context context, int resource, List<RoomData> objects){
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
        roomArea=(TextView)view.findViewById(R.id.roomArea);
        roomName=(TextView) view.findViewById(R.id.roomName);
        textRoomTime=(TextView)view.findViewById(R.id.textRoomTime);
        modeTextView=(TextView)view.findViewById(R.id.modeTextView);
        principalTextView=(TextView)view.findViewById(R.id.principalTextView);
        //Log.e("tag",roomData.getRoomName()+","+roomData.getRoomArea()+","+roomData.getRoomTime());
        roomName.setText(roomData.getRoomName());
        roomArea.setText(roomData.getRoomArea()+"");
        textRoomTime.setText(roomData.getRoomTime()+"");
        principalTextView.setText(roomData.getPrincipal());
        if (roomData.getMode().equals("广谱消毒")){
            modeTextView.setText("模式名称：广谱模式");
        }else if(roomData.getMode().equals("专业消毒")){
            modeTextView.setText("模式名称：专业消毒");
        }else if(roomData.getMode().equals("快速消毒")){
            modeTextView.setText("模式名称：快速消毒");
        }
        return view;
    }

}



