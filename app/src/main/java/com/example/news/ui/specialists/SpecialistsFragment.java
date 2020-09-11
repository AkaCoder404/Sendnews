package com.example.news.ui.specialists;

import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.news.R;
import com.example.news.activities.SpecialistActivity;
import com.example.news.adapters.ExpertsAdapter;
import com.example.news.adapters.NewsListAdapter;
import com.example.news.api.Api;
import com.example.news.api.ApiInterface;
import com.example.news.api.SpecialistsApiInterface;
import com.example.news.models.Article;
import com.example.news.models.Specialist;
import com.example.news.models.Specialists;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecialistsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = "SpecialistFragment";
    private List<Specialist> specialists = new ArrayList<>();
    private int ExpertsCount = 10;
    private int mPosition;
    //Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ExpertsAdapter adapter;
    private TextView textView;
    private Context context;
    //Swipe to Refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //Error
    private RelativeLayout errorLayout;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button errorButton;
    //


    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        View root = layoutInflater.inflate(R.layout.fragment_specialists, container, false);
        setHasOptionsMenu(true);
        context = getActivity();

        //Headline
        textView = root.findViewById(R.id.text_home);
        textView.setText("Experts");

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

        // RecyclerView + LayoutManager + Adapter
        recyclerView = root.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExpertsAdapter(specialists, context);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //Detect When Reaches Bottom
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Toast.makeText(context, "Last, Loaded More", Toast.LENGTH_LONG).show();
                    ExpertsCount += 10;
                    adapter.setLimit(ExpertsCount);
                    recyclerView.setAdapter(adapter);
                    recyclerView.scrollToPosition(ExpertsCount - 10);
                }
            }
        });

        //Load Json file
        onLoadingSwipeRefresh("");

        return root;
    }
    // Load Information from experts api
    public void LoadJson(final String keyword) {
        Log.d(TAG, "calling experts api");
        mSwipeRefreshLayout.setRefreshing(true);
        SpecialistsApiInterface apiInterface = Api.getApiClient("https://innovaapi.aminer.cn/predictor/api/v1/valhalla/highlight/").create(SpecialistsApiInterface.class);
        Call<Specialists> call;

        call = apiInterface.getExperts("2");

        call.enqueue(new Callback<Specialists>() {
            @Override
            public void onResponse(Call<Specialists> call, Response<Specialists> response) {
                Log.d(TAG, "connection response");
                if(response.isSuccessful() && response.body().getSpecialistList() != null) {
                    if(!specialists.isEmpty()) {
                        specialists.clear();
                    }
                    specialists = response.body().getSpecialistList();
                    // testOutput();
                    setUpRecyclerView(specialists);
                }
                else {
                    Log.d(TAG, "cannot load");
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
            }
            @Override
            public void onFailure(Call<Specialists> call, Throwable t) {
                Log.d(TAG, "connection failure");
                showErrorMessage(R.drawable.error, "No Result", "Network Failure _\n" + t.toString());
            }
        });
    }
    //RecyclerView
    private void setUpRecyclerView(List<Specialist> specialistList) {
        adapter = new ExpertsAdapter(specialistList, context);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        initListner();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    // start intent for specialist
    private void initListner() {
        adapter.setOnItemClickListener(new ExpertsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "item clicked ad sending intent");
                Intent intent = new Intent(getActivity(), SpecialistActivity.class);
                    mPosition = position;
                    Specialist specialist = specialists.get(position);
                    intent.putExtra("avatar", specialist.getAvatar());
                    intent.putExtra("bind", specialist.getBind());
                    intent.putExtra("activity", specialist.getIndices().getActivity());
                    intent.putExtra("citations", specialist.getIndices().getCitations());
                    intent.putExtra("diversity", specialist.getIndices().getDiversity());
                    intent.putExtra("gindex", specialist.getIndices().getGindex());
                    intent.putExtra("hindex", specialist.getIndices().getHindex());
                    intent.putExtra("newStar", specialist.getIndices().getNewStar());
                    intent.putExtra("risingStar", specialist.getIndices().getRisingStar());
                    intent.putExtra("sociability", specialist.getIndices().getSociability());
                    intent.putExtra("name", specialist.getName());
                    intent.putExtra("name_zh", specialist.getName_zh());
                    intent.putExtra("num_followed", specialist.getNum_followed());
                    intent.putExtra("num_viewed", specialist.getNum_viewed());
                    intent.putExtra("address", specialist.getProfile().getAddress());
                    intent.putExtra("affiliation", specialist.getProfile().getAffiliation());
                    intent.putExtra("affiliation_zh", specialist.getProfile().getAffiliation_zh());
                    intent.putExtra("bio", specialist.getProfile().getBio());
                    intent.putExtra("edu", specialist.getProfile().getAddress());
                    intent.putExtra("email", specialist.getProfile().getEmail());
                    intent.putExtra("emails_cr", specialist.getProfile().getEmail_cr());
                    intent.putExtra("fax", specialist.getProfile().getFax());
                    intent.putExtra("homepage", specialist.getProfile().getHomepage());
                    intent.putExtra("note", specialist.getProfile().getNote());
                    intent.putExtra("position", specialist.getProfile().getPosition());
                    intent.putExtra("work", specialist.getProfile().getWork());
                    intent.putExtra("is_passedaway", specialist.getIs_passedaway());
                    startActivity(intent);
            }
        });
    }
    // Refreshing
    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword) {
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }
    //Error
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
    //Test Output
    private void testOutput() {
        System.out.println(specialists.get(0).getName() +
                "," + specialists.get(0).getName_zh() +
                "," + specialists.get(9).getProfile().getEdu());
    }
    //Search
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.app_bar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Service.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("Search Experts...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 2 ) {
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
}
