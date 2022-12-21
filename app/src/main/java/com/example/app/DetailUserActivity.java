package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailUserActivity extends AppCompatActivity
{
    // creating object of ViewPager
    ViewPager mViewPager;

    // images array
    final ArrayList<User> siteSingalAtList = new ArrayList<User>();
    ViewPagerAdapter mViewPagerAdapter;
    private Button delete;
    private Button contact;
    private TextView email;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;
    String selectedShapeName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);
        delete = findViewById(R.id.del);
        contact = findViewById(R.id.act);
        email = findViewById(R.id.Email);
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getSelectedShape();
//        deleteUser();


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToPass = email.getText().toString();
                // start the SecondActivity
                Intent intent = new Intent(new Intent(DetailUserActivity.this, ContactAdminActivity.class));
                intent.putExtra(Intent.EXTRA_TEXT, textToPass);
                startActivity(intent);
//                startActivity(new Intent(DetailUserActivity.this, ContactAdminActivity.class));
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previousIntent = getIntent();
                String parsedStringID = previousIntent.getStringExtra("id");
                deleteAccount(parsedStringID);
            }
        });

//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DetailUserActivity.this, ContactActivity.class));
//                finish();
//            }
//        });
    }

    private void getSelectedShape()
    {
        Intent previousIntent = getIntent();
        String parsedStringID = previousIntent.getStringExtra("id");
        selectedShapeName = previousIntent.getStringExtra("name");
        Log.d("updateSelectedName", "id="+parsedStringID);
        getParsedShape(parsedStringID);
    }


    private void deleteAccount(String parsedID){
        fstore.collection("Users").document(parsedID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String email = doc.getString("Email");
                    String id = doc.getString("ID");
                    String user = doc.getString("isUser");
                    User blockUser = new User(email,id,user);
                    Log.d("TAG", blockUser.getId());
                    fstore.collection("BlockedUsers").document(parsedID).set(blockUser);
                    fstore.collection("Users").document(parsedID).delete();
                    Toast.makeText(DetailUserActivity.this, "removed", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DetailUserActivity.this, "dont exist", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void getParsedShape(String parsedID)
    {
        fstore.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        siteSingalAtList.clear();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String email = document.getString("Email");
                                String id = document.getString("ID");
                                String user = document.getString("isUser");
                                User u = new User(email,id,user);
                                if(parsedID.equals(id)){
                                siteSingalAtList.add(u);
                                this.setValues(u);
                                Log.d("getParsedShape", "siteSingalAtList.size="+siteSingalAtList.size());
                                assert u != null;}
                            }
//                            mViewPagerAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }


            private void setValues(User s)
            {
                TextView siteName = (TextView) findViewById(R.id.Email);
                siteName.setText(s.getEmail());
            }});
        //--------------------------------------------
    }
    }