package com.example.atomizer2_0;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.Adapter.SharedPreferenceUtil;
import com.example.atomizer2_0.ui.main.BroadDisnfectionFragment;
import com.example.atomizer2_0.ui.main.MainFragment;
import com.example.atomizer2_0.ui.main.MainSettingFragment;
import com.example.atomizer2_0.ui.main.MoreHistoryFragment;
import com.example.atomizer2_0.ui.main.ProfessionDisnfectionFragment;
import com.example.atomizer2_0.ui.main.QuickDisinfectionFragment;
import com.example.atomizer2_0.ui.main.TemplateFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.atomizer2_0.ui.main.BroadDisnfectionFragment.broadHandler;
import static com.example.atomizer2_0.ui.main.ProfessionDisnfectionFragment.professionHandler;
import static com.example.atomizer2_0.ui.main.QuickDisinfectionFragment.quickHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static List<RoomData> templateRoomData = new ArrayList<RoomData>();
    public static List<RoomData> historyData = new ArrayList<RoomData>();
    public static RoomData nowRoomData=new RoomData("快速消毒","null","请编辑任务",0,0,0);
    public static SharedPreferenceUtil sharedPreferenceUtil=new SharedPreferenceUtil();//对象存储
    public static int nowFragmentId;
    public static int lastFragmentId;
    public static ImageButton barButton;
    public static TextView barTitle;
    private LinearLayout barLayout;
    public static boolean runFlag=false;
    public static long Countdown=0;
    public static boolean state=false;
    public static Timer timerTask ,dateTaskTimer;
    public static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        barButton=findViewById(R.id.barButton);
        barLayout=findViewById(R.id.barLayout);
        barTitle=findViewById(R.id.barTitle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
            nowFragmentId=R.id.main;
        }
        barButton.setVisibility(View.INVISIBLE);
        barButton.setOnClickListener(this);
