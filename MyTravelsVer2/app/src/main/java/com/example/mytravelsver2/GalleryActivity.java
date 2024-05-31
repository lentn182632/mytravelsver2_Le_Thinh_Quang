package com.example.mytravelsver2;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    GalleryFragment galleryFragment;
    Button btn_galleryBack;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryFragment = new GalleryFragment();

        btn_galleryBack = findViewById(R.id.btn_galleryBack);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, galleryFragment)
                .commit();

        btn_galleryBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, SplashActivity.class));
                finish();
            }
        });
    }
}