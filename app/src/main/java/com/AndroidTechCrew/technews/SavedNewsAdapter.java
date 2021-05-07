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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
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
                Toast.makeText(context, savedNew.getTitle(), Toast.LENGTH_LONG).show();
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
        private ImageView heartOn;
        private ImageView heartOff;
        private TextView numOfLikes;
        private RecyclerView recyclerView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivImage = itemView.findViewById(R.id.ivImage);
            rvSavedNews = itemView.findViewById(R.id.rlSavedNewsItem);
            ibDelete = itemView.findViewById(R.id.ibDeleteSavedNews);
            commentButton = itemView.findViewById(R.id.btnSavedNewsComment);
            heartOff = itemView.findViewById(R.id.heartOff);
            heartOn = itemView.findViewById(R.id.heartOn);
            numOfLikes = itemView.findViewById(R.id.tvNumberOfLikes);
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
                    i.putExtra("description", savedNew.getDescription());
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
                    //switching out hearts
                    heartOff.setVisibility(View.INVISIBLE);
                    heartOn.setVisibility(View.VISIBLE);
                    //saving the likes to a user
                    DocumentReference likeRef = db.collection("users/" + currentUser.getUid() + "/likes").document(savedNew.getTitle());
                    HashMap<String, String> likeData = new HashMap<>();
                    likeData.put(savedNew.getTitle(), "true");
                    likeRef.set(likeData);
                    //saving the number of likes per article
                    DocumentReference articleLikesRef = db.collection("articles").document(savedNew.getTitle()).collection("likes").document("numOfLikes");
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
                    db.collection("users/" + currentUser.getUid() + "/likes").document(savedNew.getTitle()).delete();

                    //deleting likes from articles
                    DocumentReference articleLikesRef = db.collection("articles").document(savedNew.getTitle()).collection("likes").document("numOfLikes");
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

            //this checks if the users has already liked it
            db.collection("users/" + currentUser.getUid() + "/likes").document(savedNew.getTitle()).get()
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
            db.document("articles/" + savedNew.getTitle() + "/likes/numOfLikes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
