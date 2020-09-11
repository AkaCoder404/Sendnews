package com.example.news.ui.knowledgeGraph;

import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.Constants;
import com.example.news.R;
import com.example.news.activities.KnowledgeGraphActivity;
import com.example.news.adapters.KnowledgeGraphAdapter;
import com.example.news.adapters.NewsListAdapter;
import com.example.news.api.Api;
import com.example.news.api.ApiInterface;
import com.example.news.api.KnowledgeEntityApiInterface;
import com.example.news.models.KnowledgeEntities;
import com.example.news.models.KnowledgeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeGraphFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    //Variables
    private String TAG = "KnowledgeGraph";
    private int EntityCount = 10;
    private List<KnowledgeEntity> knowledgeEntityList = new ArrayList<>();

    //Views
    private TextView mTextView;
    private Context mContext;

    //Swipe to Refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //Error
    private RelativeLayout mErrorLayout;
    private ImageView mErrorImage;
    private TextView mErrorTitle, mErrorMessage;
    private Button mErrorButton;

    //RecyclerView, Adapter,
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private KnowledgeGraphAdapter mAdapter;

    //Preferences
    private SharedPreferences mSharedPreferences;


    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View root = layoutInflater.inflate(R.layout.fragment_knowledge_graph, container, false);
        setHasOptionsMenu(true);
        mContext = getActivity();

        //TextView, Refresh, Error
        mTextView = root.findViewById(R.id.text_home);
        mTextView.setText("Knowledge Entities");
        mSwipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorPrimary));
        mErrorLayout = root.findViewById(R.id.errorLayout);
        mErrorImage = root.findViewById(R.id.errorImage);
        mErrorMessage = root.findViewById(R.id.errorMessage);
        mErrorTitle = root.findViewById(R.id.errorTitle);
        mErrorButton = root.findViewById(R.id.errorButton);

        //RecyclerView, LayoutManager, Adapter
        mRecyclerView = root.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new KnowledgeGraphAdapter(knowledgeEntityList, mContext);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        //detect when user scrolls to bottom, then load 10 more cards
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Toast.makeText(mContext, "No More", Toast.LENGTH_LONG).show();
                }
            }
        });

        onLoadingSwipeRefresh("病毒");

        return root;
    }
    //load from api
    public void LoadJson(final String keyword) {
        mErrorLayout.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);
        KnowledgeEntityApiInterface apiInterface = Api.getApiClient(
                "https://innovaapi.aminer.cn/covid/api/v1/pneumonia/").
                create(KnowledgeEntityApiInterface.class);

        Call<KnowledgeEntities> call;
        call = apiInterface.getEntities(keyword);

        call.enqueue(new Callback<KnowledgeEntities>() {
            @Override
            public void onResponse(Call<KnowledgeEntities> call,
                                   Response<KnowledgeEntities> response) {
                Log.d(TAG, "On Response Started");
                if(response.isSuccessful() && response.body().getEntityList() != null) {
                    if(!knowledgeEntityList.isEmpty()) {
                        knowledgeEntityList.clear();
                    }
                    knowledgeEntityList = response.body().getEntityList();
//                    Map<String, String> maps = knowledgeEntityList.get(0).getAbstractInfo().getCovid().getProperties();
//                    System.out.println(maps);
                    //testOutput();
                    setUpRecyclerView(knowledgeEntityList);
                }
                else {
                    mSwipeRefreshLayout.setRefreshing(false);
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
                Log.d(TAG, knowledgeEntityList.size() + " has been read");
                Log.d(TAG, "On Response Finished");
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<KnowledgeEntities> call, Throwable t) {
                Log.d(TAG, "Connection Failure");
                mSwipeRefreshLayout.setRefreshing(false);
                showErrorMessage(R.drawable.error, "No Result", "Network Failure _\n"+ t.toString());
            }
        });
    }
    //set up recycler view
    public void setUpRecyclerView(List<KnowledgeEntity> knowledgeEntityList) {
        mAdapter = new KnowledgeGraphAdapter(knowledgeEntityList, mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        initListner();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //activity to show specific page for
    public void initListner() {
        mAdapter.setOnItemClickListener(new KnowledgeGraphAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "item clicked and sending intent");
                Intent intent = new Intent(getActivity(), KnowledgeGraphActivity.class);
                KnowledgeEntity knowledgeEntity = knowledgeEntityList.get(position);
                intent.putExtra("hot", knowledgeEntity.getHot());
                intent.putExtra("url", knowledgeEntity.getUrl());
                intent.putExtra("img", knowledgeEntity.getImg());
                intent.putExtra("label", knowledgeEntity.getLabel());
                intent.putExtra("enwiki", knowledgeEntity.getAbstractInfo().getEnwiki());
                intent.putExtra("baidu", knowledgeEntity.getAbstractInfo().getBaidu());
                intent.putExtra("zhwiki", knowledgeEntity.getAbstractInfo().getZhwiki());
                intent.putExtra("定义", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getDefinition());
                intent.putExtra("特征", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getCharacteristics());
                intent.putExtra("包括", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getIncludes());
                intent.putExtra("生存条件", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getLivingConditions());
                intent.putExtra("传播方式", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getInfectionType());
                intent.putExtra("应用", knowledgeEntity.getAbstractInfo().getCovid().getProperty().getUses());
                startActivity(intent);
            }
        });
    }

    //refresh
    @Override
    public void onRefresh() {
        LoadJson("");
    }

    //swipe down to refresh
    public void onLoadingSwipeRefresh(final String keyword) {
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }

    //test api reading
    public void testOutput() {
        System.out.println("\nKnowledge List\n" +
                knowledgeEntityList.get(0).getLabel() + "\n" +
                knowledgeEntityList.get(0).getUrl() + "\n" +
                knowledgeEntityList.get(0).getImg() + "\n" +
                knowledgeEntityList.get(0).getAbstractInfo().getBaidu() + "\n" +
                knowledgeEntityList.get(0).getAbstractInfo().getCovid().getProperty().getDefinition());
    }
    //Error Message
    private void showErrorMessage(int imageView, String title, String message) {
        if (mErrorLayout.getVisibility() == View.GONE) {
            mErrorLayout.setVisibility(View.VISIBLE);
        }
        mErrorImage.setImageResource(imageView);
        mErrorTitle.setText(title);
        mErrorMessage.setText(message);

        mErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoadingSwipeRefresh("");
            }
        });
    }

    //override menu functionality and search functionality
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Service.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Entities");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                LoadJson(newText);
                //mAdapter.getFilter().filter();
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);

    }
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






}


