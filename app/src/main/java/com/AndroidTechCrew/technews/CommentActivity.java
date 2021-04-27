package com.AndroidTechCrew.technews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CommentActivity extends AppCompatActivity {

    private ImageView articleImage;
    private TextView articleTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        articleImage = findViewById(R.id.ivCommentImage);
        articleTitle = findViewById(R.id.tvCommentTitle);

        String title = getIntent().getStringExtra("title");
        articleTitle.setText(title);

        String imageURL = getIntent().getStringExtra("image");
        Glide.with(this).load(imageURL).into(articleImage);

    }
}