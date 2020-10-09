package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.Adapter.MoreHistoryAdapter;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.atomizer2_0.MainActivity.historyData;
import static com.example.atomizer2_0.ui.main.MoreHistoryFragment.checkList;

public class PrintfHistoryFragment extends Fragment {
    private EditText printfEditText;
    private Button printfButton;
    private PrintfHistoryViewModel printfHistoryViewModel;
    public static PrintfHistoryFragment newInstance() {
        return new PrintfHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.printf_history_fragment;
        MainActivity.barTitle.setText("打印预览");
        View root=inflater.inflate(R.layout.printf_history, container, false);
        printfEditText = root.findViewById(R.id.printfEditText);
        printfButton=root.findViewById(R.id.printfButton);
        String printfBuff=null;
        printfBuff="         xxxx消毒记录      \n"+"   消毒方式     任务名称     负责人     消毒时长       作业时间   \n\n";
        printfEditText.append("                           xxxx消毒记录                        \n\n");
        printfEditText.append("      消毒方式      任务名称      负责人      消毒时长      作业时间   \n\n");
        for (int i=0;i<historyData.size();i++){
            if (checkList.get(i)){
                String RoomName=historyData.get(i).getRoomName();
                if (RoomName.length()<6){
                    for (int j=0;j<6-RoomName.length();j++){
                        RoomName=RoomName+" ";
                    }
                }
                Log.e("tag",RoomName);
                String Principal=historyData.get(i).getPrincipal();
                if (Principal.length()<6){
                    for (int j=0;j<6-Principal.length();j++){
                        Principal=Principal+" ";
                    }
                }
                Log.e("tag",Principal);
                printfEditText.append("      "+historyData.get(i).getMode()+"      "+RoomName+"      "+Principal+"          "+String.format("%-3s",(historyData.get(i).getRoomTime()+""))+"                "+historyData.get(i).getTaskData()+"\n");

//                try {
//                    //printfEditText.append("      "+historyData.get(i).getMode()+"      "+new String((String.format("%-10s",new String(historyData.get(i).getRoomName().getBytes(),"ISO-8859-1"))).getBytes("ISO-8859-1"))+"      "+new String((String.format("%-10s",new String(historyData.get(i).getPrincipal().getBytes(),"ISO-8859-1"))).getBytes("ISO-8859-1"))+"          "+new String((String.format("%-3s",new String((historyData.get(i).getRoomTime()+"").getBytes(),"ISO-8859-1"))).getBytes("ISO-8859-1"))+"                "+historyData.get(i).getTaskData()+"\n");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
            }

        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        printfEditText.append("\n\n\n\n\n");
        printfEditText.append("                                                   打印时间："+simpleDateFormat.format(date));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        printfHistoryViewModel = ViewModelProviders.of(this).get(PrintfHistoryViewModel.class);
        // TODO: Use the ViewModel
    }
}
