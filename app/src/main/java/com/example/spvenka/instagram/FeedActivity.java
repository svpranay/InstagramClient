package com.example.spvenka.instagram;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private String url = "https://api.instagram.com/v1/media/popular?client_id=0b92f83a8a8743b899b5079a18326c6f";
    private ArrayList<InstagramPhoto> photos = null;
    private InstagramPhotosAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        photos = new ArrayList<>();
        adapter = new InstagramPhotosAdapter(this, photos);
        ListView listView = (ListView) findViewById(R.id.lvPhotos);
        listView.setAdapter(adapter);
        fetchPopularPhotos(0);
    }

    public void fetchPopularPhotos(int page) {
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
                adapter.clear();
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
                adapter.addAll(photos);
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
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
