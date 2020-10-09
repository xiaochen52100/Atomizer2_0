package com.example.atomizer2_0.Adapter;

import android.content.Context;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;
import com.example.atomizer2_0.ui.main.BroadDisnfectionFragment;

import java.util.List;

import static com.example.atomizer2_0.MainActivity.*;
import static com.example.atomizer2_0.ui.main.QuickDisinfectionFragment.quickHandler;


public class RoomAdapter extends ArrayAdapter<RoomData> {
    private List<RoomData> mData;
    private Context mContext;
    private int resourceId;
    private Handler mHandler;
    public RoomAdapter(Context context, int resource, List<RoomData> objects, Handler mHandler){
        super(context, resource,objects);
        this.mData=objects;
        this.mContext=context;
        this.resourceId=resource;
        this.mHandler=mHandler;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RoomData roomData=getItem(position);
        final boolean[] editListen = {false};
        final boolean[] editAreaListen = {false};
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        final EditText roomArea=(EditText)view.findViewById(R.id.roomArea);
        final EditText roomName=(EditText) view.findViewById(R.id.roomName);
        final SeekBar seekBarRoomTime=(SeekBar)view.findViewById(R.id.seekBarRoomTime);
        final Button confirmBtn=view.findViewById(R.id.confirmBtn);
        final EditText editRoomTime=(EditText)view.findViewById(R.id.editRoomTime);
        final Spinner modeSpinner=(Spinner)view.findViewById(R.id.modeSpinner);
        roomName.setText(roomData.getRoomName());
        roomArea.setText(roomData.getRoomArea()+"");
        seekBarRoomTime.setProgress(roomData.getRoomProcess());
        editRoomTime.setText(roomData.getRoomTime()+"");
        editRoomTime.setEnabled(false);
        Log.e("tag","getMode:"+roomData.getMode());
        if (roomData.getMode().equals("广谱消毒")){
            modeSpinner.setSelection(0);
        }else if(roomData.getMode().equals("专业消毒")){
            modeSpinner.setSelection(1);
        }
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id){
                TextView tv = (TextView)view;
                tv.setTextSize(23.0f);    //设置大小

                if (i==0){
                    templateRoomData.get(position).setMode("广谱消毒");
                    MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);
                }else{
                    templateRoomData.get(position).setMode("专业消毒");
                    MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });

        editRoomTime.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editListen[0] =true;
                } else {
                    editListen[0] =false;
                }
            }
        });
        roomArea.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editAreaListen[0] =true;
                } else {
                    editAreaListen[0] =false;
                }
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
                templateRoomData.get(position).setRoomName(roomName.getText().toString());
                MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);
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
                if (editAreaListen[0]){
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
                        templateRoomData.get(position).setRoomTime(roomTimes);
                        templateRoomData.get(position).setRoomArea(roomAreas);
                        templateRoomData.get(position).setRoomProcess(50);
                        MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);
                    }
                }

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.nowRoomData=templateRoomData.get(position);
                Toast.makeText(getContext(), "任务选择成功", Toast.LENGTH_LONG).show();
                Message msg = new Message();
                if(templateRoomData.get(position).getMode().equals("广谱消毒")){
                    msg.what = 1;
                }else if(templateRoomData.get(position).getMode().equals("专业消毒")){
                    msg.what = 2;
                }
                mHandler.sendMessage(msg);
//                if(historyData.size()<3){
//                    historyData.add(new RoomData(roomData.getRoomName(),roomData.getRoomArea(),roomData.getRoomTime()));
//                }else {
//                    historyData.remove(0);
//                    historyData.add(new RoomData(roomData.getRoomName(),roomData.getRoomArea(),roomData.getRoomTime()));
//                }

            }
        });
        seekBarRoomTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

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
                templateRoomData.get(position).setRoomTime(time);
                templateRoomData.get(position).setRoomProcess(seekBar.getProgress());
                MainActivity.sharedPreferenceUtil.writeObject(getContext(),"templateRoomData",templateRoomData);

            }
        });
        return view;
    }

}



