package com.AndroidTechCrew.technews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidTechCrew.technews.fragments.Comment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private ImageView articleImage;
    private TextView articleTitle;
    private TextView areThereComments;

    private List<Comment> comments;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        db = FirebaseFirestore.getInstance();

        comments = new ArrayList<>();

        areThereComments = findViewById(R.id.tvArethereComments);
        articleImage = findViewById(R.id.ivCommentImage);
        articleTitle = findViewById(R.id.tvCommentTitle);

        String title = getIntent().getStringExtra("title");
        articleTitle.setText(title);

        String imageURL = getIntent().getStringExtra("image");
        Glide.with(this).load(imageURL).into(articleImage);

        db.collection("articles/" + title + "/comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String username = document.getData().get("username").toString();
                                String comment = document.getData().get("comment").toString();
                                comments.add(new Comment("https://dummyimage.com/300.png/09f/fff",username,comment));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                        }
                        if(comments.size() == 0){
                            areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                        }
                        else{
                            initializeRV();
                        }
                    }
                });


    }

    private void initializeRV(){
        RecyclerView recyclerView = findViewById(R.id.commentsRV);
        CommentAdapter adapter = new CommentAdapter(this,comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}