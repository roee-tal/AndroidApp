package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.auth.User;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StartActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button create;
    private Button login;

    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        create = findViewById(R.id.create);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        googleBtn = findViewById(R.id.faceb);
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

//        Map<String,Object> us_info = new HashMap<>();
//        us_info.put("name", "roee");
//        us_info.put("fam", "tal");
//        FirebaseDatabase.getInstance().getReference().child("Users").updateChildren(us_info);
//        Map<String,Object> us_info2 = new HashMap<>();
//        us_info.put("name", "sivan");
//        us_info.put("fam", "cohen");
//        FirebaseDatabase.getInstance().getReference().child("First7").updateChildren(us_info2);


        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String pas_email = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(pas_email)){
                    Toast.makeText(StartActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                    loginu(txt_email , pas_email);
                }
            }
        });

//        fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                LoginManager.getInstance().logInWithReadPermissions(StartActivity.this, Arrays.asList("public_profile"));
//            }
//        });
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data); if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseauthwithgoogle(account);

            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseauthwithgoogle(GoogleSignInAccount account) {
        AuthCredential cred = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(cred).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = auth.getCurrentUser();
                String uid = user.getUid();
                if (authResult.getAdditionalUserInfo().isNewUser()) {
                    String e_mail = user.getEmail();
                    DocumentReference df = fstore.collection("Users").document(user.getUid());
                    Map<String,Object> us_info = new HashMap<>();
                    us_info.put("ID", uid);
                    us_info.put("Email", e_mail);
                    us_info.put("isUser", "1");
                    us_info.put("LettersNum", "0");
                    df.set(us_info);
                    FirebaseDatabase.getInstance().getReference().child("Users").push().updateChildren(us_info);
                    Toast.makeText(getApplicationContext(), "account created", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Exist", Toast.LENGTH_SHORT).show();
                }
                checkUserAccessLevel(uid);
            }
        });
    }


    void navigateToSecondActivity(){
        finish();
        Intent intent = new Intent(StartActivity.this,SecActivity.class);
        startActivity(intent);
    }


    private void loginu(String txt_email, String pas_email) {
        auth.signInWithEmailAndPassword(txt_email, pas_email).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    assert user != null;
                    if(user.isEmailVerified()) {
                        Toast.makeText(StartActivity.this, "success", Toast.LENGTH_LONG).show();
                        checkUserAccessLevel(user.getUid());
                        finish();
                    }
                    else{
                        Toast.makeText(StartActivity.this, "Please verify your email", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void checkUserAccessLevel(String uid) {
        DocumentReference df =  fstore.collection("Users").document(uid);
        df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        if (document.getString("isUser").equals("1")) {
                            startActivity(new Intent(StartActivity.this, MainActivity.class));
                            finish();
                        }
                        else{
                            startActivity(new Intent(StartActivity.this, AdminActivity.class));
                            finish();
                        }
                    }
                }
            }
        });
//        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Log.d("TAG", "onSuccess" + documentSnapshot.getData());
//                if(documentSnapshot.getString("isAdmin")!=null){
//                    startActivity(new Intent(StartActivity.this, AdminActivity.class));
//                    finish();
//                }
//                if(documentSnapshot.getString("isUser")!=null){
//                    startActivity(new Intent(StartActivity.this, SecActivity.class));
//                    finish();
//                }
//            }
//        });
    }
}

