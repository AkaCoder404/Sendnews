package com.example.news;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.news.models.Article;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Constants {
    //Websites
    public final static String covidDataUrl = "https://covid-dashboard.aminer.cn/api/dist/epidemic.json";
    public final static String covidEventsUrl = "https://covid-dashboard.aminer.cn/api/dist/events.json";
    public final static String covidEventsIdURL = "https://covid-dashboard-api.aminer.cn/event/[id]";
    public final static String covidEventsSearchUrl = "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery";
    //Query Example
    //https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=病毒
    public final static String covidExpertsListUrl = "https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/get_ncov_expers_list?v=2";

    public static boolean HomeFirstReload = true;
    public static List<Article> articles;

    public final static String[] categories = {
            "news",
            "paper",
            "event"
    };









}
