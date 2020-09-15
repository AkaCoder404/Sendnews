package com.example.news.ui.covidData;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.news.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CovidDataFragment extends Fragment {

    HorizontalBarChart chart;
    HorizontalBarChart chart2;
    ProgressBar pg;
    HashMap<String, float[]> map = new HashMap<String, float[]>();
    HashMap<String, float[]> map2 = new HashMap<String, float[]>();
    ArrayList<BarEntry> dataVals;
    ArrayList<BarEntry> dataVals2;
    ArrayList<String> keyList;
    ArrayList<String> keyList2;
    int[] colorClassArray = new int[]{Color.RED, Color.GREEN, Color.YELLOW};
    Button button1;
    Button button2;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_covid_data, container, false);
//        Toast.makeText(getActivity(), "CovidDataFragment", Toast.LENGTH_SHORT).show();
//        final TextView textView = root.findViewById(R.id.textView);

        chart = (HorizontalBarChart) root.findViewById(R.id.countries);
        chart.setNoDataText("");
        chart.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        chart2 = (HorizontalBarChart) root.findViewById(R.id.china);
        chart2.setNoDataText("");

        pg = (ProgressBar) root.findViewById(R.id.progressbar);

        button1 = (Button) root.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.INVISIBLE);
                chart2.setVisibility(View.VISIBLE);
            }
        });

        button2 = (Button) root.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.VISIBLE);
                chart2.setVisibility(View.INVISIBLE);
            }
        });

        dataVals = new ArrayList<>();
        dataVals2 = new ArrayList<>();

        new Thread(new Runnable() {
            public void run() {
                URL url = null;
                HttpURLConnection con = null;

                try {
                    url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
                    con = (HttpURLConnection) url.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //Read JSON response and print
                    JSONObject jo = new JSONObject(response.toString());
                    Iterator iter = jo.keys();
                    while(iter.hasNext()) {
                        String key = (String) iter.next();
                        if (evaluate(key, "countries")) {
                            JSONObject obj = jo.getJSONObject(key);
                            JSONArray data = obj.getJSONArray("data");
                            JSONArray cases =(JSONArray) data.get(data.length() - 1);
                            float [] fdata = new float [3];
                            fdata[0] = (float) cases.getDouble(3);
                            fdata[1] = (float) cases.getDouble(2) - fdata[0];
                            fdata[2] = (float) cases.getDouble(0) - fdata[1];
                            map.put(key, fdata);
                        }
                        else if (evaluate(key, "china")) {
//                            Log.d("debug", key);
                            JSONObject obj = jo.getJSONObject(key);
                            JSONArray data = obj.getJSONArray("data");
                            JSONArray cases =(JSONArray) data.get(data.length() - 1);
                            float [] fdata = new float [3];
                            fdata[0] = (float) cases.getDouble(3);
                            fdata[1] = (float) cases.getDouble(2) - fdata[0];
                            fdata[2] = (float) cases.getDouble(0) - fdata[1];
                            map2.put(key.substring(6, key.length()), fdata);
//                            Log.d("debug", key.substring(6, key.length()));
//                            Log.d("debug", fdata[0] + " " + fdata[1] + " " + fdata[2]);
                        }
                    }
                    keyList = new ArrayList(map.keySet());
                    keyList2 = new ArrayList(map2.keySet());

                    float count = 0;
                    for (Map.Entry<String,float[]> entry : map.entrySet()) {
                        dataVals.add(new BarEntry(count, entry.getValue()));
                        count = count + 1;
                    }
                    count = 0;
                    for (Map.Entry<String,float[]> entry : map2.entrySet()) {
                        dataVals2.add(new BarEntry(count, entry.getValue()));
                        count = count + 1;
                    }

                    Handler threadHandler = new Handler(Looper.getMainLooper());
                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            pg.setVisibility(View.GONE);

                            drawGraph(chart, dataVals, keyList);
                            drawGraph(chart2, dataVals2, keyList2);
//                            for (int i = 0; i < dataVals.size(); i++) {
//                                BarEntry entry = dataVals.get(i);
//                                Log.d("debug", String.valueOf(entry.getX()));
//                                for (int j = 0; j < 3; j++) {
//                                    Log.d("debug", String.valueOf(entry.getYVals()[j]));
//                                }
//                            }

                            // Styling

//                            XAxis xAxis = chart.getXAxis();
//                            xAxis.setValueFormatter(new IndexAxisValueFormatter(keyList));
//
//                            xAxis.setDrawLabels(true);
//                            xAxis.setDrawAxisLine(true);
//                            xAxis.setDrawGridLines(false);
//                            xAxis.setTextColor(getResources().getColor(R.color.white));
//                            xAxis.setAxisLineColor(getResources().getColor(android.R.color.white));
//                            xAxis.setAxisLineWidth(2);  // y ashix line size
//                            xAxis.setGranularityEnabled(true);
//                            xAxis.setCenterAxisLabels(false);
//                            xAxis.setGranularity(1f);
//                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//                            YAxis yAxis = chart.getAxisLeft();
//                            yAxis.setDrawAxisLine(true);
//                            yAxis.setDrawGridLines(false);
//                            yAxis.setTextColor(getResources().getColor(R.color.white));
//                            yAxis.setAxisLineColor(getResources().getColor(R.color.white));
//                            yAxis.setAxisLineWidth(2);  // y ashix line size
//                            yAxis.setAxisMinValue(0);
//                            yAxis.setLabelCount(5);
//
//                            YAxis rightAxis = chart.getAxisRight();
//                            rightAxis.setDrawLabels(false);
//
//                            chart.setBackgroundColor(getResources().getColor(R.color.pHubSideBar));
//                            chart.setGridBackgroundColor(getResources().getColor(R.color.pHubSideBar));
//
//                            BarDataSet barDataSet = null;
//                            barDataSet = new BarDataSet(dataVals,"");
//                            barDataSet.setColors(colorClassArray);
//                            barDataSet.setDrawValues(false);
//
//                            BarData barData = new BarData(barDataSet);
//                            barData.setBarWidth(1f);
//
//                            chart.setData(barData);
//                            chart.getDescription().setText("");
//                            chart.setVerticalScrollBarEnabled(true);
//                            chart.setHorizontalScrollBarEnabled(true);
//                            chart.getLegend().setEnabled(true);
//                            chart.getLegend().setTextColor(getResources().getColor(R.color.white));
//                            Legend legend = chart.getLegend();
//
//                            String [] titleList = new String[] {"Deaths", "Cured", "Confirmed"};
//                            List<LegendEntry> entries = new ArrayList<>();
//                            for (int i = 0; i < 3; i++) {
//                                LegendEntry entry = new LegendEntry();
//                                entry.formColor = colorClassArray[i];
//                                entry.label = titleList[i];
//                                entries.add(entry);
//                            }
//                            legend.setCustom(entries);
//
//
//                            chart.setDrawGridBackground(false);
//                            chart.setDoubleTapToZoomEnabled(false);
//                            chart.setVisibleXRangeMaximum(25);
//                            chart.moveViewToX(1);
//
//                            chart.notifyDataSetChanged();
//                            chart.invalidate();
//
//                            xAxis.setLabelCount(202, true);
//                            xAxis.setTextSize(10f);
//                            Log.d("debug", "label count: " + String.valueOf(xAxis.getLabelCount()));
                        }
                    });

                    Log.d("debug", "datavals size" + String.valueOf(dataVals.size()));
                    Log.d("debug", "run success");

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.d("debug", "run fail");
                }

            }
        }).start();

        Log.d("debug", "datavals size" + String.valueOf(dataVals.size()));

        return root;
    }


    private void drawGraph(HorizontalBarChart chart, ArrayList<BarEntry> dataVals, ArrayList<String> keys) {
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(keys));

        xAxis.setDrawLabels(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(getResources().getColor(R.color.white));
        xAxis.setAxisLineColor(getResources().getColor(android.R.color.white));
        xAxis.setAxisLineWidth(2);  // y ashix line size
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawAxisLine(true);
        yAxis.setDrawGridLines(false);
        yAxis.setTextColor(getResources().getColor(R.color.white));
        yAxis.setAxisLineColor(getResources().getColor(R.color.white));
        yAxis.setAxisLineWidth(2);  // y ashix line size
        yAxis.setAxisMinValue(0);
        yAxis.setLabelCount(5);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawLabels(false);

        chart.setBackgroundColor(getResources().getColor(R.color.pHubSideBar));
        chart.setGridBackgroundColor(getResources().getColor(R.color.pHubSideBar));

        BarDataSet barDataSet = null;
        barDataSet = new BarDataSet(dataVals,"");
        barDataSet.setColors(colorClassArray);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(1f);

        chart.setData(barData);
        chart.getDescription().setText("");
        chart.setVerticalScrollBarEnabled(true);
        chart.setHorizontalScrollBarEnabled(true);
        chart.getLegend().setEnabled(true);
        chart.getLegend().setTextColor(getResources().getColor(R.color.white));
        Legend legend = chart.getLegend();

        String [] titleList = new String[] {"Deaths", "Cured", "Confirmed"};
        List<LegendEntry> entries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LegendEntry entry = new LegendEntry();
            entry.formColor = colorClassArray[i];
            entry.label = titleList[i];
            entries.add(entry);
        }
        legend.setCustom(entries);


        chart.setDrawGridBackground(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setVisibleXRangeMaximum(25);
        chart.moveViewToX(1);

        chart.notifyDataSetChanged();
        chart.invalidate();

        xAxis.setLabelCount(dataVals.size(), true);
        xAxis.setTextSize(10f);
        Log.d("debug", "label count: " + String.valueOf(xAxis.getLabelCount()));
    }


    private boolean evaluate (String key, String select) {
        if (select == "china") {
            int count = key.length() - key.replace("|", "").length();
            if (key.substring(0, 4).equals("Chin") && count == 1) {
                return true;
            }
        }
        else if (!key.contains("|") && select == "countries") {
            if (!key.substring(0, 4).equals("Worl")) return true;
        }

        return false;
    }

