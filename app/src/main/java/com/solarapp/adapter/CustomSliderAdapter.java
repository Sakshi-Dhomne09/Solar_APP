package com.solarapp.adapter;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class CustomSliderAdapter extends SliderAdapter {


    List<String> images = new ArrayList<>();

    public CustomSliderAdapter(List<String> images) {
        this.images = images;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {

        imageSlideViewHolder.bindImageSlide(images.get(position));
    }
}
