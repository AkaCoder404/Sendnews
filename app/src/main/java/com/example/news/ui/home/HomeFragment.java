package com.example.news.ui.home;

import android.content.Context;
import android.content.Intent;
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

import com.example.news.activities.NewsDetailActivity;
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
    private final String TAG_HomeFragment = "HomeFragment";
    private TextView topHeadline;
    private Context context;

    // Adding Cards
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsListAdapter adapter;

    //Swipe to Refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //Pagination
    private PaginationAdapter paginationAdapter;
    private List<Pagination> values = new ArrayList<>();

    //New Activity
    public Intent mintent;
    public Article marticle;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        //textView = root.findViewById(R.id.text_home);

        //Cards
        // Get Activity

        //Swipe Up to Refresh
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        layoutManager = new LinearLayoutManager(context);

        // RecyclerView + Adapter
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsListAdapter(articles, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
    //Load From Website - Working --> Not with Keyword
    public void LoadJson(final String keyword){
        ApiInterface apiInterface = Api.getApiClient("https://covid-dashboard.aminer.cn/api/dist/").create(ApiInterface.class);
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
                    setUpRecyclerView(articles);

                }
                else {
                    Toast.makeText(context,"No Result", Toast.LENGTH_SHORT).show();
                }
                System.out.println(articles.size() + " have been read");
                Log.d(TAG_HomeFragment, "On Response Finished");

            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG_HomeFragment, "Connection Failure");
            }
        });

    }
    //RecyclerView
    private void setUpRecyclerView(List<Article> articles) {
        adapter = new NewsListAdapter(articles, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        initListener();
        adapter.notifyDataSetChanged();

    }

    //Infinite Scroll -> implement later
    //Click Cards

    private void initListener() {
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG_HomeFragment, "Item Clicked and Sending Intent");
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                Article article = articles.get(position);
                intent.putExtra("url", article.getId());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("date", article.getTime());
                startActivity(intent);
            }
        });
    }




    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) { }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {   }



}