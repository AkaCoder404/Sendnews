package com.example.news.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.adapters.NewsListAdapter;
import com.example.news.R;
import com.example.news.adapters.PaginationAdapter;
import com.example.news.api.Api;
import com.example.news.api.ApiInterface;
import com.example.news.models.Article;
import com.example.news.models.News;
import com.example.news.models.Pagination;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements AbsListView.OnScrollListener{

    //Static Variables
    static TextView textView;
    public static String data;
    String url="https://covid-dashboard.aminer.cn/api/event/5f05f3f69fced0a24b2f84ee";
    String urlTest = "http://newsapi.org/v2/everything?q=bitcoin&from=2020-08-04&sortBy=publishedAt&apiKey=API_KEY";
    //Variables
    private final static String TAG_HomeFragment = "HomeFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeFragmentAdapter mHomeFragmentAdapter;
    private HomeViewModel homeViewModel;
    private TextView secondText;
    private Button button1;
    static Context context;

    // Adding Cards
    private static RecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static List<Article> articles = new ArrayList<>();
    private NewsListAdapter adapter;

    //Pagination
    private PaginationAdapter paginationAdapter;
    private List<Pagination> values = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        //textView = root.findViewById(R.id.text_home);

//        recyclerView = root.findViewById(R.id.recycler_view);
//        mHomeFragmentAdapter = new HomeFragmentAdapter();
//        recyclerView.setAdapter(mHomeFragmentAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);

          //Cards
        // Get Activity

        //Swipe Up to Refresh
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        // RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //recyclerView.setNestedScrollingEnabled(false);
        adapter = new NewsListAdapter(articles, context);
        recyclerView.setAdapter(adapter);

        LoadJson("");


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d(TAG_HomeFragment, "onRefresh called from SwipeRefreshLayout");
//                            LoadJson();
                    //layoutManager = new LinearLayoutManager(context);
                    //recyclerView.setLayoutManager(layoutManager);
                    LoadJson("");
                    adapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        return root;
    }
    //Load From Website - Working
    public static void LoadJson(final String keyword){
        ApiInterface apiInterface = Api.getApiClient().create(ApiInterface.class);
        Call<News> call;

        if(keyword.length() > 0) {
            call = apiInterface.searchNews(keyword);
        } else {
            call = apiInterface.getNews();
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                Log.d(TAG_HomeFragment, "On Response Started");
                if(response.isSuccessful() && response.body().getArticle() != null) {
                    if(!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticle();
//                    for (Article r : articles) {
//                        Log.d("Id", r.getId());
//                        Log.d("Title", r.getTitle());
//                    }
                }
                else {
                    Toast.makeText(context,"No Result", Toast.LENGTH_SHORT).show();
                }
                System.out.println(articles.size() + " have been read");
                Log.d(TAG_HomeFragment, "On Response Finished");
                setUpRecyclerView(articles);
            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG_HomeFragment, "Connection Failure");
            }
        });

    }
    //RecyclerView
    private static void setUpRecyclerView(List<Article> articles) {

        NewsListAdapter adapter = new NewsListAdapter(articles, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //Infinite Scroll

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) { }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {   }


}