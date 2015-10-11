package com.example.spvenka.instagram;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    String url = "https://api.instagram.com/v1/media/popular?client_id=0b92f83a8a8743b899b5079a18326c6f";
    public ArrayList<InstagramPhoto> photos = null;
    InstagramPhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        photos = new ArrayList<>();
        adapter = new InstagramPhotosAdapter(this, photos);
        ListView listView = (ListView) findViewById(R.id.lvPhotos);
        listView.setAdapter(adapter);
        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.i("DEBUG", response.toString());
                JSONArray photosArray = null;
                try {
                    photosArray = response.getJSONArray("data");
                    for (int i = 0; i < photosArray.length(); i++) {
                        JSONObject jsonObject = photosArray.getJSONObject(i);
                        InstagramPhoto instagramPhoto = new InstagramPhoto();
                        instagramPhoto.userName = jsonObject.getJSONObject("user").getString("username");
                        instagramPhoto.caption = jsonObject.getJSONObject("caption").getString("text");
                        instagramPhoto.imageUrl = jsonObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        instagramPhoto.imageHeight = jsonObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        instagramPhoto.imageLikes = jsonObject.getJSONObject("likes").getInt("count");
                        instagramPhoto.userPhoto = jsonObject.getJSONObject("user").getString("profile_picture");
                        photos.add(instagramPhoto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
