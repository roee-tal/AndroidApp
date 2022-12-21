////package com.example.app;
////
////import android.content.Context;
////import android.graphics.Bitmap;
////import android.graphics.BitmapFactory;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ArrayAdapter;
////import android.widget.ImageView;
////import android.widget.TextView;
////import android.widget.Toast;
////
////import androidx.annotation.NonNull;
////
////import com.google.android.gms.tasks.OnFailureListener;
////import com.google.android.gms.tasks.OnSuccessListener;
////import com.google.firebase.storage.FileDownloadTask;
////import com.google.firebase.storage.FirebaseStorage;
////import com.google.firebase.storage.StorageReference;
////
////import java.io.File;
////import java.io.IOException;
////import java.util.List;
////
////public class ShapeAdapter extends ArrayAdapter<Site>
////{
////    private StorageReference myStorage;
////
////    public ShapeAdapter(Context context, int resource, List<Site> shapeList)
////    {
////        super(context,resource,shapeList);
////    }
////
////
////    @Override
////    public View getView(int position, View convertView, ViewGroup parent)
////    {
////        Site site = getItem(position);
////
////        if(convertView == null)
////        {
////            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape_cell, parent, false);
////        }
////        TextView tv = (TextView) convertView.findViewById(R.id.shapeName);
//////        ImageView iv = (ImageView) convertView.findViewById(R.id.mainImage);
////
////        tv.setText(site.getId() + " - " + site.getName());
//////        iv.setImageResource(site.getImage());
////        this.setimageView(site.getImage(), convertView);
////
////
////
////        return convertView;
////    }
////
////    private void setimageView(String pictureName, View convertView) {
////        pictureName = "shvil.jpg";
////        myStorage = FirebaseStorage.getInstance().getReference().child("picture/"+pictureName);
////        try {
////            final File localTempFile = File.createTempFile("shvil", "jpg");
////            myStorage.getFile(localTempFile)
////                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
////                        @Override
////                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
////                            Toast.makeText(getContext(), "Picture Retrieved",Toast.LENGTH_SHORT).show();
////                            Bitmap bitmap = BitmapFactory.decodeFile(localTempFile.getAbsolutePath());
////                            ((ImageView) convertView.findViewById(R.id.mainImage)).setImageBitmap(bitmap);
////                        }
////                    }).addOnFailureListener(new OnFailureListener() {
////                        @Override
////                        public void onFailure(@NonNull Exception e) {
////                            Toast.makeText(getContext(), "Error During Picture Retrieved",Toast.LENGTH_SHORT).show();
////                        }
////                    });
////        }catch (IOException e){
////            e.printStackTrace();
////        }
////    }
////}
//package com.example.app;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.storage.FileDownloadTask;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//public class ShapeAdapter extends ArrayAdapter<Site>
//{
//    private StorageReference myStorage;
//
//    public ShapeAdapter(Context context, int resource, List<Site> shapeList)
//    {
//        super(context,resource,shapeList);
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        Site site = getItem(position);
//
//        if(convertView == null)
//        {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape_cell, parent, false);
//        }
//        TextView name = (TextView) convertView.findViewById(R.id.siteName);
//        TextView rate = (TextView) convertView.findViewById(R.id.siteRate);
////        ImageView iv = (ImageView) convertView.findViewById(R.id.mainImage);
//
//        name.setText(site.getName());
//        rate.setText("\n" + site.getRate() + "/10");
////        iv.setImageResource(site.getImage());
//        this.setimageView(site.getImage(), convertView);
//
//
//
//        return convertView;
//    }
//
//    private void setimageView(String pictureName, View convertView) {
//        pictureName = "shvil.jpg";
//        myStorage = FirebaseStorage.getInstance().getReference().child("picture/"+pictureName);
//        try {
//            final File localTempFile = File.createTempFile("shvil", "jpg");
//            myStorage.getFile(localTempFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(getContext(), "Picture Retrieved",Toast.LENGTH_SHORT).show();
//                            Bitmap bitmap = BitmapFactory.decodeFile(localTempFile.getAbsolutePath());
//                            ((ImageView) convertView.findViewById(R.id.mainImage)).setImageBitmap(bitmap);
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "Error During Picture Retrieved",Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//}
package com.example.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ShapeAdapter extends ArrayAdapter<Site>
{
    private StorageReference myStorage;
    private List<Site> shapeList;

    public ShapeAdapter(Context context, int resource, List<Site> shapeList)
    {
        super(context,resource,shapeList);
        this.shapeList = shapeList;
//        this.sortTypeList = sortTypeList;
    }

    @Override
    public void sort(@NonNull Comparator<? super Site> comparator) {
        //TOdo: ned to add arraylist of sottype
        super.sort(comparator);
    }

    @Override
    public int getCount() {
        Log.d("getCount_out", "shapeList.size()="+shapeList.size() );
        return shapeList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
//        if (position < shapeList.size()) {
        Site site = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape_cell, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.siteName);
        TextView rate = (TextView) convertView.findViewById(R.id.siteRate);
//        ImageView iv = (ImageView) convertView.findViewById(R.id.mainImage);

        name.setText(site.getName());
        rate.setText("\n" + site.getRate() + "/10");
//        iv.setImageResource(site.getImage());
        this.setimageView(site.getImage(), convertView);
//        }
//        else /*if (position < sortTypeList.size())*/ {
//            Log.d("getView", "sort="+sortTypeList.get(position));
//            sort(rateSort);
//        }

        return convertView;
    }

    private void setimageView(String pictureName, View convertView) {
//        pictureName = "Jerusalem Forest/image1.jfif";
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