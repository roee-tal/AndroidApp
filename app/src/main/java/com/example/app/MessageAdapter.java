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

public class MessageAdapter extends ArrayAdapter<Message> {
    private StorageReference myStorage;

    public MessageAdapter(Context context, int resource, List<Message> shapeList) {
        super(context, resource, shapeList);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message site = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape_cell, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.siteName);
//        ImageView iv = (ImageView) convertView.findViewById(R.id.mainImage);

        tv.setText(site.getText());
//        iv.setImageResource(site.getImage());
//        this.setimageView(site.getUsername(), convertView);

        return convertView;
    }
}
