package com.example.spvenka.instagram;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter {

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InstagramPhoto instagramPhoto = (InstagramPhoto) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
        }

        TextView imageCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        TextView userName = (TextView) convertView.findViewById(R.id.userName);
        ImageView instaPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        ImageView userPhoto = (ImageView) convertView.findViewById(R.id.userPhoto);
        imageCaption.setText(instagramPhoto.caption);
        userName.setText(instagramPhoto.userName);
        instaPhoto.setImageResource(0);

        Picasso.with(getContext()).load(instagramPhoto.imageUrl).into(instaPhoto);

        Picasso.with(getContext()).load(instagramPhoto.userPhoto).transform(new CircleTransform()).into(userPhoto);

        return convertView;
    }
}
