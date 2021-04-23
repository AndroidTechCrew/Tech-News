package com.AndroidTechCrew.technews;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.HomeFeedFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public static final String TAG = "test";
    Context context;
    List<News> news;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user;

    public NewsAdapter(Context context, List<News> news){
        this.news = news;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.user = mAuth.getCurrentUser();
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        News article = news.get(position);
        Log.i(TAG,news.get(position).toString());
        holder.rlBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: GO to the article of the page
                Intent i = new Intent(context, ArticlePageActivity.class);
                i.putExtra("link", news.get(position).getArticleLink());
                context.startActivity(i);
            }
        });
        holder.bind(article);
    }

    @Override
    public int getItemCount() { return news.size(); }

    public void clear(){
        news.clear();
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<News> newsList){
        news.addAll(newsList);
        notifyDataSetChanged();
    }

    public void saveArticle(View view){
        Log.i(TAG,"Button pressed in adapter");
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDesc;
        RelativeLayout rlBox;
        Button btnSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            rlBox = itemView.findViewById(R.id.rlBox);
            btnSave = itemView.findViewById(R.id.btnSave);


        }

        public void bind(News news){
            tvTitle.setText(news.getTitle());
            tvDesc.setText(news.getDescription());
            Glide.with(context).load(news.getImageURL()).into(ivImage);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Test button");
                    db.collection("users/" + user.getUid() + "/savedNews");
                }
            });
        }
    }
}
