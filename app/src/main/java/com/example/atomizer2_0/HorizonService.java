package com.example.atomizer2_0;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import static com.example.atomizer2_0.MainActivity.nowRoomData;
import static com.example.atomizer2_0.MainActivity.testFlag;

public class HorizonService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "打印时间: " + new Date().
                        toString());
                if (testFlag){
                    Log.d("TAG", "开始设备");
                    long currentTime = System.currentTimeMillis();
                    MainActivity.Countdown=currentTime+60*500;
                    MainActivity.state=true;
                    testFlag=false;
                }else{
                    Log.d("TAG", "关闭设备");
                    MainActivity.state=false;
                    testFlag=true;
                }
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime();
        if (testFlag){
            triggerAtTime = SystemClock.elapsedRealtime() + 30000;
        }else {
            triggerAtTime = SystemClock.elapsedRealtime() + 60000*5;
        }
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}