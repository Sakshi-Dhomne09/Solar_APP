package com.solarapp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;


public class PicassoImageLoadingService implements ImageLoadingService {
    public Context context;

    public PicassoImageLoadingService(Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        //Picasso.get().load(url).into(imageView);
         Picasso.get().load(url).fit().into(imageView);


    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        //Picasso.get().load(resource).into(imageView);
        Picasso.get().load(resource).fit().into(imageView);
    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        //Picasso.get().load(url).placeholder(placeHolder).error(errorDrawable).into(imageView);
        Picasso.get().load(url).placeholder(placeHolder).fit().into(imageView);
        //Picasso.get().load(url).resize(800,400).into(imageView);

    }
}
