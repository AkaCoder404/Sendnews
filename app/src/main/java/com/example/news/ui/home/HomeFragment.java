package com.example.news.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.Constants;
import com.example.news.activities.NewsDetailActivity;
import com.example.news.adapters.NewsListAdapter;
import com.example.news.R;
import com.example.news.adapters.PaginationAdapter;
import com.example.news.api.Api;
import com.example.news.api.ApiInterface;
import com.example.news.models.Article;
import com.example.news.models.News;
import com.example.news.models.Pagination;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener, SwipeRefreshLayout.OnRefreshListener {

    //Static Variables
    static TextView textView;
    public static String data;
    //Variables
    private final String TAG_HomeFragment = "HomeFragment";
    private Context context;
    // Adding Cards
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private NewsListAdapter adapter;
    //Swipe to Refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //Pagination --> not yet implemented
    private PaginationAdapter paginationAdapter;
    private List<Pagination> values = new ArrayList<>();
    //Error
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    //Swipe For More
    private int NewsCount = 10;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    //Change Card to Gray
    CardView cardView;
    Constants constant;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();

        //Headline
        textView = root.findViewById(R.id.text_home);
        textView.setVisibility(View.GONE);

        //Cards

        //Error
        errorLayout = root.findViewById(R.id.errorLayout);
        errorImage = root.findViewById(R.id.errorImage);
        errorMessage = root.findViewById(R.id.errorMessage);
        errorTitle = root.findViewById(R.id.errorTitle);
        errorButton = root.findViewById(R.id.errorButton);

        //Swipe Up to Refresh
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent));

        //CardView

        //Try to Implement Double Swipe --> implement later?
//        mSwipyRefreshLayout = root.findViewById(R.id.swipe_refresh);
//        mSwipyRefreshLayout.setOnRefreshListener(this);
//        mSwipyRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent));

        // RecyclerView + LayoutManager + Adapter
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsListAdapter(articles, context);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Detect When Reaches Bottom
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Toast.makeText(context, "Last", Toast.LENGTH_LONG).show();

                    NewsCount += 10;
                    adapter.setLimit(NewsCount);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(NewsCount - 10);
                }
            }
        });

        //First Load
        if (Constants.HomeFirstReload == true)  {
            onLoadingSwipeRefresh("");
            Constants.HomeFirstReload = false;
            // Temporary Solution to not reload all articles
        }
        else{
            articles = Constants.articles;
            System.out.println((articles.size()));
            adapter = new NewsListAdapter(articles, context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(adapter);
        }
        // onRefresh(SwipyRefreshLayoutDirection.TOP);
        return root;
    }
    //Load From Website - Working --> Not with Keyword
    public void LoadJson(final String keyword){
        errorLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        //mSwipyRefreshLayout.setRefreshing(true);

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
                    //
                    Constants.articles = articles;

                    // Temporary Stuff
                    System.out.println("\nFirst Article\n" +
                            "_id  :"    + articles.get(0).getId() + "\n" +
                            "type :"    + articles.get(0).getType() + "\n" +
                            "title:"    + articles.get(0).getTitle() + "\n" +
                            "cat  :"    + articles.get(0).getCategory() + "\n" +
                            "time :"    + articles.get(0).getTime() + "\n" +
                            "lang :"    + articles.get(0).getLang() + "\n" +
                            "geoinfo:"  + articles.get(0).getGeoInfo() + ",\n"  +
                              articles.get(0).getGeoInfo().get(0) + ":" +
                                                articles.get(0).getGeoInfo().get(0).getLongitude()  + "," +
                                                articles.get(0).getGeoInfo().get(0).getLatitude() + "," +
                                                articles.get(0).getGeoInfo().get(0).getOriginText() + "," +
                                                articles.get(0).getGeoInfo().get(0).getOriginText() + "\n" +
                            "infl :"    + articles.get(0).getInfluence());


                    textView.setVisibility(View.VISIBLE);
                    setUpRecyclerView(articles);
                }
                else {
                    // mSwipeRefreshLayout.setRefreshing(false);
                    mSwipyRefreshLayout.setRefreshing(false);
                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 Server broken";
                            break;
                        default:
                            errorCode = "Unknown Error";
                            break;
                    }
                    showErrorMessage(R.drawable.error, "No Result", "Please Try Again _\n" + errorCode);
                }
                System.out.println(articles.size() + " have been read");
                Log.d(TAG_HomeFragment, "On Response Finished");

            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG_HomeFragment, "Connection Failure");
                mSwipeRefreshLayout.setRefreshing(false);
                //mSwipyRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.error, "No Result", "Network Failure _\n"+ t.toString());
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
        mSwipeRefreshLayout.setRefreshing(false);
        //mSwipyRefreshLayout.setRefreshing(false);
    }

    private void initListener() {
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @SuppressLint("ResourceAsColor")
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
    public void onRefresh() {
        LoadJson("");
    }
    private void onLoadingSwipeRefresh(final String keyword) {
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        NewsCount = 10;
                        LoadJson(keyword);
                    }
                }
        );
    }

    private void showErrorMessage(int imageView, String title, String message) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadingSwipeRefresh("");
            }
        });

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at " + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            mSwipyRefreshLayout.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            NewsCount = 10;
                            LoadJson("");
                        }
                    });
        } else {
            adapter.setLimit(NewsCount += 10);
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(NewsCount);
            mSwipyRefreshLayout.setRefreshing(false);
            mSwipyRefreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
        }
    }
}