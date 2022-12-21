package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContactAdminActivity extends AppCompatActivity
{
    // creating object of ViewPager

    // images array
    final ArrayList<User>  siteSingalAtList = new ArrayList<User>();
    public static ArrayList<Message> shapeList = new ArrayList<Message>();
    private ArrayList<String> selectedFilters = new ArrayList<String>();
    private String currentSearchText = "";
    private ListView listView;
    private TextView email;
    private Button contact;
    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_admin);
        email = findViewById(R.id.email_con);
        fstore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        getSelectedShape();
//        setupData(String text);
        setUpList();
        setUpOnclickListener();
        selectedFilters.add("all");
    }

    private void getSelectedShape()
    {
        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);

        // use the text in a TextView
        TextView textView = (TextView) findViewById(R.id.email_con);
        textView.setText(text);
        Log.d("updateSelectedName", "id="+text);
        setupData(text);
    }

    private void setupData(String text)
    {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        FirebaseFirestore fstore;
        fstore = FirebaseFirestore.getInstance();
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference();
        fstore.collection("Messages").whereEqualTo("Email",text)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String mess = document.getString("Message");
                                Message m = new Message(mess);
                                Log.d("TAG", m.getText());
                                shapeList.add(m);
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



    private void setUpList()
    {
        listView = (ListView) findViewById(R.id.shapesListView);

        setAdapter(shapeList);
    }

    private void setUpOnclickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                String s = email.getText().toString();
                Message selectShape = (Message) (listView.getItemAtPosition(position));
                Intent showDetail = new Intent(getApplicationContext(), DetailMessageActivity.class);
                showDetail.putExtra(Intent.EXTRA_TEXT,selectShape.getText());
                showDetail.putExtra("mail",s);
                startActivity(showDetail);
                finish();
            }
        });
    }

    public void messageTapped(View view)
    {
        Collections.sort(shapeList, Message.nameAscending);
        checkForFilter();
//        unSelectAllSortButtons();
//        lookSelected(messages);
    }

    private void checkForFilter()
    {
//        if(selectedFilters.contains("all"))
//        {
//            if(currentSearchText.equals(""))
//            {
                setAdapter(shapeList);
//            }
//            else
//            {
//                ArrayList<Message> filteredShapes = new ArrayList<Message>();
//                for(Message site : shapeList)
//                {
//                    if(site.getText().toLowerCase().contains(currentSearchText))
//                    {
//                        filteredShapes.add(site);
//                    }
//                }
//                setAdapter(filteredShapes);
//            }
//        }
//        else
//        {
//            filterList(null);
//        }
    }

    private void setAdapter(ArrayList<Message> shapeList)
    {
        MessageAdapter adapter = new MessageAdapter(getApplicationContext(), 0, shapeList);
        listView.setAdapter(adapter);
    }
}
