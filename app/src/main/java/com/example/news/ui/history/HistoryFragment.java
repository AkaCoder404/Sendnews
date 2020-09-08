package com.example.news.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.R;
import com.example.news.adapters.HistoryAdapter;
import com.example.news.data.History;
import com.example.news.data.HistoryViewModel;


import java.util.ArrayList;
import java.util.List;

public  class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView recyclerView;
    private List<History> histories = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        //final TextView textView = root.findViewById(R.id.text_slideshow);

        Toast.makeText(getActivity(), "History Fragment", Toast.LENGTH_SHORT).show();

        //Recycler View + Layout + Adapter
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        final HistoryAdapter adapter = new HistoryAdapter(histories, getActivity());
        recyclerView.setAdapter(adapter);

        //Swipe Refresh Turn Off
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setEnabled(false);

        historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);

        historyViewModel.getAllHistory().observe(getActivity(), new Observer<List<History>>() {
            @Override
            public void onChanged(@Nullable List<History> history) {
                //update RecyclerView
                adapter.setHistories(history);

            }
        });
        return root;
    }
}