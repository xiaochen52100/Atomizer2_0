package com.example.atomizer2_0.Adapter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomData implements Serializable {
    private String roomName=null;
    private int roomArea=0;
    private int roomTime=0;
    private int roomProcess=0;
    private String mode=null;//0：快速,1：广谱,2:专业
    private String taskData=null;
    private String principal=null;
    public RoomData(String mode,String principal,String roomName,int roomArea,int roomTime,int roomProcess){
        this.mode=mode;
        this.principal=principal;
        this.roomName=roomName;
        this.roomArea=roomArea;
        this.roomTime=roomTime;
        this.roomProcess=roomProcess;
        getDate();
    }
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }
    public void setRoomArea(int roomArea){
        this.roomArea=roomArea;
    }
    public void setRoomTime(int roomTime){
        this.roomTime=roomTime;
    }
    public void setRoomProcess(int roomProcess){
        this.roomProcess=roomProcess;
    }
    public String getRoomName(){
        return roomName;
    }
    public int getRoomArea(){
        return roomArea;
    }
    public int getRoomTime(){
        return roomTime;
    }
    public int getRoomProcess(){
        return roomProcess;
    }
    private void getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        taskData=simpleDateFormat.format(date);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getMode(){
        return this.mode;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    public String getPrincipal(){
        return this.principal;
    }
    public void setTaskData(String taskData){
        this.taskData=taskData;
    }
    public String getTaskData(){
        return this.taskData;
    }
}
