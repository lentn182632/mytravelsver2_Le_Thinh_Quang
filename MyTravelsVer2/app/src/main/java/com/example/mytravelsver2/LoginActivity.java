package com.example.mytravelsver2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth fba_FirebaseAuth;
    private Button btn_Login, btn_Signup;
    CheckBox cbx_CheckBox;
    SharedPreferences spf_SharedPreferences;
    TextView txt_ForgotPassword;
    EditText edt_Email, edt_Password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_Login = findViewById(R.id.btn_Login);
        btn_Signup = findViewById(R.id.btn_Signup);
        edt_Email = findViewById(R.id.edt_Email);
        edt_Password = findViewById(R.id.edt_Password);
        cbx_CheckBox = findViewById(R.id.cbx_KeepLogin);
        txt_ForgotPassword = findViewById(R.id.txt_ForgotPassword);
        spf_SharedPreferences = getApplication().getSharedPreferences("login",MODE_PRIVATE);

        edt_Email.setText(spf_SharedPreferences.getString("m_Email",""));
        edt_Password.setText(spf_SharedPreferences.getString("m_Password",""));
        cbx_CheckBox.setChecked(spf_SharedPreferences.getBoolean("m_Checked",false));

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
        btn_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSignUp();
            }
        });

        txt_ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {onClickForgot();}
        });
    }

    private void onClickSignUp() {
        String lw_Email = edt_Email.getText().toString().trim();
        String lw_Password = edt_Password.getText().toString().trim();

        if (CheckingNetwork.isNetworkAvailable(this)){
            if (lw_Email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();

            } else if (lw_Password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();

            } else {
                fba_FirebaseAuth = FirebaseAuth.getInstance();
                fba_FirebaseAuth.createUserWithEmailAndPassword(lw_Email, lw_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FuntionCheckBox(cbx_CheckBox, lw_Email, lw_Password);
                           // SharedPreferences.Editor editor = spf_SharedPreferences.edit();
                           // editor.putString("m_Email",lw_Email);
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickLogin() {
        String lw_Email = edt_Email.getText().toString().trim();
        String lw_Password = edt_Password.getText().toString().trim();
        if (CheckingNetwork.isNetworkAvailable(this)){
            if (lw_Email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();

            } else if (lw_Password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Password is required", Toast.LENGTH_SHORT).show();

            } else {
                fba_FirebaseAuth = FirebaseAuth.getInstance();
                fba_FirebaseAuth.signInWithEmailAndPassword(lw_Email, lw_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FuntionCheckBox(cbx_CheckBox, lw_Email, lw_Password);
                            Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email and password are incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void FuntionCheckBox(CheckBox cbx_CheckBox, String str_Email, String str_Password){
        if (cbx_CheckBox.isChecked()){
            SharedPreferences.Editor editor = spf_SharedPreferences.edit();
            editor.putString("m_Email",str_Email);
            editor.putString("m_Password", str_Password);
            editor.putBoolean("m_Checked",true);
            editor.commit();
        }else {
            SharedPreferences.Editor editor = spf_SharedPreferences.edit();
            editor.remove("m_Email");
            editor.remove("m_Password");
            editor.remove("m_Checked");
            editor.commit();
        }
    }

    private void onClickForgot() {
        String lw_Email = edt_Email.getText().toString().trim();
        if (CheckingNetwork.isNetworkAvailable(this)){
            if (lw_Email.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email is required", Toast.LENGTH_SHORT).show();

            } else {
                fba_FirebaseAuth = FirebaseAuth.getInstance();
                fba_FirebaseAuth.sendPasswordResetEmail(lw_Email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else {
            Toast.makeText(this, "No internet", Toast.LENGTH_SHORT).show();
        }
    }
}