package com.example.myidol.model;

import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class Photo {
    String linkImage;

    public Photo() {
    }

    public Photo(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Log.d("test","binđimg image");
        Glide.with(view.getContext())
                .load(imageUrl)
                .into(view);
    }
}
