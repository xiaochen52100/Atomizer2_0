package com.example.atomizer2_0.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.atomizer2_0.Adapter.HistoryAdapter;
import com.example.atomizer2_0.Adapter.MoreHistoryAdapter;
import com.example.atomizer2_0.MainActivity;
import com.example.atomizer2_0.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.atomizer2_0.MainActivity.historyData;

public class MoreHistoryFragment extends Fragment {
    private MoreHistoryViewModel mMoreHistoryViewModel;
    private ListView moreHistoryList;
    private Button viewButton;
    private MoreHistoryAdapter moreHistoryAdapter;
    public static MoreHistoryFragment newInstance() {
        return new MoreHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        MainActivity.nowFragmentId=R.id.more_history_fragment;
        //MainActivity.barTitle.setText("历史记录");
        View root=inflater.inflate(R.layout.more_history, container, false);
        moreHistoryList = root.findViewById(R.id.moreHistoryList);

        //viewButton=root.findViewById(R.id.viewButton);

        moreHistoryAdapter=new MoreHistoryAdapter(getContext(),R.layout.more_history_item, historyData);
        moreHistoryList.setAdapter(moreHistoryAdapter);


//
//        viewButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.container, PrintfHistoryFragment.newInstance())
//                        .commitNow();
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMoreHistoryViewModel = ViewModelProviders.of(this).get(MoreHistoryViewModel.class);
        // TODO: Use the ViewModel
    }
}
