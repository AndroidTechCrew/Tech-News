package com.AndroidTechCrew.technews.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidTechCrew.technews.LoginActivity;
import com.AndroidTechCrew.technews.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private Button buttonLogout;
    private FirebaseAuth mAuth;
    private TextView username;
    private ImageView profileImage;
    private String basicPriofilePic = "https://cdn.business2community.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640.png";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


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
        profileImage = view.findViewById(R.id.ivProfileImage);
        Glide.with(getContext()).load(basicPriofilePic).override(200,250).into(profileImage);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    username.setText(document.getData().get("email").toString().substring(0,document.getData().get("email").toString().indexOf("@")));
                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
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

    }

    private void goToLoginActivity() {
        Intent i = new Intent(getContext(), LoginActivity.class);
        getContext().startActivity(i);
    }


}