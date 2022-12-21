package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DetailMessageActivity extends AppCompatActivity {
    private Button delete;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_message);
        getSelectedShape();
        delete = findViewById(R.id.del);
        fstore = FirebaseFirestore.getInstance();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent previousIntent = getIntent();
                String parsedStringMail = previousIntent.getStringExtra("mail");
//                deleteAccount(parsedStringMail);
            }
        });

    }

    private void getSelectedShape(){
//        String parsedStringID = previousIntent.getStringExtra("messsage");
//        Log.d("updateSelectedName", "id="+parsedStringID);
//        getParsedShape(parsedStringID);
        Intent intent = getIntent();
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        String mail = intent.getStringExtra("mail");
        TextView textView = (TextView) findViewById(R.id.mes);
        TextView mailview = (TextView) findViewById(R.id.email_cont);
        textView.setText(text);
        mailview.setText(mail);
        Log.d("updateSelectedName", "id="+text);
    }

//    private void deleteAccount(String parsedMail){
//        fstore.collection("Messages").whereEqualTo("Email",parsedMail)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot document : task.getResult()){
//                                Log.d("TAG", document.getId() + " => " + document.getData());
//                                String mess = document.getString("Message");
//                                Message m = new Message(mess);
//                                Log.d("TAG", m.getText());
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }

}