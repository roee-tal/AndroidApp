package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText user;
    private Button create;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        create = findViewById(R.id.create);
        password = findViewById(R.id.password);
        user = findViewById(R.id.Username);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String pas_email = password.getText().toString();
                if(TextUtils.isEmpty(txt_email )|| TextUtils.isEmpty(pas_email)){
                    Toast.makeText(LoginActivity.this, "empty!", Toast.LENGTH_LONG).show();
                }
                else if(pas_email.length()<6){
                    Toast.makeText(LoginActivity.this, "less than 6 chars", Toast.LENGTH_LONG).show();
                }
                else{
                    regisuser(txt_email,pas_email);
                }
            }
        });
    }

    private void regisuser(String txt_email, String pas_email) {
        auth.createUserWithEmailAndPassword(txt_email, pas_email).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser fu = auth.getCurrentUser();
                    fu.sendEmailVerification();
                    DocumentReference df = fstore.collection("Users").document(fu.getUid());
                    Map<String,Object> us_info = new HashMap<>();
                    us_info.put("ID", fu.getUid());
                    us_info.put("Email", email.getText().toString());
                    us_info.put("isUser", "1");
                    FirebaseDatabase.getInstance().getReference().child("Users").push().updateChildren(us_info);
                    df.set(us_info);
                    Toast.makeText(LoginActivity.this, "success, verify email", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

