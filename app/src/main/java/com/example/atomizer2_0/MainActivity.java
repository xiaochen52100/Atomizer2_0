package com.example.atomizer2_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atomizer2_0.Adapter.RoomData;
import com.example.atomizer2_0.Adapter.SharedPreferenceUtil;
import com.example.atomizer2_0.serial.SerialPortThread;
import com.example.atomizer2_0.ui.main.BroadDisnfectionFragment;
import com.example.atomizer2_0.ui.main.MainFragment;
import com.example.atomizer2_0.ui.main.MainSettingFragment;
import com.example.atomizer2_0.ui.main.ProfessionDisnfectionFragment;
import com.example.atomizer2_0.ui.main.TemplateFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.atomizer2_0.ui.main.BroadDisnfectionFragment.broadHandler;
import static com.example.atomizer2_0.ui.main.ProfessionDisnfectionFragment.professionHandler;
import static com.example.atomizer2_0.ui.main.QuickDisinfectionFragment.quickHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static List<RoomData> templateRoomData = new ArrayList<RoomData>();
    public static List<RoomData> historyData = new ArrayList<RoomData>();
    public static RoomData nowRoomData=new RoomData("快速消毒"," ","请编辑任务",0,0,0);
    public static SharedPreferenceUtil sharedPreferenceUtil=new SharedPreferenceUtil();//对象存储
    public static int nowFragmentId;
    public static int lastFragmentId;
    public static ImageView barButton;
    public static TextView barTitle;
    private LinearLayout barLayout;
    public static boolean runFlag=false;
    public static long Countdown=0;
    public static boolean state=false;
    public static Timer timerTask ,dateTaskTimer;
    public static int mode;
    public static TextView barDate;
    public static TextView modeText;
    public static List<Boolean> checkList=new ArrayList<Boolean>();
    public static SerialPortThread serialPortThread;
    public static TextToSpeech textToSpeech = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity);

        barButton=findViewById(R.id.barButton);
//        barButton.setVisibility(View.INVISIBLE);
        barLayout=findViewById(R.id.barLayout);
        barDate=findViewById(R.id.barDate);
        modeText=findViewById(R.id.modeText);
        modeText.setVisibility(View.GONE);
//        barTitle=findViewById(R.id.barTitle);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
            nowFragmentId=R.id.main;
        }
//        barButton.setVisibility(View.INVISIBLE);
//        barButton.setOnClickListener(this);
//        templateRoomData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        templateRoomData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        historyData.add(new RoomData("快速消毒","李","房间1",20,20,20));
//        historyData.add(new RoomData("快速消毒","李","房间1",20,20,20));
        if(sharedPreferenceUtil.readObject(this,"templateRoomData")==null){
            templateRoomData.add(new RoomData("广谱消毒"," ","房间1",20,7,50));
            sharedPreferenceUtil.writeObject(this,"templateRoomData",templateRoomData);
            Log.e("tag","read null");
        }else{
            templateRoomData=(List<RoomData>)sharedPreferenceUtil.readObject(this,"templateRoomData");
        }
        if(sharedPreferenceUtil.readObject(this,"HistoryList")==null){
            historyData.add(new RoomData("快速消毒"," ","房间1",20,7,50));
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
        serialPortThread=new SerialPortThread();
        serialPortThread.openSerialPort();
//        initTTS();
//        //textToSpeech.setLanguage(Locale.CHINESE);
//        //设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//        textToSpeech.setPitch(4f);
//        //设置语速
//        textToSpeech.setSpeechRate(3f);
//        //输入中文，若不支持的设备则不会读出来
//        textToSpeech.speak("测试",
//                TextToSpeech.QUEUE_FLUSH, null);

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if(hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
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
            msg.what = 1;
            msg.obj=dateString;
            mainActivityHandler.sendMessage(msg);

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
                    }
                    byte[] sendBuf={0x29};
                    serialPortThread.sendSerialPort(sendBuf);
                    state=false;
                }else{
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
                    int time= (int) ((Countdown-currentTime)/1000);
                    Message msg2 = new Message();
                    if (time<=0){
                        msg2.what = 3;
                        msg2.obj=time;
                    }else{
                        msg2.what = 3;
                        msg2.obj=time;
                    }
                    if (nowRoomData.getMode().equals("快速消毒")){
                        quickHandler.sendMessage(msg2);
                    }else if(nowRoomData.getMode().equals("广谱消毒")){
                        broadHandler.sendMessage(msg2);
                    }else if(nowRoomData.getMode().equals("专业消毒")){
                        professionHandler.sendMessage(msg2);
                    }
                    byte[] sendBuf={0x25};
                    serialPortThread.sendSerialPort(sendBuf);
                }

            }
            else{
                byte[] sendBuf={0x29};
                serialPortThread.sendSerialPort(sendBuf);
            }
        }
    };
    private void initTTS() {

        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == 0) {

                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    //int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    //boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    //Log.i("zhh_tts", "US支持否？--》" + a +
                    //        "\nzh-CN支持否》--》" + b);

                } else {
                    Toast.makeText(MainActivity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

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
                barDate.setText((String)msg.obj);
            }

        }
    };
}