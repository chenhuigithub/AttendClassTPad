package com.example.attendclasstpad.adapter;

import com.example.attendclasstpad.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    Context mContext;
    int mGalleryItemBackground;
    int[] ivs;

    public ImageAdapter(int[] ivs, Context context) {
        mContext = context;
        this.ivs = ivs;

        TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery1);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.Gallery1_android_galleryItemBackground, 0);
        a.recycle();

    }

    @Override
    public int getCount() {
        return ivs.length;
    }

    @Override
    public Object getItem(int position) {
        return ivs[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        ImageView i = new ImageView(mContext);

        i.setImageResource(ivs[position]);
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setLayoutParams(new Gallery.LayoutParams(300, 400));

        // The preferred Gallery item background
        i.setBackgroundResource(mGalleryItemBackground);

        return i;

    }

}
