package com.example.mymusic.SignInSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    public static final String TAG ="TAG";
    EditText npassward,nphone,nemail,fullname;
    Button register;
    TextView nLoginBtn;
    ProgressBar progressBar;


    String userID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        npassward =findViewById(R.id.passward);
        nphone =findViewById(R.id.phone);
        nemail =findViewById(R.id.email);
        fullname =findViewById(R.id.name);
        register =findViewById(R.id.register);
        nLoginBtn =findViewById(R.id.createtext);
        progressBar =findViewById(R.id.progressbar);



        fAuth =FirebaseAuth.getInstance();
//        fstore =FirebaseFirestore.getInstance();

        nLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }

        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email =nemail.getText().toString().trim();
                String passward =npassward.getText().toString().trim();
                final String Fullname =fullname.getText().toString();
                final String phone =nphone.getText().toString();

                if (TextUtils.isEmpty(email)){
                    nemail.setError("Invalid email");
                    return;
                }
                if (TextUtils.isEmpty(Fullname)){
                    fullname.setError("Invalid name");
                    return;
                }
                if (TextUtils.isEmpty(passward)){
                    npassward.setError("Invalid passward");
                    return;
                }
                if (passward.length()<6){
                    npassward.setError("Passward should be greater than or equal to 6");
                }
                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email,passward).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser fuser =fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(getApplicationContext(), "Register Successful", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"Onfailure :email not sent"+e.getMessage());
                                }
                            });
                            Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                            userID =fAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference =fstore.collection("user").document(userID);
                            Map<String,Object>user = new HashMap<>();
                            user.put("fname",Fullname);
                            user.put("email",email);
                            user.put("phone",phone);
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Log.d(TAG,"OnSuccess : user profile is Created for "+userID);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Log.d(TAG,"Onfailure : "+e.toString());
//                                }
//                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        }
                        else {
                            Toast.makeText(Register.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}