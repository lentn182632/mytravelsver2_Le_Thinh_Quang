package com.example.mytravelsver2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {
    private Button btn_Search, btn_Gallery, btn_Profile, btn_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btn_Search = findViewById(R.id.btn_Search);
        btn_Gallery = findViewById(R.id.btn_Gallery);
        btn_Profile = findViewById(R.id.btn_Profile);
        btn_Back = findViewById(R.id.btn_Back);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingNetwork.isNetworkAvailable(SplashActivity.this)){
                    startActivity(new Intent(SplashActivity.this, SearchActivity.class));
                    //finish();
                }else {
                    Toast.makeText(SplashActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, GalleryActivity.class));
                finish();
            }
        });

        btn_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, ProfileActivity.class));
                //finish();
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                //finish();
            }
        });
    }
}