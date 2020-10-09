package com.example.atomizer2_0.ui.main;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Printer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import static com.example.atomizer2_0.ui.main.ProfessionDisnfectionFragment.professionHandler;
import static com.example.atomizer2_0.ui.main.QuickDisinfectionFragment.quickHandler;

public class BroadDisnfectionFragment extends Fragment implements View.OnClickListener{
    private BroadDisnfectionViewModel mBroadDisnfectionViewModel;
    private Button buttonParameter;
    private EditText principalEditText;
    private static TextView countTimeText;
    private TextView tempretureTextView;
    private TextView humidityTextView;
    private static TextView dateTextView;
    private SeekBar seekBarLevel;
    private TextView roomName;
    private EditText roomArea,roomTime;
    protected static ImageButton startImageButton;
    protected ImageButton stopImageButton;
    private static CircularProgressView circularProgressView;
    public static BroadDisnfectionFragment newInstance() {
        return new BroadDisnfectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.broad_disinfection, container, false);
        MainActivity.barTitle.setText("广谱消毒");
        MainActivity.nowFragmentId=R.id.broad_disinfection_fragment;
        MainActivity.lastFragmentId=R.id.broad_disinfection_fragment;
        MainActivity.mode=1;
        buttonParameter=root.findViewById(R.id.buttonParameter);
        principalEditText=root.findViewById(R.id.principalEditText);
        countTimeText=root.findViewById(R.id.countTimeText);
        tempretureTextView=root.findViewById(R.id.tempretureTextView);
        humidityTextView=root.findViewById(R.id.humidityTextView);
        seekBarLevel=root.findViewById(R.id.seekBarLevel);
        dateTextView=root.findViewById(R.id.dateTextView);
        circularProgressView=root.findViewById(R.id.circularProgressView);
        buttonParameter.setOnClickListener(this);
        startImageButton=root.findViewById(R.id.startImageButton);
        stopImageButton=root.findViewById(R.id.stopImageButton);
        startImageButton.setOnClickListener(this);
        stopImageButton.setOnClickListener(this);
        editTextListen();
        roomName=root.findViewById(R.id.roomName);
        roomArea=root.findViewById(R.id.roomArea);
        roomTime=root.findViewById(R.id.roomTime);
        if (nowRoomData.getMode().equals("广谱消毒")){
            roomName.setText(nowRoomData.getRoomName()+"");
            roomArea.setText(nowRoomData.getRoomArea()+"");
            roomTime.setText(nowRoomData.getRoomTime()+"");

        }else{
            roomName.setText("未选择任务");
            roomArea.setText(0+"");
            roomTime.setText(0+"");
        }

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBroadDisnfectionViewModel = ViewModelProviders.of(this).get(BroadDisnfectionViewModel.class);
        // TODO: Use the ViewModel
    }
    private void editTextListen(){
        principalEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nowRoomData.setPrincipal(principalEditText.getText().toString());
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonParameter:
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, TemplateFragment.newInstance())
                        .commitNow();
                Log.e("tag","buttonParameter");
                break;
            case R.id.startImageButton:
                if (!principalEditText.getText().toString().equals("")){
                    if (!MainActivity.state&&nowRoomData.getRoomTime()>0){
                    if (nowRoomData.getRoomName().equals("未选择任务")){
                        Toast.makeText(getContext(), "请先选择任务", Toast.LENGTH_LONG).show();
                    }else{
                        //byte[] sendBuf={0x25};
                        //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                        nowRoomData.setTaskData(dateTextView.getText().toString());
                        if(historyData.size()<3){
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
                    }

                    }else{
                        Toast.makeText(getContext(), "开启已任务，请勿重复点击", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "请输入负责人", Toast.LENGTH_LONG).show();
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
    public static Handler broadHandler  = new Handler(){
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
