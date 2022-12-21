package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {
    private Button send;
    private TextInputLayout mail;
    private TextInputLayout mes;

    private FirebaseAuth auth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        send = findViewById(R.id.send);
//        mail = findViewById(R.id.emailContact);
        mes = findViewById(R.id.Message);
        Editable text = mes.getEditText().getText();
        auth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = auth.getCurrentUser();
                Log.d("TAG", "message: " + text);
                String s = text.toString();
                String e_mail = user.getEmail();
//                ArrayList<String> messagess = new ArrayList<String>();
//                messagess.add(s);
                updateMesSize(user.getUid());
                DocumentReference df = fstore.collection("Messages").document(user.getUid());
                Map<String,Object> us_info = new HashMap<>();
                us_info.put("ID", user.getUid());
                us_info.put("Email", e_mail);
                us_info.put("Message", s);
                df.set(us_info);
                Toast.makeText(getApplicationContext(), "הודעה נשלחה", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ContactActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void updateMesSize(String id){
        fstore.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if(doc.exists()){
                    String email = doc.getString("Email");
                    String id = doc.getString("ID");
                    String user = doc.getString("isUser");
                    String num = doc.getString("LettersNum");
                    int num_cov = Integer.parseInt(num);
                    num_cov++;
                    num = Integer.toString(num_cov);
                    User user1 = new User(email,id,user,num);
                    fstore.collection("Users").document(id).update("LettersNum",num);

                }
            }
        });
    }
}