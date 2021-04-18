package com.AndroidTechCrew.technews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.AndroidTechCrew.technews.fragments.HomeFeedFragment;
import com.AndroidTechCrew.technews.fragments.ProfileFragment;
import com.AndroidTechCrew.technews.fragments.SavedNewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch(item.getItemId()){
                    case R.id.newsFeed_action:
                        fragment = new HomeFeedFragment();
                        break;
                    case R.id.profile_action:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.savedNews_action:
                        fragment = new SavedNewsFragment();
                        break;
                    default:
                        fragment = new HomeFeedFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.newsFeed_action);
    }
}