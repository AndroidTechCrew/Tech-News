package com.AndroidTechCrew.technews.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.AndroidTechCrew.technews.News;

import com.AndroidTechCrew.technews.NewsAdapter;

import com.AndroidTechCrew.technews.R;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Headers;


public class HomeFeedFragment extends Fragment {
    private static final String APIKEY = "aa399d936ced443897beb61eed279f80";
    public static final String TAG = "test";
    RecyclerView rvNews;
    ArrayList<News> news;
    NewsAdapter adapter;


    public HomeFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      
        //Log.i(TAG,News.getArticles().toString());
        news = new ArrayList<>();
        //Log.i(TAG,String.valueOf(news.size()));
        getArticles(view, news);
    }
    private void initializeRV(View view){
        RecyclerView recyclerView = view.findViewById(R.id.rvNews);
        NewsAdapter adapter = new NewsAdapter(getContext(),news);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getArticles(View view, ArrayList<News> news){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://newsapi.org/v2/everything?q=tech&sortBy=publishedAt&pageSize=5&apiKey=" + APIKEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    //Log.i(TAG,json.toString());
                    news.addAll(News.jsonToArray(json.jsonObject));
                    initializeRV(view);
                } catch (JSONException e) {
                    //Log.i(TAG,"In catch");
                    e.printStackTrace();
                    initializeRV(view);
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG,"Failed");
            }
        });
    }
}