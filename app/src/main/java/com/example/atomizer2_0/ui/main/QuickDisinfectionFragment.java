package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.CircularProgressView;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import static com.example.atomizer2_0.MainActivity.historyData;
import static com.example.atomizer2_0.MainActivity.nowRoomData;
import static com.example.atomizer2_0.MainActivity.sharedPreferenceUtil;

public class QuickDisinfectionFragment extends Fragment implements View.OnClickListener{
    private QuickDisinfectionViewModel mQuickDisinfectionViewModel;
    protected static ImageButton startImageButton;
    protected ImageButton stopImageButton;
    protected SeekBar seekBarRoomTime;
    protected EditText editTextTime, editTextRoomName;
    private static TextView countTimeText;
    private TextView tempretureTextView;
    private TextView humidityTextView;
    private static TextView dateTextView;
    private SeekBar seekBarLevel;
    private boolean editListen=false;
    private static CircularProgressView circularProgressView;
    public static QuickDisinfectionFragment newInstance() {
        return new QuickDisinfectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.quick_disinfection, container, false);
        MainActivity.nowFragmentId=R.id.quick_disinfection_fragment;
        MainActivity.lastFragmentId=R.id.quick_disinfection_fragment;
        MainActivity.barTitle.setText("快速消毒");
        startImageButton=root.findViewById(R.id.startImageButton);
        stopImageButton=root.findViewById(R.id.stopImageButton);
        seekBarRoomTime=root.findViewById(R.id.seekBarRoomTime);
        editTextTime=root.findViewById(R.id.editTextTime);
        editTextRoomName =root.findViewById(R.id.roomName);
        countTimeText=root.findViewById(R.id.countTimeText);
        circularProgressView=root.findViewById(R.id.circularProgressView);
        tempretureTextView=root.findViewById(R.id.tempretureTextView);
        humidityTextView=root.findViewById(R.id.humidityTextView);
        seekBarLevel=root.findViewById(R.id.seekBarLevel);
        dateTextView=root.findViewById(R.id.dateTextView);
        startImageButton.setOnClickListener(this);
        stopImageButton.setOnClickListener(this);
        editListen=false;
        editTextTime.setText(0+"");
        editTextRoomName.setText("快速任务");
        editListen=true;
        seekListen();
        editTextListen();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mQuickDisinfectionViewModel = ViewModelProviders.of(this).get(QuickDisinfectionViewModel.class);
        // TODO: Use the ViewModel
    }
    private void seekListen(){
        seekBarRoomTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                nowRoomData.setRoomProcess(seekBar.getProgress());
                int time= (int) ((seekBar.getProgress()/100.0)*60);
                editTextTime.setText(time+"");
                nowRoomData.setRoomTime(time);
                editListen=false;
            }
        });
    }
    private void editTextListen(){
        editTextTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editListen){
                    if (!editTextTime.getText().toString().equals("")){
                        int time=Integer.parseInt(editTextTime.getText().toString());
                        nowRoomData.setRoomTime(time);
                        int process= (int) (((double)time/60.0)*100);
                        Log.e("tag","process:"+process);
                        nowRoomData.setRoomProcess(process);
                        seekBarRoomTime.setProgress(process);
                    }
                }
            }
        });
        editTextTime.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editListen=true;
                } else {
                    editListen=false;
                }
            }
        });
        editTextRoomName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nowRoomData.setRoomName(editTextRoomName.getText().toString());
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startImageButton:
                nowRoomData.setMode("快速消毒");
                nowRoomData.setRoomTime(Integer.parseInt(editTextTime.getText().toString()));
                if (!MainActivity.state&&nowRoomData.getRoomTime()>0){
//                    if (nowRoomData.getRoomName().equals("未选择任务")){
//                        Toast.makeText(getContext(), "请先选择任务", Toast.LENGTH_LONG).show();
//                    }else{
                        //byte[] sendBuf={0x25};
                        //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                        nowRoomData.setTaskData(dateTextView.getText().toString());
                        nowRoomData.setPrincipal("null");
                        if(historyData.size()<100){
                            historyData.add(nowRoomData);
                        }else {
                            historyData.remove(0);
                            historyData.add(nowRoomData);
                        }
                        sharedPreferenceUtil.writeObject(getContext(),"HistoryList",historyData);
                        long currentTime = System.currentTimeMillis();
                        MainActivity.Countdown=currentTime+nowRoomData.getRoomTime()*60*1000;
                        startImageButton.setEnabled(false);
                        MainActivity.state=true;
                        Toast.makeText(getContext(), "开启任务", Toast.LENGTH_LONG).show();
//                    }

                }else{
                    Toast.makeText(getContext(), "开启已任务，请勿重复点击", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.stopImageButton:
                //byte[] sendBuf={0x29};
                //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                countTimeText.setText("0 min");
                circularProgressView.setProgress(0);

                MainActivity.Countdown=System.currentTimeMillis();
                startImageButton.setEnabled(true);
                MainActivity.state=false;
                Toast.makeText(getContext(), "关闭任务", Toast.LENGTH_LONG).show();
                break;

        }
    }

    public static Handler quickHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int progress= (int) (100-(double)msg.obj*100);
                circularProgressView.setProgress((int) progress);
            }
            else if(msg.what == 3){
                countTimeText.setText(((int)msg.obj)+" min");
            }
            else if(msg.what == 4){
                countTimeText.setText("0 min");
                circularProgressView.setProgress(100);
                //byte[] sendBuf={0x29};
                //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                startImageButton.setEnabled(true);
            }else if (msg.what == 5){
                dateTextView.setText((String)msg.obj);
            }

        }
    };
}
