package com.example.news.ui.home;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.Constants;
import com.example.news.activities.NewsDetailActivity;
import com.example.news.adapters.NewsListAdapter;
import com.example.news.R;
import com.example.news.api.Api;
import com.example.news.api.ApiInterface;
import com.example.news.data.History;
import com.example.news.data.HistoryDao;
import com.example.news.data.HistoryDatabase;
import com.example.news.data.HistoryViewModel;
import com.example.news.models.Article;
import com.example.news.models.News;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SwipyRefreshLayout.OnRefreshListener,
        SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener  {

    //Variables
    private final String TAG = "HomeFragment";
    private int NewsCount = 10;
    private List<Article> articles = new ArrayList<>();
    private List<Article> articleTemp = new ArrayList<>();
    private int mPosition;
    //Views
    public TextView textView;
    private FrameLayout mlayoutDate;
    private Context context;
    // Adding Cards
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private NewsListAdapter adapter;
    //Swipe to Refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SwipyRefreshLayout mSwipyRefreshLayout;
    //Error
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    //Preferences
    private SharedPreferences sharedPreferences;
    private HistoryViewModel historyViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        context = getActivity();


        //Headline + Date
        textView = root.findViewById(R.id.text_home);
        textView.setText("Top Headlines");
        // textView.setVisibility(View.GONE);

        //Shared Preferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary));

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
                    Toast.makeText(context, "Last, Loaded More", Toast.LENGTH_LONG).show();
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
            System.out.println((articles.size()));
            // Temporary Solution to not reload all articles
        }
        else{
            System.out.println((articles.size()));
                articles = new ArrayList<> (Constants.articles);
            System.out.println((articles.size()));

            if (articles != null) {
                setUpRecyclerView(articles);
            }
            else textView.setText("Swipe Down to Load News");
        }





        // onRefresh(SwipyRefreshLayoutDirection.TOP);
        return root;
    }
    //Load From Website
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
                Log.d(TAG, "On Response Started");
                textView.setText("Top Headlines");
                if(response.isSuccessful() && response.body().getArticle() != null) {
                    if(!articles.isEmpty()) {
                        articles.clear();
                    }
                    articles = response.body().getArticle();
                    // Save Articles --> don't have to regenerate unless reloaded
                    Constants.articles = new ArrayList<>(articles);
                    // Test Article
                    // testOutput(articles.get(0));

                    textView.setVisibility(View.VISIBLE);
                    setUpRecyclerView(articles);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    //mSwipyRefreshLayout.setRefreshing(false);
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
                Log.d(TAG, "On Response Finished");

            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.d(TAG, "Connection Failure");
                mSwipeRefreshLayout.setRefreshing(false);
                //mSwipyRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.error, "No Result", "Network Failure _\n"+ t.toString());
            }
        });
    }
    //RecyclerView
    private void setUpRecyclerView(List<Article> articles) {
        adapter = new NewsListAdapter(articles, context);
        adapter.getFilter().filter("");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListener();
        mSwipeRefreshLayout.setRefreshing(false);


        //mSwipyRefreshLayout.setRefreshing(false);
    }
    //Go to News Detail Page
    private void initListener() {
        adapter.setOnItemClickListener(new NewsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "item clicked and sending intent");
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                mPosition = position;
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
                        adapter.setLimit(NewsCount);
                        LoadJson(keyword);
                    }
                }
        );
    }
    //Show Error Message
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
    //Swipy implementation --> failed
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

    //Override Search Functionality
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.app_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Service.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Latest News...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2 ) {
                    adapter.getFilter().filter(s);
                    System.out.println("Searched");
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println("Searching");
                adapter.getFilter().filter(s);
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
    }
    //Override Menu Functionality
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("Item is selected");
        if(item.isChecked()) item.setChecked(false);
        else item.setChecked(true);

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return onOptionsItemSelected(item);
        }
    }
    //Test Output
    public void testOutput(Article article) {
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
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);;
        System.out.println("On Resume Article Size: " + articles.size());
        Toast.makeText(getActivity(), "Main Page", Toast.LENGTH_SHORT).show();
        adapter.getFilter().filter("");

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this);
        System.out.println("On Pause Article Size: " + articles.size());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }
}