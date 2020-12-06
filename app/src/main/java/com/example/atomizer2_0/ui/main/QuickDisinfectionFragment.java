package com.example.atomizer2_0.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.CircleProgress;
import com.example.atomizer2_0.CircularProgressView;
import com.example.atomizer2_0.DashboardView;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;
import com.example.atomizer2_0.WaveView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.atomizer2_0.MainActivity.barDate;
import static com.example.atomizer2_0.MainActivity.historyData;
import static com.example.atomizer2_0.MainActivity.nowRoomData;
import static com.example.atomizer2_0.MainActivity.sharedPreferenceUtil;
import static com.example.atomizer2_0.MainActivity.textToSpeech;

public class QuickDisinfectionFragment extends Fragment implements View.OnClickListener{
    private QuickDisinfectionViewModel mQuickDisinfectionViewModel;
    protected static Button startButton;
    protected SeekBar seekBarRoomTime;
    protected EditText editTextTime, editTextRoomName;
    private static TextView countTimeText;
    private TextView tempretureTextView;
    private TextView humidityTextView;
    private static TextView dateTextView;
    private SeekBar seekBarLevel;
    private boolean editListen=false;
    private LinearLayout homeButton;
    private WaveView waveView;
    protected static DashboardView tempDashboardView,humDashboardView,levelDashboard;
    private static CircularProgressView circularProgressView;
    private static CircleProgress mCpLoading;
    private boolean countFlag=false;
    private CountDownTimer counttimer;

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
        //MainActivity.barTitle.setText("快速消毒");
        startButton=root.findViewById(R.id.startButton);
        seekBarRoomTime=root.findViewById(R.id.seekBarRoomTime);
        editTextTime=root.findViewById(R.id.editTextTime);
        editTextRoomName =root.findViewById(R.id.roomName);
        countTimeText=root.findViewById(R.id.countTimeText);
        circularProgressView=root.findViewById(R.id.circularProgressView);
//        tempretureTextView=root.findViewById(R.id.tempretureTextView);
//        humidityTextView=root.findViewById(R.id.humidityTextView);
//        seekBarLevel=root.findViewById(R.id.seekBarLevel);
        dateTextView=root.findViewById(R.id.dateTextView);
        tempDashboardView=root.findViewById(R.id.dashboard_view_temp);
        humDashboardView=root.findViewById(R.id.dashboard_view_hum);
        tempDashboardView.setValueType(1);
        humDashboardView.setValueType(2);
//        levelDashboard=root.findViewById(R.id.dashboard_view_level);
        homeButton=root.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        mCpLoading = root.findViewById(R.id.cp_loading);
        //mCpLoading.setProgress(100,5000);
        mCpLoading.setProgress(90);
        mCpLoading.setOnCircleProgressListener(new CircleProgress.OnCircleProgressListener() {
            @Override
            public boolean OnCircleProgress(int progress) {
//                if(progress==100){
//                    mCpLoading.setProgress(0);
//                }
                return false;
            }
        });
//        waveView = (WaveView) root.findViewById(R.id.waveView);
//        waveView.setMax(100);
//        waveView.setMode(WaveView.MODE_CIRCLE);
//        waveView.setWaveColor(Color.RED);
//        waveView.setbgColor(Color.WHITE);
//        waveView.setSpeed(WaveView.SPEED_SLOW);
//        waveView.setProgress(50);
        editListen=false;
        editTextTime.setText(0+"");
        editTextRoomName.setText("快速任务");
        editListen=true;
        seekListen();
        editTextListen();
        startButton.setOnClickListener(this);

