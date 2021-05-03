package com.AndroidTechCrew.technews.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidTechCrew.technews.CommentAdapter;
import com.AndroidTechCrew.technews.EditProfileActivity;
import com.AndroidTechCrew.technews.LoginActivity;
import com.AndroidTechCrew.technews.R;
import com.AndroidTechCrew.technews.userCommentAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private Button buttonLogout;
    private FirebaseAuth mAuth;
    private TextView username;
    private ImageView profileImage;
    private String basicPriofilePic = "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png";
    private Uri profilePicUri;
    private String profilePic;
    private Button btnEditProfile;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private List<UserComments> comments;




    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore db2 = FirebaseFirestore.getInstance();


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        username = view.findViewById(R.id.tvProfileUsername);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        profileImage = view.findViewById(R.id.ivProfilePic);
        storage = FirebaseStorage.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        storageReference = storage.getReference();
        comments = new ArrayList<>();
        RecyclerView rvNews;


//        Glide.with(getContext()).load(basicPriofilePic).override(200, 250).into(profileImage);
//        FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
//                    username.setText(document.getData().get("email").toString().substring(0, document.getData().get("email").toString().indexOf("@")));
                    username.setText(document.getData().get("Username").toString());
//                    profilePic = document.getData().get("profileImage").toString();
//                    makeComment(username, comment, currentUser.getUid(), view, comments);
//                    Glide.with(ProfileFragment.this).load(profilePic).override(350,450).into(profileImage);
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });

//        String title = g.getStringExtra("title");
        db2.collection("users/" + currentUser.getUid() + "/comments")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Log.d(TAG, "ITS EMPTY ");
//                                areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String username = document.getData().get("username").toString();
                                String comment = document.getData().get("comment").toString();
                                String uid = document.getData().get("uid").toString();
                                String articleLink = document.getData().get("articleLink").toString();
                                String image = document.getData().get("imageUrl").toString();
                                String description = document.getData().get("articleDescription").toString();
                                String articleTitle = document.getData().get("articleTitle").toString();
                                makeComment(username,comment,uid,view,comments,articleLink,image,description,articleTitle);

                                if(document.getData().isEmpty()){
//                                    areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                                }
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
//                            areThereComments.setText("Seems like no one said anything. Be the first to share your insight!");
                        }
                    }
                });
        StorageReference profileRef = storageReference.child("users/" + currentUser.getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getContext()).load(uri).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Glide.with(getContext()).load(basicPriofilePic).into(profileImage);
            }
        });

        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                //take user to LoginActivity
                goToLoginActivity();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "Click", Toast.LENGTH_SHORT).show();
                goToProfilePictureActivity();

            }
        });
    }

    private void goToProfilePictureActivity() {
        Intent i = new Intent(getContext(), EditProfileActivity.class);
        getContext().startActivity(i);
    }

    private void goToLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(i);
    }
    private void initializeRV(View view){
        RecyclerView recyclerView = view.findViewById(R.id.userCommentsRV);
        userCommentAdapter adapter = new userCommentAdapter(getContext(),comments);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void makeComment(String username,String comment,String uid, View view, List<UserComments> comments, String articleLink,String image,String description, String articleTitle){
        StorageReference pathReference = storage.getReference().child("users/" + uid + "/profile.jpg");
        UserComments Comment = new UserComments("https://www.worldfuturecouncil.org/wp-content/uploads/2020/02/dummy-profile-pic-300x300-1.png",username,comment,uid,articleLink,image,description,articleTitle);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Comment.setProfilePic(uri.toString());
                comments.add(Comment);
                initializeRV(view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "running fail ");
                comments.add(Comment);
                initializeRV(view);
                Log.d(TAG, "get failed with " + comments.size());

            }
        });
    }

}