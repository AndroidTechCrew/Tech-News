package com.AndroidTechCrew.technews.fragments;

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
import android.widget.TextView;

import com.AndroidTechCrew.technews.R;
import com.AndroidTechCrew.technews.SavedNews;
import com.AndroidTechCrew.technews.SavedNewsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class SavedNewsFragment extends Fragment {
    public static final String TAG = "SavedNewsFragment";
    private FirebaseAuth mAuth;
    private RecyclerView rvSavedNews;
    private SavedNewsAdapter adapter;
    private List<SavedNews> allSavedNews;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        rvSavedNews = view.findViewById(R.id.rvSavedNews);

        allSavedNews = new ArrayList<>();
//        adapter = new SavedNewsAdapter(getContext(), allSavedNews);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

//        rvSavedNews.setAdapter(adapter);
//
//        rvSavedNews.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseFirestore ff = FirebaseFirestore.getInstance();
        ff.collection("users/" + currentUser.getUid() + "/savedNews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String title = document.getData().get("title").toString();
                                String img = document.getData().get("imageUrl").toString();
                                String des = document.getData().get("des").toString();
                                String link = document.getData().get("newurl").toString();
                                allSavedNews.add(new SavedNews(title,img,des,link));

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        initializeRV(view);
                    }
                });


    }
    private void initializeRV(View view){
        RecyclerView recyclerView = view.findViewById(R.id.rvSavedNews);
        SavedNewsAdapter adapter = new SavedNewsAdapter(getContext(),allSavedNews);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}