//        templateRoomData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        templateRoomData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        historyData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        historyData.add(new RoomData("快速消毒","李","房间1",20,20,20));
        if(sharedPreferenceUtil.readObject(this,"templateRoomData")==null){
            templateRoomData.add(new RoomData("广谱消毒","null","房间1",20,7,50));
            sharedPreferenceUtil.writeObject(this,"templateRoomData",templateRoomData);
            Log.e("tag","read null");
        }else{
            templateRoomData=(List<RoomData>)sharedPreferenceUtil.readObject(this,"templateRoomData");
        }
        if(sharedPreferenceUtil.readObject(this,"HistoryList")==null){
            historyData.add(new RoomData("快速消毒","null","房间1",20,7,50));
            sharedPreferenceUtil.writeObject(this,"HistoryList",historyData);
            Log.e("tag","read null");
        }else{
            historyData=(List<RoomData>)sharedPreferenceUtil.readObject(this,"HistoryList");
        }
        if (timerTask==null){
            timerTask = new Timer(true);
            timerTask.schedule(task, 500, 1000);
        }
        if (dateTaskTimer==null){
            dateTaskTimer = new Timer(true);
            dateTaskTimer.schedule(dateTask, 500, 1000);
        }
    }
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {

            Window _window = getWindow();
            WindowManager.LayoutParams params = _window.getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_IMMERSIVE;
            _window.setAttributes(params);
        }
    }
    public static TimerTask dateTask = new TimerTask() {
        public void run() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
            //获取当前时间
            Date date = new Date(System.currentTimeMillis());
            String dateString=simpleDateFormat.format(date);
            Message msg = new Message();
            msg.what = 5;
            msg.obj=dateString;
            switch (nowFragmentId){
                case R.id.broad_disinfection_fragment:
                    broadHandler.sendMessage(msg);
                    break;
                case R.id.profession_disinfection_fragment:
                    professionHandler.sendMessage(msg);
                    break;
                case R.id.quick_disinfection_fragment:
                    quickHandler.sendMessage(msg);
                    break;

            }


        }
    };
    public static TimerTask task = new TimerTask() {
        public void run() {
            long currentTime = System.currentTimeMillis();
            if (state){
                Log.e("tag","mode:"+nowRoomData.getMode());
                if (currentTime>=Countdown){
                    Message msg1 = new Message();
                    msg1.what = 4;
                    if (nowRoomData.getMode().equals("快速消毒")){
                        quickHandler.sendMessage(msg1);

                    }else if(nowRoomData.getMode().equals("广谱消毒")){
                        broadHandler.sendMessage(msg1);

                    }else if(nowRoomData.getMode().equals("专业消毒")){
                        professionHandler.sendMessage(msg1);
                        Log.e("tag","专业消毒");
                    }

                    state=false;
                }
                else{
                    double progess=((double) (Countdown-currentTime))/(double)(nowRoomData.getRoomTime()*60*1000);
                    //Log.v("tag","progess:"+progess+"  "+(Countdown-currentTime)+"  "+(nowRoomData.getRoomTime()*60*1000));
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj=progess;
                    if (nowRoomData.getMode().equals("快速消毒")){
                        quickHandler.sendMessage(msg);

                    }else if(nowRoomData.getMode().equals("广谱消毒")){
                        broadHandler.sendMessage(msg);

                    }else if(nowRoomData.getMode().equals("专业消毒")){
                        professionHandler.sendMessage(msg);
                    }
                    int time= (int) ((Countdown-currentTime)/1000/60);
                    Message msg2 = new Message();
                    if (time<=0){
                        msg2.what = 3;
                        msg2.obj=time;
                    }else{
                        msg2.what = 3;
                        msg2.obj=time+1;
                    }
                    if (nowRoomData.getMode().equals("快速消毒")){
                        quickHandler.sendMessage(msg2);
                    }else if(nowRoomData.getMode().equals("广谱消毒")){
                        broadHandler.sendMessage(msg2);
                    }else if(nowRoomData.getMode().equals("专业消毒")){
                        professionHandler.sendMessage(msg2);
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.barButton:
                switch (nowFragmentId){
                    case R.id.broad_disinfection_fragment:
                    case R.id.profession_disinfection_fragment:
                        if (!state){
                            barButton.setVisibility(View.INVISIBLE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainFragment.newInstance())
                                    .commitNow();
                        }else{
                            Toast.makeText(this, "任务进行中不可退出", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.template_fragment:
                        if (lastFragmentId==R.id.broad_disinfection_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, BroadDisnfectionFragment.newInstance())
                                    .commitNow();
                        }else if(lastFragmentId==R.id.profession_disinfection_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, ProfessionDisnfectionFragment.newInstance())
                                    .commitNow();
                        }else if(lastFragmentId==R.id.more_setting_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainSettingFragment.newInstance())
                                    .commitNow();
                        }
                        break;
                    case R.id.quick_disinfection_fragment:
                        if (!state){
                            barButton.setVisibility(View.INVISIBLE);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainFragment.newInstance())
                                    .commitNow();
                        }else{
                            Toast.makeText(this, "任务进行中不可退出", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.more_setting_fragment:
                        barButton.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MainFragment.newInstance())
                                .commitNow();
                        break;
                    case R.id.printf_history_fragment:
                        barButton.setVisibility(View.INVISIBLE);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, MoreHistoryFragment.newInstance())
                                .commitNow();
                        break;

                    case R.id.more_history_fragment:
                        if (lastFragmentId==R.id.broad_disinfection_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, TemplateFragment.newInstance())
                                    .commitNow();
                        }else if(lastFragmentId==R.id.profession_disinfection_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, TemplateFragment.newInstance())
                                    .commitNow();
                        }else if(lastFragmentId==R.id.more_setting_fragment){
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, MainSettingFragment.newInstance())
                                    .commitNow();
                        }

                        break;
                }
                break;
        }
    }

    public static Handler mainActivityHandler  = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
            }

        }
    };
}