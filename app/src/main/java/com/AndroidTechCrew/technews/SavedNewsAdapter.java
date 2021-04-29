package com.AndroidTechCrew.technews;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.SavedNewsFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.ViewHolder> {

    private static final String TAG = "SavedNewsAdapter";
    private Context context;
    private List<SavedNews> savedNews;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;

    public SavedNewsAdapter(Context context, List<SavedNews> savedNews) {
        this.context = context;
        this.savedNews = savedNews;
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
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
                Intent i = new Intent(context, ArticlePageActivity.class);
                i.putExtra("link", savedNews.get(position).getLink());
                context.startActivity(i);
            }
        });

        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users/" + currentUser.getUid() + "/savedNews").document(savedNew.getTitle())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                savedNews.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
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
        private ImageButton ibDelete;
        private RelativeLayout rvSavedNews;
        private Button commentButton;
        private RecyclerView recyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            rvSavedNews = itemView.findViewById(R.id.rlSavedNewsItem);
            ibDelete = itemView.findViewById(R.id.ibDeleteSavedNews);
            commentButton = itemView.findViewById(R.id.btnSavedNewsComment);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.rvSavedNews);
        }

        public void bind(SavedNews savedNew) {
//            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

            tvDescription.setText(savedNew.getDescription());
            tvTitle.setText(savedNew.getTitle());
            Glide.with(context)
                    .load(savedNew.getImgURL())
                    .into(ivImage);
            commentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("image", savedNew.getImgURL());
                    i.putExtra("title", savedNew.getTitle());
                    i.putExtra("articleLink", savedNew.getLink());
                    context.startActivity(i);
                }
            });



        }
    }
}
