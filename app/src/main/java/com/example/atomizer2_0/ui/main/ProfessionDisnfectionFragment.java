package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.atomizer2_0.CircularProgressView;
import com.example.atomizer2_0.DashboardView;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.atomizer2_0.MainActivity.barDate;
import static com.example.atomizer2_0.MainActivity.historyData;
import static com.example.atomizer2_0.MainActivity.nowRoomData;
import static com.example.atomizer2_0.MainActivity.sharedPreferenceUtil;

public class ProfessionDisnfectionFragment extends Fragment implements View.OnClickListener{
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
    protected static Button startButton;
    private LinearLayout homeButton;
    protected static DashboardView tempDashboardView,humDashboardView;
    private static CircularProgressView circularProgressView;
    public static ProfessionDisnfectionFragment newInstance() {
        return new ProfessionDisnfectionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profession_disinfection_2, container, false);
        startButton=root.findViewById(R.id.startButton);
        MainActivity.nowFragmentId=R.id.profession_disinfection_fragment;
        MainActivity.lastFragmentId=R.id.profession_disinfection_fragment;
        MainActivity.mode=2;
        buttonParameter=root.findViewById(R.id.buttonParameter);
        principalEditText=root.findViewById(R.id.principalEditText);
        countTimeText=root.findViewById(R.id.countTimeText);
        tempretureTextView=root.findViewById(R.id.tempretureTextView);
        humidityTextView=root.findViewById(R.id.humidityTextView);
        seekBarLevel=root.findViewById(R.id.seekBarLevel);
        dateTextView=root.findViewById(R.id.dateTextView);
        circularProgressView=root.findViewById(R.id.circularProgressView);
        tempDashboardView=root.findViewById(R.id.dashboard_view_temp);
        humDashboardView=root.findViewById(R.id.dashboard_view_hum);
        homeButton=root.findViewById(R.id.homeButton);
        homeButton.setOnClickListener(this);
        buttonParameter.setOnClickListener(this);
        startButton.setOnClickListener(this);
        editTextListen();
        roomName=root.findViewById(R.id.roomName);
        roomArea=root.findViewById(R.id.roomArea);
        roomTime=root.findViewById(R.id.roomTime);
        if (nowRoomData.getMode().equals("专业消毒")){
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
        //mBroadDisnfectionViewModel = ViewModelProviders.of(this).get(BroadDisnfectionViewModel.class);
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
                        .replace(R.id.container, TemplateFragment2.newInstance())
                        .commitNow();
                //Log.e("tag","buttonParameter");
                break;
            case R.id.startButton:
                if (!principalEditText.getText().toString().equals("")){
                    if (!MainActivity.state&&nowRoomData.getRoomTime()>0){
                    if (nowRoomData.getRoomName().equals("未选择任务")){
                        Toast.makeText(getContext(), "请先选择任务", Toast.LENGTH_LONG).show();
                    }else{
                        startButton.setEnabled(false);
                        TimerTask task = new TimerTask(){
                            public void run(){
                                Message msg1 = new Message();
                                msg1.what = 8;
                                professionHandler.sendMessage(msg1);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task,5000);
                        //byte[] sendBuf={0x25};
                        //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                        nowRoomData.setTaskData(barDate.getText().toString());
                        if(historyData.size()<100){
                            historyData.add(nowRoomData);
                        }else {
                            historyData.remove(0);
                            historyData.add(nowRoomData);
                        }
                        sharedPreferenceUtil.writeObject(getContext(),"HistoryList",historyData);
                        long currentTime = System.currentTimeMillis();
                        MainActivity.Countdown=currentTime+nowRoomData.getRoomTime()*60*1000;
                        MainActivity.state=true;
                        startButton.setText("停止");
                        Toast.makeText(getContext(), "开启任务", Toast.LENGTH_LONG).show();
                    }

                    }else if(MainActivity.state){
                        startButton.setEnabled(false);
                        TimerTask task = new TimerTask(){
                            public void run(){
                                Message msg1 = new Message();
                                msg1.what = 8;
                                professionHandler.sendMessage(msg1);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task,5000);
                        //byte[] sendBuf={0x29};
                        //MainActivity.serialPortThread.sendSerialPort(sendBuf);
                        countTimeText.setText("00:00");
                        circularProgressView.setProgress(0);
                        MainActivity.Countdown=System.currentTimeMillis();
                        startButton.setText("开始");
                        //startButton.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.button_corner));
                        MainActivity.state=false;
                        Toast.makeText(getContext(), "关闭任务", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "请输入负责人", Toast.LENGTH_LONG).show();
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
    public static Handler professionHandler  = new Handler(){
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
                    tempDashboardView.setRealTimeValue(tem1);
                    humDashboardView.setRealTimeValue(hum1);
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
            }else if (msg.what == 5){
                //dateTextView.setText((String)msg.obj);
            }else if(msg.what == 6){
                tempDashboardView.setRealTimeValue((float)msg.obj);
            }else if(msg.what == 7){
                humDashboardView.setRealTimeValue((float)msg.obj);
            }else if (msg.what == 8){
                startButton.setEnabled(true);
            }

        }
    };

}
