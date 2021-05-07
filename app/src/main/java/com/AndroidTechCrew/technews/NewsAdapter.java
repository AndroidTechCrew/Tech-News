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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.AndroidTechCrew.technews.fragments.HomeFeedFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
                Toast.makeText(context, article.getTitle(), Toast.LENGTH_LONG).show();
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
        Button btnComment;
        ImageView heartOff;
        ImageView heartOn;
        TextView numOfLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            rlBox = itemView.findViewById(R.id.rlBox);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnComment = itemView.findViewById(R.id.btnComment);
            heartOff = itemView.findViewById(R.id.ivNewsHeartOff);
            heartOn = itemView.findViewById(R.id.ivNewsHeartOn);
            numOfLikes = itemView.findViewById(R.id.tvNewsNumberOfLikes);
        }

        public void bind(News news){
            tvTitle.setText(news.getTitle());
            tvDesc.setText(news.getDescription());
            Glide.with(context).load(news.getImageURL()).error(R.drawable.dummyimage).into(ivImage);
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"Test button");
                    //"users/" + user.getUid() + "/savedNews"
                    db.collection("users/" + user.getUid() + "/savedNews").document(news.getTitle())
                            .set(news)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
            });

            btnComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CommentActivity.class);
                    i.putExtra("image", news.imageURL);
                    i.putExtra("title", news.getTitle());
                    i.putExtra("articleLink", news.getArticleLink());
                    i.putExtra("description",news.getDescription());
                    context.startActivity(i);
                }
            });

            heartOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //changing the number of likes TextView
                    String currentNumOflikes = numOfLikes.getText().toString();
                    String ans = String.valueOf(Integer.parseInt(currentNumOflikes) + 1);
                    numOfLikes.setText(ans);

                    heartOff.setVisibility(View.INVISIBLE);
                    heartOn.setVisibility(View.VISIBLE);
                    DocumentReference likeRef = db.collection("users/" + user.getUid() + "/likes").document(news.getTitle());
                    HashMap<String, String> likeData = new HashMap<>();
                    likeData.put(news.getTitle(), "true");
                    likeRef.set(likeData);
                    //saving the number of likes to an article
                    DocumentReference articleLikesRef = db.collection("articles").document(news.getTitle()).collection("likes").document("numOfLikes");
                    articleLikesRef.update("likes", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Likes for articles updated!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                    HashMap<String,Integer> d = new HashMap<>();
                                    d.put("likes",1);
                                    articleLikesRef.set(d);
                                }
                            });
                }
            });

            heartOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String currentNumOflikes = numOfLikes.getText().toString();
                    String ans = String.valueOf(Integer.parseInt(currentNumOflikes) - 1);
                    numOfLikes.setText(ans);

                    heartOff.setVisibility(View.VISIBLE);
                    heartOn.setVisibility(View.INVISIBLE);
                    db.collection("users/" + user.getUid() + "/likes").document(news.getTitle()).delete();
                    //deleting likes from articles
                    DocumentReference articleLikesRef = db.collection("articles").document(news.getTitle()).collection("likes").document("numOfLikes");
                    articleLikesRef.update("likes", FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Likes for articles updated!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
            });

            db.collection("users/" + user.getUid() + "/likes").document(news.getTitle()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                    heartOff.setVisibility(View.INVISIBLE);
                                    heartOn.setVisibility(View.VISIBLE);
                                } else {
                                    Log.d(TAG, "Like doesn't exist");
                                }
                            } else {
                                Log.d(TAG, "failed to get Like ", task.getException());
                            }
                        }
                    });


            //Checks to see how many likes there are
            db.document("articles/" + news.getTitle() + "/likes/numOfLikes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            Log.d(TAG, "WHAT IS THE DATA GIVEN?: " + document.getData().get("likes").toString());
                            String textOfLikes = document.getData().get("likes").toString();
                            numOfLikes.setText(textOfLikes);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

        }
    }
}
