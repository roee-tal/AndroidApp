package com.example.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Objects;
import android.graphics.Bitmap;
import android.widget.TextView;


class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images and site
    ArrayList<Bitmap>  images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;


    // Viewpager Constructor
    public ViewPagerAdapter(Context context, ArrayList<Bitmap> images) {
        this.context = context;
        this.images = images;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        Log.d("Adapter_out", ""+images.size());

        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView;
//        if (position < images.size()) {
        // inflating the item.xml
        itemView = mLayoutInflater.inflate(R.layout.image_detail, container, false);
        // referencing the image view from image_detail.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        Log.d("Adapter_out", "" + images.size());
        Log.d("Adapter_out", "" + images.get(position));

        // setting the image in the imageView
        imageView.setImageBitmap(images.get(position));
//        }
//        else {
//            // inflating the item.xml
//            itemView = mLayoutInflater.inflate(R.layout.activity_detail, container, false);
//            Log.d("Adapter_out", "" + siteSingalAtList.size());
//            Log.d("Adapter_out", "" + siteSingalAtList.get(position).getName());
//            setValues(itemView, position);
//
//        }
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }

}