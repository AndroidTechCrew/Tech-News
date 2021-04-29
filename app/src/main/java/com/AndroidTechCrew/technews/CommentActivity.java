package com.AndroidTechCrew.technews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.AndroidTechCrew.technews.fragments.Comment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivity";
    private ImageView articleImage;
    private TextView articleTitle;
    private TextView areThereComments;

    private Button btnComment;
    private EditText etComment;

    private List<Comment> comments;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        comments = new ArrayList<>();

        btnComment = findViewById(R.id.btnActionMakeComment);
        etComment = findViewById(R.id.etMakeAComment);

        areThereComments = findViewById(R.id.tvArethereComments);
        articleImage = findViewById(R.id.ivCommentImage);
        articleTitle = findViewById(R.id.tvCommentTitle);

        String title = getIntent().getStringExtra("title");
        articleTitle.setText(title);

        String imageURL = getIntent().getStringExtra("image");
        Glide.with(this).load(imageURL).into(articleImage);

        String articleLink = getIntent().getStringExtra("articleLink");

        db.collection("articles/" + title + "/comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Log.d(TAG, "ITS EMPTY ");
                                areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String username = document.getData().get("username").toString();
                                String comment = document.getData().get("comment").toString();
                                String uid = document.getData().get("uid").toString();
                                makeComment(username,comment,uid);
                                if(document.getData().isEmpty()){
                                    areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                        }
                    }
                });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> commentData = new HashMap<>();
                String comment = etComment.getText().toString();
                commentData.put("comment",comment);
                db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String username = document.getData().get("Username").toString();
                                String uid = document.getData().get("uid").toString();
                                commentData.put("username",username);
                                commentData.put("uid",uid);
                                commentData.put("articleLink",articleLink);
                                addData(commentData);
                                saveDataToUser(commentData);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }

    private void initializeRV(){
        RecyclerView recyclerView = findViewById(R.id.commentsRV);
        CommentAdapter adapter = new CommentAdapter(this,comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
//"articles/" + articleTitle + "/comments"
    private void addData(HashMap<String,String> data){
        db.collection("articles/" + articleTitle.getText().toString() + "/comments")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Comment added!", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void saveDataToUser(HashMap<String,String> data){
        db.collection("users/" + currentUser.getUid() + "/comments")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Comment saved to user", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void makeComment(String username, String comment, String uid){
        StorageReference pathReference = storage.getReference().child("users/" + uid + "/profile.jpg");
        Comment c = new Comment("https://www.worldfuturecouncil.org/wp-content/uploads/2020/02/dummy-profile-pic-300x300-1.png",username,comment);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                c.setProfilePic(uri.toString());
                comments.add(c);
                initializeRV();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "running fail ");
                comments.add(c);
                initializeRV();
                Log.d(TAG, "get failed with " + comments.size());

            }
        });

    }
}