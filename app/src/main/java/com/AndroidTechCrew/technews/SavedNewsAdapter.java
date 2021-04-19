package com.AndroidTechCrew.technews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SavedNews savedNew = savedNews.get(position);
        holder.rvSavedNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "hiii", Toast.LENGTH_SHORT).show();
            }
        });
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
        private RelativeLayout rvSavedNews;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            rvSavedNews = itemView.findViewById(R.id.rlSavedNewsItem);
        }

        public void bind(SavedNews savedNew) {
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            tvDescription.setText(savedNew.getDescription());
            tvTitle.setText(savedNew.getTitle());
            Glide.with(context)
                    .load(savedNew.getImgURL())
                    .into(ivImage);

        }
    }
}
