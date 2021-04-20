package com.AndroidTechCrew.technews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticlePageActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_page);
        String link = getIntent().getStringExtra("link");
        webView = (WebView) findViewById(R.id.wvArticlePage);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(link);
    }
}