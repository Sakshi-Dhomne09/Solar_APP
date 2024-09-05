package com.solarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Picasso;

public class ImageZoomActivity extends AppCompatActivity {


    String url;
    ZoomageView bigImageView;
    String user_id= "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);


        user_id = getIntent().getStringExtra("user_id");

        url = getIntent().getStringExtra("image");

        bigImageView = findViewById(R.id.myZoomageView);

        Picasso.get().load(url).into(bigImageView);


    }
}