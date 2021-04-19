package com.AndroidTechCrew.technews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.SavedNewsFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.ViewHolder> {

    private Context context;
    private List<SavedNews> savedNews;

    public SavedNewsAdapter(Context context, List<SavedNews> savedNews) {
        this.context = context;
        this.savedNews = savedNews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedNews savedNew = savedNews.get(position);
        holder.bind(savedNew);

    }

    @Override
    public int getItemCount() {
        return savedNews.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvDescription;
        private ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

        public void bind(SavedNews savedNew) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            tvDescription.setText(News.getDescription());
            tvTitle.setText(News.getTitle());
            Glide.with(context)
                    .load(storageReference)
                    .into(ivImage);

        }
    }
}
