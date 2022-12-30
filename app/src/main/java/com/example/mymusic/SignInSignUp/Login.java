package com.example.mymusic.SignInSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymusic.MainActivity;
import com.example.mymusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public
class Login extends AppCompatActivity {
    EditText mEmail, mPassword;


    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;

    FirebaseAuth fAuth;


    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.passward);
        mLoginBtn = findViewById(R.id.loginbtn);
        mCreateBtn = findViewById(R.id.backbtn);
        progressBar = findViewById(R.id.progressbar);
        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }


        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public
            void onClick(View view) {

                final String email = mEmail.getText().toString().trim();
                String passward = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(passward)) {
                    mPassword.setError("Passward is Required");
                    return;
                }
                if (passward.length() < 6) {
                    mPassword.setError("Password must be >= 6 character ");
                    return;

                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email, passward).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public
                    void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }


                });

            }
        });
    }
}