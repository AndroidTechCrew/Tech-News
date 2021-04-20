package com.AndroidTechCrew.technews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;


import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.Headers;

public class News {
    private static final String TAG = "News" ;
    private static final String APIKEY = "";
  
    String source;
    String author;
    static String title;
    static String description;
    String articleLink;
    String imageURL;
    String publishDate;
    String content;

    public News(){}

    public News(JSONObject jsonObject) throws JSONException {
        source = jsonObject.getJSONObject("source").getString("name");
        author = jsonObject.getString("author");
        title = jsonObject.getString("title");
        description = jsonObject.getString("description");
        articleLink = jsonObject.getString("url");
        imageURL = jsonObject.getString("urlToImage");
        publishDate = jsonObject.getString("publishedAt").substring(0,10);
        content = jsonObject.getString("content");
    }

    public static ArrayList<News> jsonToArray(JSONObject jsObject) throws JSONException {
        ArrayList<News> techNewsArticles = new ArrayList<>();
        JSONArray response = jsObject.getJSONArray("articles");
        for(int i = 0; i < response.length(); i++){
            techNewsArticles.add(new News(response.getJSONObject(i)));
        }
        return techNewsArticles;
    }

    public String getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public static String getTitle() {
        return title;
    }

    public static String getDescription() {
        return description;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getContent() {
        return content;
    }


    public static ArrayList<News> getArticles() {
        ArrayList<News> articles = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://newsapi.org/v2/sources?category=technologyapiKey=" + APIKEY, new JsonHttpResponseHandler() {
            @Override
            //TODO
            //This link above is probably going to need a language filter, unless our user base is tri-lingual with Japanese, Hindi, and English.
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    articles.addAll(News.jsonToArray(json.jsonObject));
                    Log.i(TAG, articles.toString());
                } catch (JSONException e) {
                    Log.i(TAG, "In catch");
                }
            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "Failed");
            }
            //Log.i(TAG, articles.toString());
        });
        return articles;
    }

    public static void testapi() {
        ArrayList<News> articles = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("https://newsapi.org/v2/everything?q=tech&from=2021-03-09&sortBy=publishedAt&pageSize=5&apiKey=" + APIKEY, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    articles.addAll(News.jsonToArray(json.jsonObject));
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                for (News n : articles) {
                    Log.d(TAG, String.format("\n\nNews article: \nSource: %s\nAuthor: %s\nTitle: %s\nDescription: %s\nURL: %s\nImage URL: %s\nPublish date: %s\ncontent: %s\n",
                            n.getSource(), n.getAuthor(), n.getTitle(), n.getDescription(), n.getArticleLink(), n.getImageURL(), n.getPublishDate(), n.getContent()));
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "Failed");
            }
        });

    }
}
