package com.example.mytravelsver2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    private Button btn_ChangePassword, btn_Signout, btn_Back;
    private FirebaseAuth fba_FirebaseAuth;
    private String lw_Email;
    private SharedPreferences sharedPreferences;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_ChangePassword = findViewById(R.id.btn_ChangePassword);
        btn_Signout = findViewById(R.id.btn_Signout);
        btn_Back = findViewById(R.id.btn_Back);
        sharedPreferences = getApplication().getSharedPreferences("login",MODE_PRIVATE);

        lw_Email = sharedPreferences.getString("m_Email","");
        btn_ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckingNetwork.isNetworkAvailable(ProfileActivity.this)){
                    fba_FirebaseAuth = FirebaseAuth.getInstance();
                    fba_FirebaseAuth.sendPasswordResetEmail(lw_Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(ProfileActivity.this, "Can not send link to your email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(ProfileActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SplashActivity.class));
                finish();
            }
        });
    }
}