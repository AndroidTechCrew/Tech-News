package com.AndroidTechCrew.technews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.HomeFeedFragment;
import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public static final String TAG = "test";
    Context context;
    List<News> news;

    public NewsAdapter(Context context, List<News> news){
        this.news = news;
        this.context = context;
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

    public static Drawable getImageFromURL(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable img = Drawable.createFromStream(is,"fill");
            return img;
        }
        catch(Exception e){
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDesc;
        RelativeLayout rlBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            rlBox = itemView.findViewById(R.id.rlBox);
        }

        public void bind(News news){
            tvTitle.setText(news.getTitle());
            tvDesc.setText(news.getDescription());
            Glide.with(context).load(news.getImageURL()).into(ivImage);
        }
    }
}
