package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User>
{
    private StorageReference myStorage;

    public UserAdapter(Context context, int resource, List<User> shapeList)
    {
        super(context,resource,shapeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        User site = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape_cell, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.siteName);
//        ImageView iv = (ImageView) convertView.findViewById(R.id.mainImage);

        tv.setText(site.getEmail());
//        iv.setImageResource(site.getImage());
//        this.setimageView(site.getUsername(), convertView);

        return convertView;
    }

    private void setimageView(String pictureName, View convertView) {
        pictureName = "shvil.jpg";
        myStorage = FirebaseStorage.getInstance().getReference().child("picture/"+pictureName);
        try {
            final File localTempFile = File.createTempFile("shvil", "jpg");
            myStorage.getFile(localTempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Picture Retrieved",Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localTempFile.getAbsolutePath());
                            ((ImageView) convertView.findViewById(R.id.mainImage)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error During Picture Retrieved",Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}