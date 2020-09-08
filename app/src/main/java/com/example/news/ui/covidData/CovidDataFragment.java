package com.example.news.ui.covidData;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.R;

public class CovidDataFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_covid_data, container, false);
        Toast.makeText(getActivity(), "CovidDataFragment", Toast.LENGTH_SHORT).show();
//        final TextView textView = root.findViewById(R.id.text_slideshow);
        return root;
    }
}