//    public void sortMap() {
//        Collections.sort(map, new Comparator<Map.Entry<String, float[]>>() {
//            public int compare(Map.Entry<String, float[]> o1,
//                               Map.Entry<String, float[]> o2) {
//                return (o1.getValue()[2].compareTo(o2.getValue()[2]));
//            }
//        });
//
//    }



//    private ArrayList<BarEntry> dataValues (final String select) {
//        dataVals = new ArrayList<>();
//        LinkedHashMap<String, int[]> smap = new LinkedHashMap<String, int[]>();
////        final HashMap<String, float[]> map = new HashMap<String, float[]>();
//        new Thread(new Runnable() {
//            public void run() {
//                URL url = null;
//                HttpURLConnection con = null;
//
//                try {
//                    url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
//                    con = (HttpURLConnection) url.openConnection();
//                    BufferedReader in = new BufferedReader(
//                            new InputStreamReader(con.getInputStream()));
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//                    //Read JSON response and print
//                    JSONObject jo = new JSONObject(response.toString());
//                    Iterator iter = jo.keys();
////                    HashMap<String, int[]> map = new HashMap<String, int[]>();
//                    while(iter.hasNext()) {
//                        String key = (String) iter.next();
//                        if (evaluate(key, select)) {
//                            JSONObject obj = jo.getJSONObject(key);
//                            JSONArray data = obj.getJSONArray("data");
//                            JSONArray cases =(JSONArray) data.get(data.length() - 1);
//                            float [] fdata = new float [3];
//                            fdata[0] = (float) cases.getDouble(0);
//                            fdata[1] = (float) cases.getDouble(2);
//                            fdata[2] = (float) cases.getDouble(3);
//                            map.put(key, fdata);
//                        }
//                    }
//                    float count = 0;
//                    for (Map.Entry<String,float[]> entry : map.entrySet()) {
////                        Log.d("debug", String.valueOf(entry.getKey()));
////                        for (int i = 0; i < 3; i++) Log.d("debug", String.valueOf(entry.getValue()[i]));
//                        dataVals.add(new BarEntry(count, entry.getValue()));
//                        count = count + 1;
//                    }
//                    Handler threadHandler = new Handler(Looper.getMainLooper());
//                    threadHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            for (int i = 0; i < dataVals.size(); i++) {
//                                BarEntry entry = dataVals.get(i);
//                                Log.d("debug", String.valueOf(entry.getX()));
//                                for (int j = 0; j < 3; j++) {
//                                    Log.d("debug", String.valueOf(entry.getYVals()[j]));
//                                }
//                            }
//                        }
//                    });
//
//                    Log.d("debug", "datavals size" + String.valueOf(dataVals.size()));
//
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                    Log.d("debug", "run fail");
//                }
//
//            }
//        }).start();
//
//        Log.d("debug", "datavals size" + String.valueOf(dataVals.size()));
//        return dataVals;
//
//    }
}