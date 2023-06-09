package com.wildpeppers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.wildpeppers.R;
import com.wildpeppers.adapters.CategorizedWallpapersAdapter;
import com.wildpeppers.models.CategorizedWallpapersModel;

import java.util.ArrayList;
import java.util.List;

public class CategorizedWallpapersActivity extends AppCompatActivity {

    List<CategorizedWallpapersModel> wallpaperList;
    List<CategorizedWallpapersModel> favList;
    RecyclerView recyclerView;
    CategorizedWallpapersAdapter adapter;

    DatabaseReference dbWallpapers, dbFavs;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorized_wallpapers);

        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle(category); //asta era nebunia care punea titlul categoriei in toolbar
        setSupportActionBar(toolbar);

        favList = new ArrayList<>();
        wallpaperList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategorizedWallpapersAdapter(this, wallpaperList);

        recyclerView.setAdapter(adapter);

        progressBar = findViewById(R.id.progressbar);
        dbWallpapers = FirebaseDatabase.getInstance().getReference("categories_with_images")
                .child(category);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dbFavs = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
                    .child(category);
            fetchFavWallpapers(category);
        } else {
            fetchWallpapers(category);
        }
    }

    private void fetchFavWallpapers(final String category) {
        progressBar.setVisibility(View.VISIBLE);
        dbFavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);

                        CategorizedWallpapersModel w = new CategorizedWallpapersModel(id, title, desc, url, category);
                        favList.add(w);
                    }
                }
                fetchWallpapers(category);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchWallpapers(final String category) {
        progressBar.setVisibility(View.VISIBLE);
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);

                        CategorizedWallpapersModel w = new CategorizedWallpapersModel(id, title, desc, url, category);

                        if (isFavourite(w)) {
                            w.isFavourite = true;
                        }

                        wallpaperList.add(w);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean isFavourite(CategorizedWallpapersModel w) {
        for (CategorizedWallpapersModel f : favList) {
            if (f.id.equals(w.id)) {
                return true;
            }
        }
        return false;
    }
}