        View timeView=(root).findViewById(R.id.timeView);
        timeView.bringToFront();
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
                nowRoomData.setRoomProcess(seekBar.getProgress());
                int time= (int) ((seekBar.getProgress()/100.0)*60);
                editTextTime.setText(time+"");
                nowRoomData.setRoomTime(time);
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
            case R.id.startButton:
                nowRoomData.setMode("快速消毒");
                nowRoomData.setRoomTime(Integer.parseInt(editTextTime.getText().toString()));
                if (!MainActivity.state&&nowRoomData.getRoomTime()>0&&!countFlag){
                    nowRoomData.setTaskData(barDate.getText().toString());
                    nowRoomData.setPrincipal(" ");
                    if(historyData.size()<100){
                        historyData.add(nowRoomData);
                    }else {
                        historyData.remove(0);
                        historyData.add(nowRoomData);
                    }
                    sharedPreferenceUtil.writeObject(getContext(),"HistoryList",historyData);
                    countFlag=true;
                    counttimer = new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            startButton.setText(((millisUntilFinished-1) / 1000)+"秒后开始");
//                            Message msg1 = new Message();
//                            msg1.what =7;
//                            msg1.obj=(int)(millisUntilFinished-1) / 1000;
//                            quickHandler.sendMessage(msg1);

                        }

                        @Override
                        public void onFinish() {
                            startButton.setText("停止");

                            long currentTime = System.currentTimeMillis();
                            MainActivity.Countdown=currentTime+nowRoomData.getRoomTime()*60*1000;
                            MainActivity.state=true;
                            Toast.makeText(getContext(), "开启任务", Toast.LENGTH_LONG).show();
                            countFlag=false;
                        }
                    };
                    counttimer.start();
                }else if(MainActivity.state&&!countFlag){
                    //byte[] sendBuf={0x29};
                    //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                    countTimeText.setText("00:00");
                    circularProgressView.setProgress(0);
                    MainActivity.Countdown=System.currentTimeMillis();
                    startButton.setText("开始");
                    MainActivity.state=false;
                    Toast.makeText(getContext(), "关闭任务", Toast.LENGTH_LONG).show();
                }else if(!MainActivity.state&&nowRoomData.getRoomTime()>0&&countFlag){
                    if (counttimer!=null){
                        counttimer.cancel();
                        startButton.setText("开始");
                        MainActivity.state=false;
                        countFlag=false;
                    }
                }
                break;
            case R.id.homeButton:
                if (MainActivity.state){
                    //Toast.makeText(getContext(), "任务进行中不可退出", Toast.LENGTH_LONG).show();
                }else {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MainFragment.newInstance())
                            .commitNow();
                }
                break;

        }
    }

    public static Handler quickHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                int progress= (int) (100-(double)msg.obj*100);
                circularProgressView.setProgress((int) progress);
            }else if(msg.what == 2){
                byte[] rcvByte=(byte[])msg.obj;
                if (rcvByte[0]==(byte)0xFE){
                    float tem1= (float) ((((rcvByte[3] << 8) | rcvByte[2] & 0xff))/10.0);
                    //Log.v("tag","tem1:"+tem1);
                    float hum1=(float) ((((rcvByte[7] << 8) | rcvByte[6] & 0xff)));
                    float level=(float)rcvByte[10];
                    tempDashboardView.setRealTimeValue(tem1);
                    humDashboardView.setRealTimeValue(hum1);
                    //levelDashboard.setRealTimeValue(level);
                    mCpLoading.setProgress((int)level);
                }
            }
            else if(msg.what == 3){
                countTimeText.setText(((int)msg.obj/60)+":"+((int)msg.obj%60)+"");
            }
            else if(msg.what == 4){
                countTimeText.setText("00:00");
                circularProgressView.setProgress(0);
                //byte[] sendBuf={0x29};
                //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                startButton.setText("开始");
            }else if (msg.what == 5){
//                dateTextView.setText((String)msg.obj);
            }else if (msg.what == 6){
                startButton.setEnabled(true);
            }else if(msg.what == 7){
//                textToSpeech.setLanguage(Locale.CHINESE);
//                //设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//                textToSpeech.setPitch(4f);
//                //设置语速
//                textToSpeech.setSpeechRate(3f);
//                //输入中文，若不支持的设备则不会读出来
//                textToSpeech.speak(Integer.toString(((int)msg.obj)),
//                        TextToSpeech.QUEUE_FLUSH, null);
            }

        }
    };
}
